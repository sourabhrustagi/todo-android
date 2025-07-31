package com.mobizonetech.todo.data.api.interceptors

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer
import java.io.IOException
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

class ApiLoggingInterceptor : Interceptor {
    
    companion object {
        private const val TAG = "API_LOGGING"
        private const val MAX_BODY_LENGTH = 4000
    }
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startTime = System.currentTimeMillis()
        
        // Extract API name from URL path
        val apiName = extractApiName(request.url.toString())
        
        // Log request details
        logRequest(request, apiName)
        
        // Execute request
        val response = try {
            chain.proceed(request)
        } catch (e: Exception) {
            Log.e(TAG, "❌ API Error - $apiName: ${e.message}")
            throw e
        }
        
        val endTime = System.currentTimeMillis()
        val duration = endTime - startTime
        
        // Log response details
        logResponse(response, apiName, duration)
        
        return response
    }
    
    private fun extractApiName(url: String): String {
        return try {
            val path = url.substringAfter("v1/", "")
            when {
                path.startsWith("auth/") -> "AUTH_${path.substringAfter("auth/").uppercase()}"
                path.startsWith("tasks/") -> "TASKS_${path.substringAfter("tasks/").uppercase()}"
                path == "tasks" -> "TASKS_LIST"
                path.startsWith("categories/") -> "CATEGORIES_${path.substringAfter("categories/").uppercase()}"
                path == "categories" -> "CATEGORIES_LIST"
                path.startsWith("feedback/") -> "FEEDBACK_${path.substringAfter("feedback/").uppercase()}"
                path == "feedback" -> "FEEDBACK_SUBMIT"
                else -> "UNKNOWN_API"
            }
        } catch (e: Exception) {
            "UNKNOWN_API"
        }
    }
    
    private fun logRequest(request: okhttp3.Request, apiName: String) {
        val url = request.url.toString()
        val method = request.method
        val headers = request.headers.toString()
        
        Log.d(TAG, "🚀 API Request - $apiName")
        Log.d(TAG, "   Method: $method")
        Log.d(TAG, "   URL: $url")
        Log.d(TAG, "   Headers: $headers")
        
        // Log request body if present
        request.body?.let { body ->
            try {
                val buffer = Buffer()
                body.writeTo(buffer)
                val requestBody = buffer.readString(StandardCharsets.UTF_8)
                Log.d(TAG, "   Body: ${truncateBody(requestBody)}")
            } catch (e: IOException) {
                Log.d(TAG, "   Body: [Could not read request body]")
            }
        }
    }
    
    private fun logResponse(response: Response, apiName: String, duration: Long) {
        val statusCode = response.code
        val statusMessage = response.message
        val headers = response.headers.toString()
        val isSuccess = response.isSuccessful
        
        val statusIcon = if (isSuccess) "✅" else "❌"
        val durationText = formatDuration(duration)
        
        Log.d(TAG, "$statusIcon API Response - $apiName ($durationText)")
        Log.d(TAG, "   Status: $statusCode $statusMessage")
        Log.d(TAG, "   Headers: $headers")
        
        // Log response body
        response.body?.let { body ->
            try {
                val responseBody = body.string()
                Log.d(TAG, "   Body: ${truncateBody(responseBody)}")
                
                // Create a new response body since we consumed the original
                val newBody = ResponseBody.create(body.contentType(), responseBody)
                response.newBuilder().body(newBody).build()
            } catch (e: IOException) {
                Log.d(TAG, "   Body: [Could not read response body]")
                response
            }
        } ?: response
    }
    
    private fun truncateBody(body: String): String {
        return if (body.length > MAX_BODY_LENGTH) {
            body.substring(0, MAX_BODY_LENGTH) + "... [truncated]"
        } else {
            body
        }
    }
    
    private fun formatDuration(duration: Long): String {
        return when {
            duration < 1000 -> "${duration}ms"
            duration < 60000 -> "${duration / 1000.0}s"
            else -> "${duration / 60000.0}m"
        }
    }
} 