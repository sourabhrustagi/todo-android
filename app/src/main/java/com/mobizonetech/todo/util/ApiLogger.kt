package com.mobizonetech.todo.util

import android.util.Log
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import java.text.SimpleDateFormat
import java.util.*

object ApiLogger {
    
    private const val TAG = "API_LOGGER"
    private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS"
    
    // Configuration
    var isEnabled: Boolean = true
    var logLevel: LogLevel = LogLevel.DEBUG
    
    enum class LogLevel {
        VERBOSE, DEBUG, INFO, WARNING, ERROR
    }
    
    fun logApiCall(apiName: String, requestData: Any? = null) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        log(LogLevel.DEBUG, "🔄 [$timestamp] API Call Started: $apiName")
        requestData?.let {
            log(LogLevel.DEBUG, "   Request Data: $it")
        }
    }
    
    fun logApiSuccess(apiName: String, responseData: Any? = null, duration: Long? = null) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val durationText = duration?.let { " (${formatDuration(it)})" } ?: ""
        log(LogLevel.INFO, "✅ [$timestamp] API Success: $apiName$durationText")
        responseData?.let {
            log(LogLevel.DEBUG, "   Response Data: $it")
        }
    }
    
    fun logApiError(apiName: String, error: Throwable, duration: Long? = null) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val durationText = duration?.let { " (${formatDuration(it)})" } ?: ""
        log(LogLevel.ERROR, "❌ [$timestamp] API Error: $apiName$durationText")
        log(LogLevel.ERROR, "   Error: ${error.message}")
        log(LogLevel.DEBUG, "   Stack Trace: ${error.stackTraceToString()}")
    }
    
    fun logApiTimeout(apiName: String, duration: Long) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        log(LogLevel.WARNING, "⏰ [$timestamp] API Timeout: $apiName (${formatDuration(duration)})")
    }
    
    fun logApiRetry(apiName: String, attempt: Int, maxAttempts: Int) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        log(LogLevel.WARNING, "🔄 [$timestamp] API Retry: $apiName (Attempt $attempt/$maxAttempts)")
    }
    
    fun logApiCache(apiName: String, isFromCache: Boolean) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        val cacheText = if (isFromCache) "CACHE" else "NETWORK"
        log(LogLevel.DEBUG, "💾 [$timestamp] API $cacheText: $apiName")
    }
    
    fun logApiNetworkError(apiName: String, errorType: String, errorMessage: String) {
        if (!isEnabled) return
        
        val timestamp = SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(Date())
        log(LogLevel.ERROR, "🌐 [$timestamp] API Network Error: $apiName - $errorType: $errorMessage")
    }
    
    private fun log(level: LogLevel, message: String) {
        if (level.ordinal >= logLevel.ordinal) {
            when (level) {
                LogLevel.VERBOSE -> Log.v(TAG, message)
                LogLevel.DEBUG -> Log.d(TAG, message)
                LogLevel.INFO -> Log.i(TAG, message)
                LogLevel.WARNING -> Log.w(TAG, message)
                LogLevel.ERROR -> Log.e(TAG, message)
            }
        }
    }
    
    private fun formatDuration(duration: Long): String {
        return when {
            duration < 1000 -> "${duration}ms"
            duration < 60000 -> "${duration / 1000.0}s"
            else -> "${duration / 60000.0}m"
        }
    }
    
    // Extension function for Flow to add logging
    fun <T> Flow<T>.logApiFlow(apiName: String): Flow<T> {
        val startTime = System.currentTimeMillis()
        return this
            .onStart {
                logApiCall(apiName)
            }
            .onEach { result ->
                val duration = System.currentTimeMillis() - startTime
                logApiSuccess(apiName, result, duration)
            }
            .catch { error ->
                val duration = System.currentTimeMillis() - startTime
                logApiError(apiName, error, duration)
                throw error
            }
    }
    
    // Utility function to enable/disable logging
    fun configure(enabled: Boolean = true, level: LogLevel = LogLevel.DEBUG) {
        isEnabled = enabled
        logLevel = level
        log(LogLevel.INFO, "API Logger configured - Enabled: $enabled, Level: $level")
    }
} 