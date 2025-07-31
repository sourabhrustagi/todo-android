package com.mobizonetech.todo.data.api.interceptors

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.util.UUID

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
            request.url.encodedPath.contains("/tasks") && request.method == "GET" && !request.url.encodedPath.contains("/analytics") && !request.url.encodedPath.contains("/search") -> {
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
            request.url.encodedPath.contains("/tasks") && request.method == "PATCH" -> {
                createMockCompleteTaskResponse(request)
            }
            request.url.encodedPath.contains("/tasks/analytics") && request.method == "GET" -> {
                createMockTaskAnalyticsResponse()
            }
            request.url.encodedPath.contains("/tasks/search") && request.method == "GET" -> {
                createMockTaskSearchResponse()
            }
            // Category endpoints
            request.url.encodedPath.contains("/categories") && request.method == "GET" -> {
                createMockCategoriesResponse()
            }
            request.url.encodedPath.contains("/categories") && request.method == "POST" -> {
                createMockCreateCategoryResponse()
            }
            request.url.encodedPath.contains("/categories") && request.method == "PUT" -> {
                createMockUpdateCategoryResponse()
            }
            request.url.encodedPath.contains("/categories") && request.method == "DELETE" -> {
                createMockDeleteCategoryResponse()
            }
            // Bulk operations
            request.url.encodedPath.contains("/tasks/bulk") && request.method == "POST" -> {
                createMockBulkOperationResponse()
            }
            // Feedback endpoints
            request.url.encodedPath.contains("/feedback") && request.method == "POST" -> {
                createMockSubmitFeedbackResponse()
            }
            request.url.encodedPath.contains("/feedback") && request.method == "GET" && !request.url.encodedPath.contains("/analytics") -> {
                createMockGetFeedbackResponse()
            }
            request.url.encodedPath.contains("/feedback/analytics") && request.method == "GET" -> {
                createMockFeedbackAnalyticsResponse()
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
            .body(mockJson.toResponseBody("application/json".toMediaType()))
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
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockLogoutResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "message": "Logged out successfully"
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/auth/logout").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
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
                    },
                    {
                        "id": "task_124",
                        "title": "Review code changes",
                        "description": "Review pull request for new feature",
                        "priority": "medium",
                        "category": {
                            "id": "cat_1",
                            "name": "Work",
                            "color": "#FF5722",
                            "createdAt": "2024-01-15T10:30:00Z"
                        },
                        "dueDate": "2024-01-18T23:59:59Z",
                        "completed": true,
                        "createdAt": "2024-01-15T10:30:00Z",
                        "updatedAt": "2024-01-16T15:30:00Z"
                    }
                ],
                "pagination": {
                    "page": 1,
                    "limit": 20,
                    "total": 2,
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
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockCreateTaskResponse(request: Request): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "id": "task_${UUID.randomUUID().toString().substring(0, 8)}",
                "title": "New Task",
                "description": "Task description",
                "priority": "medium",
                "category": null,
                "dueDate": null,
                "completed": false,
                "createdAt": "2024-01-15T10:30:00Z",
                "updatedAt": "2024-01-15T10:30:00Z"
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(201)
            .message("Created")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockUpdateTaskResponse(request: Request): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "id": "task_123",
                "title": "Updated Task Title",
                "description": "Updated task description",
                "priority": "high",
                "category": {
                    "id": "cat_1",
                    "name": "Work",
                    "color": "#FF5722",
                    "createdAt": "2024-01-15T10:30:00Z"
                },
                "dueDate": "2024-01-25T23:59:59Z",
                "completed": false,
                "createdAt": "2024-01-15T10:30:00Z",
                "updatedAt": "2024-01-15T11:45:00Z"
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockDeleteTaskResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "message": "Task deleted successfully"
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/tasks/task_123").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockCompleteTaskResponse(request: Request): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockTaskAnalyticsResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/tasks/analytics").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockTaskSearchResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/tasks/search").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockCategoriesResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/categories").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockCreateCategoryResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "id": "cat_3",
                "name": "New Category",
                "color": "#2196F3",
                "createdAt": "2024-01-15T10:30:00Z"
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(201)
            .message("Created")
            .request(Request.Builder().url("https://api.todoapp.com/v1/categories").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockUpdateCategoryResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/categories/cat_1").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockDeleteCategoryResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "message": "Category deleted successfully"
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/categories/cat_1").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockBulkOperationResponse(): Response {
        val mockJson = """
        {
            "success": true,
            "data": {
                "updatedCount": 3,
                "message": "Successfully completed 3 tasks"
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/tasks/bulk").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockSubmitFeedbackResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(201)
            .message("Created")
            .request(Request.Builder().url("https://api.todoapp.com/v1/feedback").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockGetFeedbackResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/feedback").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockFeedbackAnalyticsResponse(): Response {
        val mockJson = """
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
        """.trimIndent()

        return Response.Builder()
            .code(200)
            .message("OK")
            .request(Request.Builder().url("https://api.todoapp.com/v1/feedback/analytics").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun createMockNotFoundResponse(): Response {
        val mockJson = """
        {
            "success": false,
            "error": {
                "code": "NOT_FOUND",
                "message": "Endpoint not found"
            }
        }
        """.trimIndent()

        return Response.Builder()
            .code(404)
            .message("Not Found")
            .request(Request.Builder().url("https://api.todoapp.com/v1/not-found").build())
            .protocol(Protocol.HTTP_1_1)
            .body(mockJson.toResponseBody("application/json".toMediaType()))
            .build()
    }
} 