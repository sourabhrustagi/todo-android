package com.mobizonetech.todo.data.api

import com.mobizonetech.todo.data.api.models.ApiResponse
import com.mobizonetech.todo.data.api.models.AuthApiModel
import com.mobizonetech.todo.data.api.models.FeedbackApiModel
import com.mobizonetech.todo.data.api.models.TaskApiModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class TodoApiServiceTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: TodoApiService
    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

        apiService = retrofit.create(TodoApiService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `login should make correct API call`() = runBlocking {
        // Given
        val loginRequest = AuthApiModel.LoginRequest("+1234567890")
        val loginResponse = AuthApiModel.LoginResponse("OTP sent successfully", 300)
        val apiResponse = ApiResponse(
            success = true,
            message = "Login successful",
            data = loginResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.login(loginRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Login successful", result.message)
        assertEquals(loginResponse, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/auth/login"))
    }

    @Test
    fun `verifyOtp should make correct API call`() = runBlocking {
        // Given
        val verifyRequest = AuthApiModel.VerifyOtpRequest("+1234567890", "123456")
        val user = AuthApiModel.User("user_123", "+1234567890", "John Doe")
        val verifyResponse = AuthApiModel.VerifyOtpResponse("token_123", "refresh_123", 3600, user)
        val apiResponse = ApiResponse(
            success = true,
            message = "OTP verified successfully",
            data = verifyResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.verifyOtp(verifyRequest)

        // Then
        assertTrue(result.success)
        assertEquals("OTP verified successfully", result.message)
        assertEquals(verifyResponse, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/auth/verify-otp"))
    }

    @Test
    fun `logout should make correct API call`() = runBlocking {
        // Given
        val logoutResponse = AuthApiModel.LogoutResponse("Logged out successfully")
        val apiResponse = ApiResponse(
            success = true,
            message = "Logout successful",
            data = logoutResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.logout()

        // Then
        assertTrue(result.success)
        assertEquals("Logout successful", result.message)
        assertEquals(logoutResponse, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/auth/logout"))
    }

    @Test
    fun `getTasks should make correct API call with default parameters`() = runBlocking {
        // Given
        val tasks = listOf(
            TaskApiModel.Task(
                id = "task_1",
                title = "Task 1",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            )
        )
        val pagination = TaskApiModel.Pagination(1, 20, 1, 1)
        val taskListResponse = TaskApiModel.TaskListResponse(tasks, pagination)
        val apiResponse = ApiResponse(
            success = true,
            message = "Tasks retrieved successfully",
            data = taskListResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTasks()

        // Then
        assertTrue(result.success)
        assertEquals("Tasks retrieved successfully", result.message)
        assertEquals(taskListResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks"))
    }

    @Test
    fun `getTasks should make correct API call with query parameters`() = runBlocking {
        // Given
        val tasks = listOf(
            TaskApiModel.Task(
                id = "task_1",
                title = "High priority task",
                priority = "high",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            )
        )
        val pagination = TaskApiModel.Pagination(1, 10, 1, 1)
        val taskListResponse = TaskApiModel.TaskListResponse(tasks, pagination)
        val apiResponse = ApiResponse(
            success = true,
            message = "Tasks retrieved successfully",
            data = taskListResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTasks(
            page = 2,
            limit = 10,
            priority = "high",
            category = "work",
            dueDate = "2024-01-20",
            completed = false,
            search = "important",
            sortBy = "dueDate",
            sortOrder = "asc"
        )

        // Then
        assertTrue(result.success)
        val request = mockWebServer.takeRequest()
        assertEquals("GET", request.method)
        assertTrue(request.path!!.contains("/tasks"))
        assertTrue(request.path!!.contains("page=2"))
        assertTrue(request.path!!.contains("limit=10"))
        assertTrue(request.path!!.contains("priority=high"))
        assertTrue(request.path!!.contains("category=work"))
        assertTrue(request.path!!.contains("dueDate=2024-01-20"))
        assertTrue(request.path!!.contains("completed=false"))
        assertTrue(request.path!!.contains("search=important"))
        assertTrue(request.path!!.contains("sortBy=dueDate"))
        assertTrue(request.path!!.contains("sortOrder=asc"))
    }

    @Test
    fun `getTask should make correct API call`() = runBlocking {
        // Given
        val task = TaskApiModel.Task(
            id = "task_123",
            title = "Specific task",
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Task retrieved successfully",
            data = task
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTask("task_123")

        // Then
        assertTrue(result.success)
        assertEquals("Task retrieved successfully", result.message)
        assertEquals(task, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_123"))
    }

    @Test
    fun `createTask should make correct API call`() = runBlocking {
        // Given
        val createRequest = TaskApiModel.CreateTaskRequest(
            title = "New task",
            description = "Task description",
            priority = "high",
            categoryId = "cat_123",
            dueDate = "2024-01-25T18:00:00Z"
        )
        val createdTask = TaskApiModel.Task(
            id = "task_456",
            title = "New task",
            description = "Task description",
            priority = "high",
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Task created successfully",
            data = createdTask
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.createTask(createRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Task created successfully", result.message)
        assertEquals(createdTask, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks"))
    }

    @Test
    fun `updateTask should make correct API call`() = runBlocking {
        // Given
        val updateRequest = TaskApiModel.UpdateTaskRequest(
            title = "Updated task",
            description = "Updated description",
            priority = "medium",
            completed = true
        )
        val updatedTask = TaskApiModel.Task(
            id = "task_123",
            title = "Updated task",
            description = "Updated description",
            priority = "medium",
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Task updated successfully",
            data = updatedTask
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.updateTask("task_123", updateRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Task updated successfully", result.message)
        assertEquals(updatedTask, result.data)
        assertEquals("PUT", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_123"))
    }

    @Test
    fun `deleteTask should make correct API call`() = runBlocking {
        // Given
        val apiResponse = ApiResponse<Unit>(
            success = true,
            message = "Task deleted successfully",
            data = null
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.deleteTask("task_123")

        // Then
        assertTrue(result.success)
        assertEquals("Task deleted successfully", result.message)
        assertNull(result.data)
        assertEquals("DELETE", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_123"))
    }

    @Test
    fun `completeTask should make correct API call`() = runBlocking {
        // Given
        val completedTask = TaskApiModel.Task(
            id = "task_123",
            title = "Completed task",
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Task completed successfully",
            data = completedTask
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.completeTask("task_123")

        // Then
        assertTrue(result.success)
        assertEquals("Task completed successfully", result.message)
        assertEquals(completedTask, result.data)
        assertEquals("PATCH", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_123/complete"))
    }

    @Test
    fun `getCategories should make correct API call`() = runBlocking {
        // Given
        val categories = listOf(
            TaskApiModel.Category("cat_1", "Work", "#FF5733", "2024-01-15T10:30:00Z"),
            TaskApiModel.Category("cat_2", "Personal", "#33FF57", "2024-01-15T10:30:00Z")
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Categories retrieved successfully",
            data = categories
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getCategories()

        // Then
        assertTrue(result.success)
        assertEquals("Categories retrieved successfully", result.message)
        assertEquals(categories, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories"))
    }

    @Test
    fun `createCategory should make correct API call`() = runBlocking {
        // Given
        val createRequest = TaskApiModel.CreateCategoryRequest("Shopping", "#FFC300")
        val createdCategory = TaskApiModel.Category("cat_3", "Shopping", "#FFC300", "2024-01-15T10:30:00Z")
        val apiResponse = ApiResponse(
            success = true,
            message = "Category created successfully",
            data = createdCategory
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.createCategory(createRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Category created successfully", result.message)
        assertEquals(createdCategory, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories"))
    }

    @Test
    fun `updateCategory should make correct API call`() = runBlocking {
        // Given
        val updateRequest = TaskApiModel.UpdateCategoryRequest("Updated Shopping", "#FFD700")
        val updatedCategory = TaskApiModel.Category("cat_3", "Updated Shopping", "#FFD700", "2024-01-15T10:30:00Z")
        val apiResponse = ApiResponse(
            success = true,
            message = "Category updated successfully",
            data = updatedCategory
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.updateCategory("cat_3", updateRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Category updated successfully", result.message)
        assertEquals(updatedCategory, result.data)
        assertEquals("PUT", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories/cat_3"))
    }

    @Test
    fun `deleteCategory should make correct API call`() = runBlocking {
        // Given
        val apiResponse = ApiResponse<Unit>(
            success = true,
            message = "Category deleted successfully",
            data = null
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.deleteCategory("cat_3")

        // Then
        assertTrue(result.success)
        assertEquals("Category deleted successfully", result.message)
        assertNull(result.data)
        assertEquals("DELETE", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories/cat_3"))
    }

    @Test
    fun `bulkOperation should make correct API call`() = runBlocking {
        // Given
        val bulkRequest = TaskApiModel.BulkOperationRequest(
            operation = "update_priority",
            taskIds = listOf("task_1", "task_2", "task_3"),
            priority = "high"
        )
        val bulkResponse = TaskApiModel.BulkOperationResponse(3, "Successfully updated 3 tasks")
        val apiResponse = ApiResponse(
            success = true,
            message = "Bulk operation completed successfully",
            data = bulkResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.bulkOperation(bulkRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Bulk operation completed successfully", result.message)
        assertEquals(bulkResponse, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/bulk"))
    }

    @Test
    fun `searchTasks should make correct API call`() = runBlocking {
        // Given
        val tasks = listOf(
            TaskApiModel.Task(
                id = "task_1",
                title = "Search result",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            )
        )
        val searchResponse = TaskApiModel.SearchResponse(tasks, 1)
        val apiResponse = ApiResponse(
            success = true,
            message = "Search completed successfully",
            data = searchResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.searchTasks("important", "title,description", true)

        // Then
        assertTrue(result.success)
        assertEquals("Search completed successfully", result.message)
        assertEquals(searchResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        val request = mockWebServer.takeRequest()
        assertTrue(request.path!!.contains("/tasks/search"))
        assertTrue(request.path!!.contains("q=important"))
        assertTrue(request.path!!.contains("fields=title,description"))
        assertTrue(request.path!!.contains("fuzzy=true"))
    }

    @Test
    fun `getTaskAnalytics should make correct API call`() = runBlocking {
        // Given
        val byPriority = mapOf("high" to 5, "medium" to 10, "low" to 3)
        val byCategory = listOf(
            TaskApiModel.CategoryCount("Work", 8),
            TaskApiModel.CategoryCount("Personal", 5)
        )
        val analyticsResponse = TaskApiModel.AnalyticsResponse(
            total = 18,
            completed = 12,
            pending = 6,
            overdue = 2,
            byPriority = byPriority,
            byCategory = byCategory,
            completionRate = 66.67
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Analytics retrieved successfully",
            data = analyticsResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTaskAnalytics()

        // Then
        assertTrue(result.success)
        assertEquals("Analytics retrieved successfully", result.message)
        assertEquals(analyticsResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/analytics"))
    }

    @Test
    fun `submitFeedback should make correct API call`() = runBlocking {
        // Given
        val feedbackRequest = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 5,
            comment = "Great app!",
            category = "general"
        )
        val feedback = FeedbackApiModel.Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = "general",
            createdAt = "2024-01-15T10:30:00Z"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback submitted successfully",
            data = feedback
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.submitFeedback(feedbackRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Feedback submitted successfully", result.message)
        assertEquals(feedback, result.data)
        assertEquals("POST", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/feedback"))
    }

    @Test
    fun `getFeedback should make correct API call`() = runBlocking {
        // Given
        val feedbackList = listOf(
            FeedbackApiModel.Feedback(
                id = "feedback_1",
                rating = 5,
                comment = "Great app!",
                category = "general",
                createdAt = "2024-01-15T10:30:00Z"
            ),
            FeedbackApiModel.Feedback(
                id = "feedback_2",
                rating = 4,
                comment = "Good app",
                category = "bug_report",
                createdAt = "2024-01-16T10:30:00Z"
            )
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback retrieved successfully",
            data = feedbackList
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getFeedback()

        // Then
        assertTrue(result.success)
        assertEquals("Feedback retrieved successfully", result.message)
        assertEquals(feedbackList, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/feedback"))
    }

    @Test
    fun `getFeedbackAnalytics should make correct API call`() = runBlocking {
        // Given
        val ratingDistribution = mapOf("1" to 2, "2" to 3, "3" to 5, "4" to 8, "5" to 12)
        val categoryDistribution = mapOf("general" to 15, "bug_report" to 8, "feature_request" to 7)
        val analyticsResponse = FeedbackApiModel.AnalyticsResponse(
            totalFeedback = 30,
            averageRating = 4.2,
            ratingDistribution = ratingDistribution,
            categoryDistribution = categoryDistribution
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback analytics retrieved successfully",
            data = analyticsResponse
        )

        val responseJson = moshi.adapter(ApiResponse::class.java).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getFeedbackAnalytics()

        // Then
        assertTrue(result.success)
        assertEquals("Feedback analytics retrieved successfully", result.message)
        assertEquals(analyticsResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/feedback/analytics"))
    }
} 