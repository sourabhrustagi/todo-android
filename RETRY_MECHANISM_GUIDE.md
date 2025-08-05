# Retry Mechanism Guide

## Overview

The Android Todo app implements a comprehensive retry mechanism to handle network failures gracefully. This ensures robust API communication and better user experience.

## Architecture

### 1. **Multi-Level Retry Strategy**

#### **Level 1: HTTP Interceptor (RetryInterceptor)**
- **Purpose**: Handles low-level network failures
- **Scope**: All HTTP requests automatically
- **Configuration**: Environment-specific retry settings

#### **Level 2: Repository-Level Retry (RetryManager)**
- **Purpose**: Handles business logic retries
- **Scope**: Specific operations with custom logic
- **Configuration**: Operation-specific retry settings

### 2. **Retry Configuration (RetryConfig)**

```kotlin
object RetryConfig {
    // Environment-specific settings
    fun getMaxRetries(environment: Environment): Int
    fun getInitialDelay(environment: Environment): Long
    fun getMaxDelay(environment: Environment): Long
    
    // Retryable conditions
    fun isRetryableStatusCode(statusCode: Int): Boolean
    fun isRetryableException(exception: Throwable): Boolean
    
    // Exponential backoff calculation
    fun calculateRetryDelay(attempt: Int, environment: Environment): Long
}
```

## Implementation Details

### 1. **HTTP Interceptor Retry**

#### **RetryInterceptor.kt**
```kotlin
@Singleton
class RetryInterceptor @Inject constructor() : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val maxRetries = RetryConfig.getMaxRetries(currentEnvironment)
        
        for (attempt in 0..maxRetries) {
            try {
                val response = chain.proceed(request)
                
                if (response.isSuccessful) {
                    return response
                }
                
                if (isRetryableError(response.code) && attempt < maxRetries) {
                    val delay = calculateRetryDelay(attempt)
                    ApiLogger.logApiRetry(request.url.toString(), attempt + 1, maxRetries)
                    Thread.sleep(delay)
                    response.close()
                    continue
                } else {
                    return response
                }
            } catch (e: IOException) {
                if (isRetryableException(e) && attempt < maxRetries) {
                    val delay = calculateRetryDelay(attempt)
                    ApiLogger.logApiRetry(request.url.toString(), attempt + 1, maxRetries)
                    Thread.sleep(delay)
                    continue
                } else {
                    throw e
                }
            }
        }
        
        throw IOException("Request failed after $maxRetries attempts")
    }
}
```

#### **Features:**
- ✅ **Automatic retry**: All HTTP requests are automatically retried
- ✅ **Environment-specific**: Different retry settings per environment
- ✅ **Exponential backoff**: Smart delay calculation
- ✅ **Comprehensive logging**: Detailed retry logging
- ✅ **Exception handling**: Proper exception classification

### 2. **Repository-Level Retry**

#### **RetryManager.kt**
```kotlin
@Singleton
class RetryManager @Inject constructor() {
    
    // Flow retry wrapper
    fun <T> Flow<T>.withRetry(
        maxRetries: Int = RetryConfig.getMaxRetries(),
        operationName: String = "Unknown"
    ): Flow<T>
    
    // Suspend function retry wrapper
    suspend fun <T> retry(
        maxRetries: Int = RetryConfig.getMaxRetries(),
        operationName: String = "Unknown",
        block: suspend () -> T
    ): T
    
    // Result-based retry wrapper
    suspend fun <T> retryWithResult(
        maxRetries: Int = RetryConfig.getMaxRetries(),
        operationName: String = "Unknown",
        block: suspend () -> T
    ): Result<T>
}
```

#### **Usage Examples:**

**1. Flow Retry:**
```kotlin
override fun getTasks(): Flow<Result<List<Task>>> = flow {
    val tasks = apiService.getTasks()
        .withRetry(operationName = "GET_TASKS")
        .first()
    
    emit(Result.success(tasks))
}
```

**2. Suspend Function Retry:**
```kotlin
val task = retryManager.retry(operationName = "CREATE_TASK") {
    apiService.createTask(taskRequest)
}
```

**3. Result-Based Retry:**
```kotlin
val result = retryManager.retryWithResult(operationName = "UPDATE_TASK") {
    apiService.updateTask(taskId, taskRequest)
}
```

## Environment-Specific Configuration

### **Mock Environment**
- **Max Retries**: 1 (minimal retries for development)
- **Initial Delay**: 100ms (fast retries)
- **Max Delay**: 1000ms (short delays)

### **Development Environment**
- **Max Retries**: 2 (moderate retries)
- **Initial Delay**: 500ms (moderate delays)
- **Max Delay**: 5000ms (reasonable delays)

### **Production Environment**
- **Max Retries**: 3 (full retries)
- **Initial Delay**: 1000ms (standard delays)
- **Max Delay**: 10000ms (maximum delays)

## Retryable Conditions

### **1. HTTP Status Codes**
```kotlin
val RETRYABLE_STATUS_CODES = setOf(
    500, // Internal Server Error
    502, // Bad Gateway
    503, // Service Unavailable
    504, // Gateway Timeout
    429  // Too Many Requests (Rate Limiting)
)
```

### **2. Network Exceptions**
```kotlin
fun isRetryableException(exception: Throwable): Boolean {
    return when (exception) {
        is SocketTimeoutException -> true
        is UnknownHostException -> true
        is ConnectException -> true
        is NoRouteToHostException -> true
        else -> {
            // Check for connection-related exceptions by message
            val message = exception.message?.lowercase() ?: ""
            RETRYABLE_EXCEPTION_PATTERNS.any { pattern ->
                message.contains(pattern)
            }
        }
    }
}
```

### **3. Exception Patterns**
```kotlin
val RETRYABLE_EXCEPTION_PATTERNS = setOf(
    "timeout",
    "connection",
    "network",
    "unreachable",
    "refused",
    "reset"
)
```

## Exponential Backoff Algorithm

### **Formula:**
```
delay = min(initialDelay * (backoffMultiplier ^ attempt), maxDelay)
```

### **Example Delays (Production):**
- **Attempt 1**: 1000ms
- **Attempt 2**: 2000ms
- **Attempt 3**: 4000ms
- **Attempt 4**: 8000ms (capped at 10000ms)

## Logging and Monitoring

### **1. Retry Logging**
```kotlin
ApiLogger.logApiRetry(url, attempt, maxRetries)
```

### **2. Error Logging**
```kotlin
ApiLogger.logApiError(operationName, exception)
```

### **3. Success Logging**
```kotlin
ApiLogger.logApiSuccess(operationName, result)
```

## Integration with Dependency Injection

### **NetworkModule.kt**
```kotlin
@Provides
@Singleton
fun provideOkHttpClient(
    mockApiManager: MockApiManager,
    retryInterceptor: RetryInterceptor
): OkHttpClient {
    return OkHttpClient.Builder().apply {
        // Add retry interceptor first
        addInterceptor(retryInterceptor)
        
        // Other interceptors...
        if (mockApiManager.isMockApiEnabled) {
            addInterceptor(MockInterceptor())
        }
        
        if (ApiConfig.enableApiLogging) {
            addInterceptor(ApiLoggingInterceptor())
        }
    }
    .connectTimeout(ApiConfig.connectTimeout, TimeUnit.SECONDS)
    .readTimeout(ApiConfig.readTimeout, TimeUnit.SECONDS)
    .writeTimeout(ApiConfig.writeTimeout, TimeUnit.SECONDS)
    .retryOnConnectionFailure(true)
    .build()
}
```

## Best Practices

### **1. Retry Strategy**
- ✅ **Use exponential backoff**: Prevents overwhelming the server
- ✅ **Limit retry attempts**: Avoid infinite loops
- ✅ **Environment-specific settings**: Different behavior per environment
- ✅ **Proper exception handling**: Distinguish retryable from non-retryable errors

### **2. Logging Strategy**
- ✅ **Log all retry attempts**: For debugging and monitoring
- ✅ **Log final failures**: For error analysis
- ✅ **Include relevant context**: URL, attempt number, delay
- ✅ **Use appropriate log levels**: WARNING for retries, ERROR for failures

### **3. Performance Considerations**
- ✅ **Minimize retry overhead**: Use efficient delay calculation
- ✅ **Avoid blocking operations**: Use coroutines for async retries
- ✅ **Cache successful responses**: Reduce unnecessary retries
- ✅ **Monitor retry patterns**: Identify problematic endpoints

### **4. User Experience**
- ✅ **Show loading states**: During retry attempts
- ✅ **Provide feedback**: When retries are happening
- ✅ **Graceful degradation**: Fallback to cached data
- ✅ **Clear error messages**: When all retries fail

## Testing

### **1. Unit Tests**
```kotlin
@Test
fun `retry should succeed after initial failure`() = runTest {
    var attempts = 0
    val result = retryManager.retry(operationName = "TEST") {
        attempts++
        if (attempts < 3) throw IOException("Network error")
        "Success"
    }
    
    assertEquals("Success", result)
    assertEquals(3, attempts)
}
```

### **2. Integration Tests**
```kotlin
@Test
fun `repository should retry failed API calls`() = runTest {
    // Given: API that fails initially then succeeds
    whenever(apiService.getTasks()).thenReturn(
        flowOf(Result.failure(IOException("Network error"))),
        flowOf(Result.success(listOf(task)))
    )
    
    // When: Repository makes API call
    val result = repository.getTasks().first()
    
    // Then: Should succeed after retry
    assertTrue(result is Result.Success)
}
```

## Configuration Options

### **1. Environment-Based Configuration**
```kotlin
// Mock environment - minimal retries
RetryConfig.getMaxRetries(Environment.MOCK) // Returns 1

// Development environment - moderate retries
RetryConfig.getMaxRetries(Environment.DEVELOPMENT) // Returns 2

// Production environment - full retries
RetryConfig.getMaxRetries(Environment.PRODUCTION) // Returns 3
```

### **2. Custom Retry Settings**
```kotlin
// Custom retry for critical operations
val result = retryManager.retry(
    maxRetries = 5,
    operationName = "CRITICAL_OPERATION"
) {
    criticalApiCall()
}
```

### **3. Flow Retry with Custom Settings**
```kotlin
val tasks = apiService.getTasks()
    .withRetry(
        maxRetries = 3,
        operationName = "GET_TASKS"
    )
    .first()
```

## Monitoring and Analytics

### **1. Retry Metrics**
- **Retry Count**: Number of retry attempts per operation
- **Success Rate**: Percentage of operations that succeed after retries
- **Average Retry Delay**: Time spent waiting between retries
- **Failure Patterns**: Types of errors that trigger retries

### **2. Performance Impact**
- **Network Overhead**: Additional requests due to retries
- **User Experience**: Impact on app responsiveness
- **Battery Usage**: Energy consumption from retry attempts
- **Data Usage**: Bandwidth consumption from retries

## Troubleshooting

### **Common Issues**

**1. Too Many Retries**
- **Cause**: Low retry thresholds
- **Solution**: Increase max retries or adjust retry conditions

**2. Slow Response Times**
- **Cause**: Long retry delays
- **Solution**: Reduce initial delay or max delay

**3. Battery Drain**
- **Cause**: Frequent retries
- **Solution**: Implement smarter retry logic or caching

**4. Server Overload**
- **Cause**: Aggressive retry strategy
- **Solution**: Implement rate limiting or backoff

### **Debugging Tips**

**1. Enable Detailed Logging**
```kotlin
ApiLogger.configure(enabled = true, level = ApiLogger.LogLevel.DEBUG)
```

**2. Monitor Retry Patterns**
```kotlin
// Add custom logging for retry analysis
ApiLogger.logApiRetry(url, attempt, maxRetries)
```

**3. Test Different Scenarios**
```kotlin
// Test with network simulation
// Test with server errors
// Test with timeout conditions
```

This retry mechanism ensures robust API communication while providing excellent user experience and proper error handling. 