package com.mobizonetech.todo.core.config

import com.mobizonetech.todo.BuildConfig

object ApiConfig {
    
    // Environment configuration
    enum class Environment {
        MOCK,
        DEVELOPMENT,
        STAGING,
        PRODUCTION
    }
    
    // Current environment - can be set via BuildConfig or manually
    val currentEnvironment: Environment = when (BuildConfig.ENVIRONMENT) {
        "MOCK" -> Environment.MOCK
        "DEVELOPMENT" -> Environment.DEVELOPMENT
        "PRODUCTION" -> Environment.PRODUCTION
        else -> Environment.DEVELOPMENT
    }
    
    // API Configuration based on environment
    val baseUrl: String = BuildConfig.BASE_URL
    
    // Mock API configuration
    private var _useMockApi: Boolean = BuildConfig.USE_MOCK_API
    
    val useMockApi: Boolean
        get() = _useMockApi
    
    fun setMockApiEnabled(enabled: Boolean) {
        _useMockApi = enabled
    }
    
    fun toggleMockApi() {
        _useMockApi = !_useMockApi
    }
    
    // Logging configuration
    val enableApiLogging: Boolean = BuildConfig.ENABLE_API_LOGGING
    
    // Timeout configuration
    val connectTimeout: Long = 30L
    val readTimeout: Long = 30L
    val writeTimeout: Long = 30L
    
    // Retry configuration
    val maxRetryAttempts: Int = 3
    val retryDelayMs: Long = 1000L
    
    // Cache configuration
    val cacheSize: Int = 100
    val cacheExpiryHours: Long = 24L
    
    // Security configuration
    val tokenExpiryHours: Long = 24L
    val refreshTokenExpiryDays: Long = 30L
    
    // Debug information
    fun getDebugInfo(): String {
        return """
            API Configuration:
            - Environment: $currentEnvironment
            - Base URL: $baseUrl
            - Mock API: $useMockApi
            - API Logging: $enableApiLogging
            - Connect Timeout: ${connectTimeout}s
            - Read Timeout: ${readTimeout}s
            - Write Timeout: ${writeTimeout}s
        """.trimIndent()
    }
} 