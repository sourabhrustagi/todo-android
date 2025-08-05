package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.core.config.ApiConfig
import com.mobizonetech.todo.core.config.RetryConfig
import com.mobizonetech.todo.util.ApiLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.retryWhen
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetryManager @Inject constructor() {

    /**
     * Wraps a Flow with retry logic
     */
    fun <T> Flow<T>.withRetry(
        maxRetries: Int = RetryConfig.getMaxRetries(ApiConfig.currentEnvironment),
        operationName: String = "Unknown"
    ): Flow<T> {
        return this.retryWhen { cause, attempt ->
            if (attempt > maxRetries) {
                ApiLogger.logApiError(operationName, cause)
                false // Stop retrying
            } else if (RetryConfig.isRetryableException(cause)) {
                val delay = RetryConfig.calculateRetryDelay(attempt.toInt(), ApiConfig.currentEnvironment)
                ApiLogger.logApiRetry(operationName, attempt.toInt(), maxRetries)
                delay(delay)
                true // Continue retrying
            } else {
                ApiLogger.logApiError(operationName, cause)
                false // Don't retry non-retryable exceptions
            }
        }
    }

    /**
     * Wraps a suspend function with retry logic
     */
    suspend fun <T> retry(
        maxRetries: Int = RetryConfig.getMaxRetries(ApiConfig.currentEnvironment),
        operationName: String = "Unknown",
        block: suspend () -> T
    ): T {
        var lastException: Throwable? = null
        
        for (attempt in 0..maxRetries) {
            try {
                return block()
            } catch (e: Throwable) {
                lastException = e
                
                if (attempt < maxRetries && RetryConfig.isRetryableException(e)) {
                    val delay = RetryConfig.calculateRetryDelay(attempt, ApiConfig.currentEnvironment)
                    ApiLogger.logApiRetry(operationName, attempt + 1, maxRetries)
                    delay(delay)
                } else {
                    ApiLogger.logApiError(operationName, e)
                    throw e
                }
            }
        }
        
        throw lastException ?: IOException("Operation failed after $maxRetries attempts")
    }

    /**
     * Wraps a suspend function with retry logic and returns Result
     */
    suspend fun <T> retryWithResult(
        maxRetries: Int = RetryConfig.getMaxRetries(ApiConfig.currentEnvironment),
        operationName: String = "Unknown",
        block: suspend () -> T
    ): com.mobizonetech.todo.core.result.Result<T> {
        return try {
            val result = retry(maxRetries, operationName, block)
            com.mobizonetech.todo.core.result.Result.success(result)
        } catch (e: Throwable) {
            com.mobizonetech.todo.core.result.Result.error(e)
        }
    }

    /**
     * Determines if an operation should be retried based on the exception
     */
    fun shouldRetry(exception: Throwable): Boolean {
        return RetryConfig.isRetryableException(exception)
    }

    /**
     * Gets the appropriate retry count for the current environment
     */
    fun getMaxRetries(): Int {
        return RetryConfig.getMaxRetries(ApiConfig.currentEnvironment)
    }
} 