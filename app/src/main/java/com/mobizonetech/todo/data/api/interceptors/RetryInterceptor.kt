package com.mobizonetech.todo.data.api.interceptors

import com.mobizonetech.todo.core.config.ApiConfig
import com.mobizonetech.todo.core.config.RetryConfig
import com.mobizonetech.todo.util.ApiLogger
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {

    companion object {
        private val currentEnvironment = ApiConfig.currentEnvironment
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var lastException: IOException? = null

        val maxRetries = RetryConfig.getMaxRetries(currentEnvironment)
        // Try the request up to maxRetries times
        for (attempt in 0..maxRetries) {
            try {
                response = chain.proceed(request)
                
                // If response is successful, return it
                if (response.isSuccessful) {
                    return response
                }

                // Check if the error is retryable
                if (isRetryableError(response.code)) {
                    if (attempt < maxRetries) {
                        val delay = calculateRetryDelay(attempt)
                        ApiLogger.logApiRetry(request.url.toString(), attempt + 1, maxRetries)
                        Thread.sleep(delay)
                        response.close()
                        continue
                    }
                } else {
                    // Non-retryable error, return the response
                    return response
                }
            } catch (e: IOException) {
                lastException = e
                
                // Check if the exception is retryable
                if (isRetryableException(e) && attempt < maxRetries) {
                    val delay = calculateRetryDelay(attempt)
                    ApiLogger.logApiRetry(request.url.toString(), attempt + 1, maxRetries)
                    Thread.sleep(delay)
                    continue
                } else {
                    // Non-retryable exception or max retries reached
                    throw e
                }
            }
        }

        // If we get here, all retries failed
        response?.close()
        throw lastException ?: IOException("Request failed after $maxRetries attempts")
    }

    /**
     * Determines if an HTTP status code is retryable
     */
    private fun isRetryableError(statusCode: Int): Boolean {
        return RetryConfig.isRetryableStatusCode(statusCode)
    }

    /**
     * Determines if an exception is retryable
     */
    private fun isRetryableException(exception: IOException): Boolean {
        return RetryConfig.isRetryableException(exception)
    }

    /**
     * Calculates retry delay with exponential backoff
     */
    private fun calculateRetryDelay(attempt: Int): Long {
        return RetryConfig.calculateRetryDelay(attempt, currentEnvironment)
    }
} 