# API Documentation

## Overview

This API documentation covers the REST endpoints for the Android Todo application. During development, these endpoints are mocked using a MockInterceptor to simulate real API responses:

- **Complete Task Management Application**: Full-featured task management with mock API integration for development

## Base URL
`https://api.todoapp.com/v1`

## Authentication

### Bearer Token Authentication
All API requests require a Bearer token in the Authorization header:
```
Authorization: Bearer {access_token}
```

### Authentication Endpoints

#### POST /auth/login
**Send OTP to user's phone number**

**Request Body**:
```json
{
  "phoneNumber": "+1234567890"
}
```

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "message": "OTP sent successfully",
    "expiresIn": 300
  }
}
```

#### POST /auth/verify-otp
**Verify OTP and get access token**

**Request Body**:
```json
{
  "phoneNumber": "+1234567890",
  "otp": "123456"
}
```

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "token": "mock_access_token_123",
    "refreshToken": "mock_refresh_token_123",
    "expiresIn": 3600,
    "user": {
      "id": "user_123",
      "phoneNumber": "+1234567890",
      "name": "John Doe"
    }
  }
}
```

#### POST /auth/logout
**Logout user and invalidate token**

**Authorization**: `Bearer {access_token}`

**Response (200)**:
```json
{
  "success": true,
  "message": "Logged out successfully"
}
```

## Task Management Endpoints

### Basic Task Operations

#### GET /tasks
**Get all tasks for the authenticated user**

**Query Parameters**:
- `page` (optional): Page number for pagination (default: 1)
- `limit` (optional): Number of items per page (default: 20)
- `priority` (optional): Filter by priority (high, medium, low)
- `category` (optional): Filter by category ID
- `dueDate` (optional): Filter by due date (YYYY-MM-DD)
- `completed` (optional): Filter by completion status (true, false)
- `search` (optional): Search in title and description
- `sortBy` (optional): Sort field (title, priority, dueDate, createdAt)
- `sortOrder` (optional): Sort order (asc, desc)

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "tasks": [
      {
        "id": "task_123",
        "title": "Complete project documentation",
        "description": "Write comprehensive documentation for the new feature",
        "priority": "high",
        "category": {
          "id": "cat_1",
          "name": "Work",
          "color": "#FF5722",
          "createdAt": "2024-01-15T10:30:00Z"
        },
        "dueDate": "2024-01-20T23:59:59Z",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 1,
      "totalPages": 1
    }
  }
}
```

#### POST /tasks
**Create a new task**

**Request Body**:
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the new feature",
  "priority": "high",
  "categoryId": "cat_1",
  "dueDate": "2024-01-20T23:59:59Z"
}
```

**Response (201)**:
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the new feature",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

#### GET /tasks/{taskId}
**Get a specific task by ID**

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the new feature",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

#### PUT /tasks/{taskId}
**Update a task**

**Request Body**:
```json
{
  "title": "Updated task title",
  "description": "Updated task description",
  "priority": "medium",
  "categoryId": "cat_2",
  "dueDate": "2024-01-25T23:59:59Z",
  "completed": true
}
```

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Updated task title",
    "description": "Updated task description",
    "priority": "medium",
    "category": {
      "id": "cat_2",
      "name": "Personal",
      "color": "#4CAF50",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    "dueDate": "2024-01-25T23:59:59Z",
    "completed": true,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

#### DELETE /tasks/{taskId}
**Delete a task**

**Response (200)**:
```json
{
  "success": true,
  "message": "Task deleted successfully"
}
```

#### PATCH /tasks/{taskId}/complete
**Mark a task as completed**

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the new feature",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": true,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

## Enhanced Task Management Endpoints

### Advanced Task Features

#### GET /tasks (Enhanced)
**Get tasks with advanced filtering and sorting**

**Query Parameters**:
- `page` (optional): Page number for pagination (default: 1)
- `limit` (optional): Number of items per page (default: 20)
- `priority` (optional): Filter by priority (high, medium, low)
- `category` (optional): Filter by category ID
- `dueDate` (optional): Filter by due date (YYYY-MM-DD)
- `completed` (optional): Filter by completion status (true, false)
- `search` (optional): Search in title and description
- `sortBy` (optional): Sort field (title, priority, dueDate, createdAt)
- `sortOrder` (optional): Sort order (asc, desc)

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "tasks": [
      {
        "id": "task_123",
        "title": "Complete project documentation",
        "description": "Write comprehensive documentation for the new feature",
        "priority": "high",
        "category": {
          "id": "cat_1",
          "name": "Work",
          "color": "#FF5722"
        },
        "dueDate": "2024-01-20T23:59:59Z",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "pagination": {
      "page": 1,
      "limit": 20,
      "total": 1,
      "totalPages": 1
    }
  }
}
```

#### POST /tasks (Enhanced)
**Create a task with advanced properties**

**Request Body**:
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive documentation for the new feature",
  "priority": "high",
  "categoryId": "cat_1",
  "dueDate": "2024-01-20T23:59:59Z"
}
```

**Response (201)**:
```json
{
  "success": true,
  "data": {
    "id": "task_123",
    "title": "Complete project documentation",
    "description": "Write comprehensive documentation for the new feature",
    "priority": "high",
    "category": {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722"
    },
    "dueDate": "2024-01-20T23:59:59Z",
    "completed": false,
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T10:30:00Z"
  }
}
```

### Category Management

#### GET /categories
**Get all categories for the user**

**Response (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": "cat_1",
      "name": "Work",
      "color": "#FF5722",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "cat_2",
      "name": "Personal",
      "color": "#4CAF50",
      "createdAt": "2024-01-15T10:30:00Z"
    }
  ]
}
```

#### POST /categories
**Create a new category**

**Request Body**:
```json
{
  "name": "Work",
  "color": "#FF5722"
}
```

**Response (201)**:
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Work",
    "color": "#FF5722",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

#### PUT /categories/{categoryId}
**Update a category**

**Request Body**:
```json
{
  "name": "Updated Work",
  "color": "#FF9800"
}
```

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Updated Work",
    "color": "#FF9800",
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

#### DELETE /categories/{categoryId}
**Delete a category**

**Response (200)**:
```json
{
  "success": true,
  "message": "Category deleted successfully"
}
```

#### POST /categories
**Create a new category**

**Request Body**:
```json
{
  "name": "Work",
  "color": "#FF5722"
}
```

**Response (201)**:
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Work",
    "color": "#FF5722",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

#### PUT /categories/{categoryId}
**Update a category**

**Request Body**:
```json
{
  "name": "Updated Work",
  "color": "#FF9800"
}
```

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "id": "cat_1",
    "name": "Updated Work",
    "color": "#FF9800",
    "createdAt": "2024-01-15T10:30:00Z",
    "updatedAt": "2024-01-15T11:45:00Z"
  }
}
```

#### DELETE /categories/{categoryId}
**Delete a category**

**Response (200)**:
```json
{
  "success": true,
  "message": "Category deleted successfully"
}
```

### Bulk Operations

#### POST /tasks/bulk
**Perform bulk operations on tasks**

**Request Body**:
```json
{
  "operation": "complete",
  "taskIds": ["task_123", "task_124", "task_125"],
  "categoryId": "cat_1",
  "priority": "high"
}
```

**Available Operations**:
- `complete`: Mark tasks as completed
- `delete`: Delete tasks
- `updatePriority`: Update priority for all tasks
- `moveToCategory`: Move tasks to a different category

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "updatedCount": 3,
    "message": "Successfully completed 3 tasks"
  }
}
```

### Search and Analytics

#### GET /tasks/search
**Advanced search functionality**

**Query Parameters**:
- `q` (required): Search query
- `fields` (optional): Search fields (title, description, all)
- `fuzzy` (optional): Enable fuzzy search (true, false)

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "tasks": [
      {
        "id": "task_123",
        "title": "Complete project documentation",
        "description": "Write comprehensive documentation for the new feature",
        "priority": "high",
        "category": {
          "id": "cat_1",
          "name": "Work",
          "color": "#FF5722",
          "createdAt": "2024-01-15T10:30:00Z"
        },
        "dueDate": "2024-01-20T23:59:59Z",
        "completed": false,
        "createdAt": "2024-01-15T10:30:00Z",
        "updatedAt": "2024-01-15T10:30:00Z"
      }
    ],
    "total": 1
  }
}
```

#### GET /tasks/analytics
**Get task analytics and statistics**

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "total": 25,
    "completed": 15,
    "pending": 10,
    "overdue": 3,
    "byPriority": {
      "high": 8,
      "medium": 12,
      "low": 5
    },
    "byCategory": [
      {
        "category": "Work",
        "count": 15
      },
      {
        "category": "Personal",
        "count": 10
      }
    ],
    "completionRate": 60.0
  }
}
```

## Feedback Endpoints

### Feedback Management

#### POST /feedback
**Submit user feedback**

**Authorization**: `Bearer {access_token}`

**Request Body**:
```json
{
  "rating": 5,
  "comment": "Great app! Very user-friendly interface.",
  "category": "general"
}
```

**Response (201)**:
```json
{
  "success": true,
  "data": {
    "id": "feedback_123",
    "rating": 5,
    "comment": "Great app! Very user-friendly interface.",
    "category": "general",
    "createdAt": "2024-01-15T10:30:00Z"
  }
}
```

#### GET /feedback
**Get user's feedback history**

**Authorization**: `Bearer {access_token}`

**Response (200)**:
```json
{
  "success": true,
  "data": [
    {
      "id": "feedback_123",
      "rating": 5,
      "comment": "Great app! Very user-friendly interface.",
      "category": "general",
      "createdAt": "2024-01-15T10:30:00Z"
    },
    {
      "id": "feedback_124",
      "rating": 4,
      "comment": "Good app, but could use more features.",
      "category": "feature_request",
      "createdAt": "2024-01-10T15:45:00Z"
    }
  ]
}
```

#### GET /feedback/analytics
**Get feedback analytics (admin only)**

**Authorization**: `Bearer {access_token}`

**Response (200)**:
```json
{
  "success": true,
  "data": {
    "totalFeedback": 150,
    "averageRating": 4.2,
    "ratingDistribution": {
      "5": 60,
      "4": 45,
      "3": 25,
      "2": 15,
      "1": 5
    },
    "categoryDistribution": {
      "general": 80,
      "feature_request": 40,
      "bug_report": 20,
      "improvement": 10
    }
  }
}
```

## Error Responses

### Standard Error Format
```json
{
  "success": false,
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Invalid request data",
    "details": {
      "title": "Title is required"
    }
  }
}
```

### Not Found Error
```json
{
  "success": false,
  "error": {
    "code": "NOT_FOUND",
    "message": "Endpoint not found"
  }
}
```

### Common Error Codes
- `400`: Bad Request - Invalid request data
- `401`: Unauthorized - Invalid or missing authentication
- `403`: Forbidden - Insufficient permissions
- `404`: Not Found - Resource not found
- `422`: Validation Error - Invalid input data
- `500`: Internal Server Error - Server error

## Rate Limiting

- **Rate Limit**: 1000 requests per hour per user
- **Headers**:
  - `X-RateLimit-Limit`: Request limit per hour
  - `X-RateLimit-Remaining`: Remaining requests
  - `X-RateLimit-Reset`: Time when limit resets

## Data Formats

### Date Format
All dates are in ISO 8601 format: `YYYY-MM-DDTHH:mm:ssZ`

### Priority Values
- `high`: High priority tasks
- `medium`: Medium priority tasks
- `low`: Low priority tasks

### Task Status
- `completed`: Task is completed
- `pending`: Task is pending (default)

## Mock API Implementation

### MockInterceptor
During development, the app uses a MockInterceptor to simulate API responses without requiring a backend server.

```kotlin
class MockInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val mockResponse = when {
            // Authentication endpoints
            request.url.encodedPath.contains("/auth/login") && request.method == "POST" -> {
                createMockLoginResponse()
            }
            request.url.encodedPath.contains("/auth/verify-otp") && request.method == "POST" -> {
                createMockVerifyOtpResponse()
            }
            request.url.encodedPath.contains("/auth/logout") && request.method == "POST" -> {
                createMockLogoutResponse()
            }
            // Task endpoints
            request.url.encodedPath.contains("/tasks") && request.method == "GET" -> {
                createMockTasksResponse()
            }
            request.url.encodedPath.contains("/tasks") && request.method == "POST" -> {
                createMockCreateTaskResponse(request)
            }
            request.url.encodedPath.contains("/tasks") && request.method == "PUT" -> {
                createMockUpdateTaskResponse(request)
            }
            request.url.encodedPath.contains("/tasks") && request.method == "DELETE" -> {
                createMockDeleteTaskResponse()
            }
            // Feedback endpoints
            request.url.encodedPath.contains("/feedback") && request.method == "POST" -> {
                createMockSubmitFeedbackResponse()
            }
            request.url.encodedPath.contains("/feedback") && request.method == "GET" -> {
                createMockGetFeedbackResponse()
            }
            else -> {
                createMockNotFoundResponse()
            }
        }
        return mockResponse
    }
    
    private fun createMockLoginResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "message": "OTP sent successfully",
                "expiresIn": 300
            }
        }
        """.trimIndent()
        
        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/auth/login").build())
            .protocol(Protocol.HTTP_1_1)
            .body(ResponseBody.create(MediaType.get("application/json"), mockJson))
            .build()
    }
    
    private fun createMockVerifyOtpResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "token": "mock_access_token_123",
                "refreshToken": "mock_refresh_token_123",
                "expiresIn": 3600,
                "user": {
                    "id": "user_123",
                    "phoneNumber": "+1234567890",
                    "name": "John Doe"
                }
            }
        }
        """.trimIndent()
        
        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/auth/verify-otp").build())
            .protocol(Protocol.HTTP_1_1)
            .body(ResponseBody.create(MediaType.get("application/json"), mockJson))
            .build()
    }
    
    private fun createMockTasksResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "tasks": [
                    {
                        "id": "task_123",
                        "title": "Complete project documentation",
                        "description": "Write comprehensive documentation",
                        "completed": false,
                        "createdAt": "2024-01-15T10:30:00Z",
                        "updatedAt": "2024-01-15T10:30:00Z"
                    }
                ],
                "pagination": {
                    "page": 1,
                    "limit": 20,
                    "total": 1,
                    "totalPages": 1
                }
            }
        }
        """.trimIndent()
        
        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/tasks").build())
            .protocol(Protocol.HTTP_1_1)
            .body(ResponseBody.create(MediaType.get("application/json"), mockJson))
            .build()
    }
}
```

### Integration with OkHttp
```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(MockInterceptor()) // Add mock interceptor for development
            .addInterceptor(LoggingInterceptor()) // Add logging for debugging
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.todoapp.com/v1/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}
```

## SDKs and Libraries

### Android (Kotlin)
```kotlin
// Retrofit service interface
interface TodoApiService {
    @GET("tasks")
    suspend fun getTasks(@Query("page") page: Int = 1): ApiResponse<TaskListResponse>
    
    @POST("tasks")
    suspend fun createTask(@Body task: CreateTaskRequest): ApiResponse<Task>
    
    @PUT("tasks/{taskId}")
    suspend fun updateTask(@Path("taskId") taskId: String, @Body task: UpdateTaskRequest): ApiResponse<Task>
    
    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String): ApiResponse<Unit>
}
```

## Support

For API support and questions:
- **Email**: api-support@todoapp.com
- **Documentation**: https://docs.todoapp.com/api
- **Status Page**: https://status.todoapp.com 