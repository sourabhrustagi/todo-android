# Mock API Toggle Guide

## Overview

The Android Todo app includes a configurable mock API system that allows you to easily switch between mock responses and real API calls during development.

## How It Works

### 1. **Environment-Based Configuration**
The mock API is automatically enabled/disabled based on the build environment:
- **Development (Debug)**: Mock API enabled by default
- **Staging**: Mock API disabled (uses real API)
- **Production**: Mock API disabled (uses real API)

### 2. **Runtime Override**
You can override the default environment setting at runtime using the settings screen.

## Usage

### Method 1: Settings Screen (Recommended)
1. Open the app
2. Navigate to **Settings**
3. In debug builds, you'll see a **"Development Settings"** section
4. Toggle the **"Mock API"** switch to enable/disable mock responses
5. Restart the app to apply changes

### Method 2: Programmatic Control
```kotlin
// Inject the MockApiToggle
@Inject
lateinit var mockApiToggle: MockApiToggle

// Enable mock API
mockApiToggle.enableMockApi()

// Disable mock API
mockApiToggle.disableMockApi()

// Toggle mock API
mockApiToggle.toggleMockApi()

// Reset to default environment setting
mockApiToggle.resetToDefault()

// Check current status
val isEnabled = mockApiToggle.isMockApiEnabled
val status = mockApiToggle.getMockApiStatus()
```

### Method 3: Direct API Config
```kotlin
// Enable mock API
ApiConfig.setMockApiEnabled(true)

// Disable mock API
ApiConfig.setMockApiEnabled(false)

// Toggle mock API
ApiConfig.toggleMockApi()
```

## Configuration Files

### 1. **ApiConfig.kt**
Controls the default behavior based on environment:
```kotlin
object ApiConfig {
    val useMockApi: Boolean = when (currentEnvironment) {
        Environment.DEVELOPMENT -> true
        Environment.STAGING -> false
        Environment.PRODUCTION -> false
    }
}
```

### 2. **MockApiManager.kt**
Manages persistent settings and runtime overrides:
```kotlin
@Singleton
class MockApiManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val isMockApiEnabled: Boolean
        get() = // Checks override first, then falls back to environment default
    
    fun setMockApiEnabled(enabled: Boolean, override: Boolean = true)
    fun toggleMockApi()
    fun resetToDefault()
}
```

### 3. **NetworkModule.kt**
Applies the configuration to OkHttpClient:
```kotlin
@Provides
@Singleton
fun provideOkHttpClient(mockApiManager: MockApiManager): OkHttpClient {
    return OkHttpClient.Builder().apply {
        if (mockApiManager.isMockApiEnabled) {
            addInterceptor(MockInterceptor()) // Mock API responses
        }
        // ... other interceptors
    }.build()
}
```

## Mock Responses

When mock API is enabled, the following endpoints return mock responses:

### Authentication
- `POST /auth/login` - Returns mock OTP sent response
- `POST /auth/verify-otp` - Returns mock authentication tokens
- `POST /auth/logout` - Returns mock logout response

### Tasks
- `GET /tasks` - Returns mock task list
- `POST /tasks` - Returns mock created task
- `PUT /tasks/{id}` - Returns mock updated task
- `DELETE /tasks/{id}` - Returns mock deletion response
- `PATCH /tasks/{id}/complete` - Returns mock completion response

### Categories
- `GET /categories` - Returns mock category list
- `POST /categories` - Returns mock created category
- `PUT /categories/{id}` - Returns mock updated category
- `DELETE /categories/{id}` - Returns mock deletion response

### Analytics & Search
- `GET /tasks/analytics` - Returns mock analytics data
- `GET /tasks/search` - Returns mock search results

### Feedback
- `POST /feedback` - Returns mock feedback submission
- `GET /feedback` - Returns mock feedback history

## Important Notes

### 1. **App Restart Required**
Due to OkHttpClient being a singleton, changes to mock API settings require an app restart to take effect.

### 2. **Debug Only**
The settings screen toggle is only visible in debug builds (`BuildConfig.DEBUG`).

### 3. **Persistent Settings**
Mock API overrides are stored in SharedPreferences and persist across app launches.

### 4. **Environment Priority**
The system follows this priority:
1. User override (if set)
2. Environment default (development/staging/production)

## Debug Information

You can get detailed debug information about the mock API configuration:

```kotlin
val status = mockApiManager.getMockApiStatus()
println(status)
// Output:
// MockApiStatus(
//   isEnabled=true,
//   hasOverride=false,
//   defaultState=true,
//   currentState=true,
//   environment=DEVELOPMENT
// )
```

## Troubleshooting

### Mock API Not Working
1. Check if you're in debug mode
2. Verify the MockInterceptor is added to OkHttpClient
3. Check the ApiConfig.useMockApi value
4. Restart the app after changing settings

### Real API Not Working
1. Disable mock API in settings
2. Restart the app
3. Check network connectivity
4. Verify API endpoints are correct

## Example Usage in Development

```kotlin
// In your ViewModel or Repository
class TaskRepository @Inject constructor(
    private val apiService: TodoApiService,
    private val mockApiToggle: MockApiToggle
) {
    
    suspend fun getTasks(): Result<List<Task>> {
        return try {
            // This will use mock responses if mock API is enabled
            val response = apiService.getTasks()
            Result.success(response.data.tasks)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    // You can also check mock status for debugging
    fun logMockStatus() {
        if (BuildConfig.DEBUG) {
            Log.d("TaskRepository", mockApiToggle.getMockApiStatus())
        }
    }
}
```

This system provides a flexible way to switch between mock and real API responses during development while maintaining clean separation between environments. 