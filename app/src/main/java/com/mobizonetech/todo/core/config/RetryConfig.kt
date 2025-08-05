package com.mobizonetech.todo.core.config

import com.mobizonetech.todo.core.config.ApiConfig.Environment

/**
 * Configuration for API retry mechanism
 */
object RetryConfig {
    
    // Retry settings
    const val MAX_RETRIES = 3
    const val INITIAL_RETRY_DELAY_MS = 1000L
    const val MAX_RETRY_DELAY_MS = 10000L
    const val BACKOFF_MULTIPLIER = 2.0
    
    // Retryable HTTP status codes
    val RETRYABLE_STATUS_CODES = setOf(
        500, // Internal Server Error
        502, // Bad Gateway
        503, // Service Unavailable
        504, // Gateway Timeout
        429  // Too Many Requests (Rate Limiting)
    )
    
    // Retryable exception patterns
    val RETRYABLE_EXCEPTION_PATTERNS = setOf(
        "timeout",
        "connection",
        "network",
        "unreachable",
        "refused",
        "reset"
    )
    
    // Environment-specific retry settings
    fun getMaxRetries(environment: Environment): Int {
        return when (environment) {
            Environment.MOCK -> 1 // Minimal retries for mock
            Environment.DEVELOPMENT -> 2 // Fewer retries for development
            Environment.PRODUCTION -> MAX_RETRIES // Full retries for production
            else -> MAX_RETRIES
        }
    }
    
    fun getInitialDelay(environment: Environment): Long {
        return when (environment) {
            Environment.MOCK -> 100L // Faster retries for mock
            Environment.DEVELOPMENT -> 500L // Moderate delays for development
            Environment.PRODUCTION -> INITIAL_RETRY_DELAY_MS // Standard delays for production
            else -> INITIAL_RETRY_DELAY_MS
        }
    }
    
    fun getMaxDelay(environment: Environment): Long {
        return when (environment) {
            Environment.MOCK -> 1000L // Shorter max delay for mock
            Environment.DEVELOPMENT -> 5000L // Moderate max delay for development
            Environment.PRODUCTION -> MAX_RETRY_DELAY_MS // Standard max delay for production
            else -> MAX_RETRY_DELAY_MS
        }
    }
    
    /**
     * Determines if a status code is retryable
     */
    fun isRetryableStatusCode(statusCode: Int): Boolean {
        return statusCode in RETRYABLE_STATUS_CODES
    }
    
    /**
     * Determines if an exception is retryable
     */
    fun isRetryableException(exception: Throwable): Boolean {
        return when (exception) {
            is java.net.SocketTimeoutException -> true
            is java.net.UnknownHostException -> true
            is java.net.ConnectException -> true
            is java.net.NoRouteToHostException -> true
            else -> {
                // Check for connection-related exceptions by message
                val message = exception.message?.lowercase() ?: ""
                RETRYABLE_EXCEPTION_PATTERNS.any { pattern ->
                    message.contains(pattern)
                }
            }
        }
    }
    
    /**
     * Calculates retry delay with exponential backoff
     */
    fun calculateRetryDelay(attempt: Int, environment: Environment = Environment.PRODUCTION): Long {
        val initialDelay = getInitialDelay(environment)
        val maxDelay = getMaxDelay(environment)
        
        val delay = (initialDelay * Math.pow(BACKOFF_MULTIPLIER, attempt.toDouble())).toLong()
        return minOf(delay, maxDelay)
    }
} 