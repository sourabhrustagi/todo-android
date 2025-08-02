package com.mobizonetech.todo.data.api

import com.mobizonetech.todo.data.api.models.ApiResponse
import com.mobizonetech.todo.data.api.models.AuthApiModel
import com.mobizonetech.todo.data.api.models.FeedbackApiModel
import com.mobizonetech.todo.data.api.models.TaskApiModel
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
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

        val responseType = Types.newParameterizedType(ApiResponse::class.java, AuthApiModel.LoginResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<AuthApiModel.LoginResponse>>(responseType).toJson(apiResponse)
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

        val responseType = Types.newParameterizedType(ApiResponse::class.java, AuthApiModel.VerifyOtpResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<AuthApiModel.VerifyOtpResponse>>(responseType).toJson(apiResponse)
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

        val responseType = Types.newParameterizedType(ApiResponse::class.java, AuthApiModel.LogoutResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<AuthApiModel.LogoutResponse>>(responseType).toJson(apiResponse)
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
        val taskListResponse = TaskApiModel.TaskListResponse(
            tasks = listOf(
                TaskApiModel.Task("task_1", "Test Task", "Test Description", "HIGH", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
            ),
            pagination = TaskApiModel.Pagination(1, 20, 1, 1)
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Tasks retrieved successfully",
            data = taskListResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.TaskListResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.TaskListResponse>>(responseType).toJson(apiResponse)
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
        val taskListResponse = TaskApiModel.TaskListResponse(
            tasks = listOf(
                TaskApiModel.Task("task_1", "Test Task", "Test Description", "HIGH", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
            ),
            pagination = TaskApiModel.Pagination(1, 20, 1, 1)
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Tasks retrieved successfully",
            data = taskListResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.TaskListResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.TaskListResponse>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTasks(page = 2, limit = 10, priority = "HIGH", category = "WORK")

        // Then
        assertTrue(result.success)
        assertEquals("Tasks retrieved successfully", result.message)
        assertEquals(taskListResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        val requestPath = mockWebServer.takeRequest().path!!
        assertTrue(requestPath.contains("/tasks"))
        assertTrue(requestPath.contains("page=2"))
        assertTrue(requestPath.contains("limit=10"))
        assertTrue(requestPath.contains("priority=HIGH"))
        assertTrue(requestPath.contains("category=WORK"))
    }

    @Test
    fun `getTask should make correct API call`() = runBlocking {
        // Given
        val task = TaskApiModel.Task("task_1", "Test Task", "Test Description", "HIGH", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Task retrieved successfully",
            data = task
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Task::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Task>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.getTask("task_1")

        // Then
        assertTrue(result.success)
        assertEquals("Task retrieved successfully", result.message)
        assertEquals(task, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_1"))
    }

    @Test
    fun `createTask should make correct API call`() = runBlocking {
        // Given
        val createRequest = TaskApiModel.CreateTaskRequest("New Task", "New Description", "HIGH", "WORK", "2023-12-31")
        val createdTask = TaskApiModel.Task("task_1", "New Task", "New Description", "HIGH", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Task created successfully",
            data = createdTask
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Task::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Task>>(responseType).toJson(apiResponse)
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
        val updateRequest = TaskApiModel.UpdateTaskRequest("Updated Task", "Updated Description", "MEDIUM", "PERSONAL", "2023-12-31")
        val updatedTask = TaskApiModel.Task("task_1", "Updated Task", "Updated Description", "MEDIUM", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Task updated successfully",
            data = updatedTask
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Task::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Task>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.updateTask("task_1", updateRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Task updated successfully", result.message)
        assertEquals(updatedTask, result.data)
        assertEquals("PUT", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_1"))
    }

    @Test
    fun `deleteTask should make correct API call`() = runBlocking {
        // Given
        val apiResponse = ApiResponse<Unit>(
            success = true,
            message = "Task deleted successfully"
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, Unit::class.java)
        val responseJson = moshi.adapter<ApiResponse<Unit>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.deleteTask("task_1")

        // Then
        assertTrue(result.success)
        assertEquals("Task deleted successfully", result.message)
        assertEquals("DELETE", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_1"))
    }

    @Test
    fun `completeTask should make correct API call`() = runBlocking {
        // Given
        val completedTask = TaskApiModel.Task("task_1", "Test Task", "Test Description", "HIGH", null, "2023-12-31", true, "2023-01-01", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Task completed successfully",
            data = completedTask
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Task::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Task>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.completeTask("task_1")

        // Then
        assertTrue(result.success)
        assertEquals("Task completed successfully", result.message)
        assertEquals(completedTask, result.data)
        assertEquals("PATCH", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/tasks/task_1/complete"))
    }

    @Test
    fun `getCategories should make correct API call`() = runBlocking {
        // Given
        val categories = listOf(
            TaskApiModel.Category("cat_1", "Work", "#FF0000", "2023-01-01"),
            TaskApiModel.Category("cat_2", "Personal", "#00FF00", "2023-01-01")
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Categories retrieved successfully",
            data = categories
        )

        val listType = Types.newParameterizedType(List::class.java, TaskApiModel.Category::class.java)
        val responseType = Types.newParameterizedType(ApiResponse::class.java, listType)
        val responseJson = moshi.adapter<ApiResponse<List<TaskApiModel.Category>>>(responseType).toJson(apiResponse)
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
        val createRequest = TaskApiModel.CreateCategoryRequest("New Category", "#0000FF")
        val createdCategory = TaskApiModel.Category("cat_1", "New Category", "#0000FF", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Category created successfully",
            data = createdCategory
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Category::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Category>>(responseType).toJson(apiResponse)
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
        val updateRequest = TaskApiModel.UpdateCategoryRequest("Updated Category", "#FFFF00")
        val updatedCategory = TaskApiModel.Category("cat_1", "Updated Category", "#FFFF00", "2023-01-01")
        val apiResponse = ApiResponse(
            success = true,
            message = "Category updated successfully",
            data = updatedCategory
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.Category::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.Category>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.updateCategory("cat_1", updateRequest)

        // Then
        assertTrue(result.success)
        assertEquals("Category updated successfully", result.message)
        assertEquals(updatedCategory, result.data)
        assertEquals("PUT", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories/cat_1"))
    }

    @Test
    fun `deleteCategory should make correct API call`() = runBlocking {
        // Given
        val apiResponse = ApiResponse<Unit>(
            success = true,
            message = "Category deleted successfully"
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, Unit::class.java)
        val responseJson = moshi.adapter<ApiResponse<Unit>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.deleteCategory("cat_1")

        // Then
        assertTrue(result.success)
        assertEquals("Category deleted successfully", result.message)
        assertEquals("DELETE", mockWebServer.takeRequest().method)
        assertTrue(mockWebServer.takeRequest().path!!.contains("/categories/cat_1"))
    }

    @Test
    fun `bulkOperation should make correct API call`() = runBlocking {
        // Given
        val bulkRequest = TaskApiModel.BulkOperationRequest(
            operation = "DELETE",
            taskIds = listOf("task_1", "task_2")
        )
        val bulkResponse = TaskApiModel.BulkOperationResponse(
            updatedCount = 2,
            message = "Successfully deleted 2 tasks"
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Bulk operation completed successfully",
            data = bulkResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.BulkOperationResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.BulkOperationResponse>>(responseType).toJson(apiResponse)
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
        val searchResponse = TaskApiModel.SearchResponse(
            tasks = listOf(
                TaskApiModel.Task("task_1", "Test Task", "Test Description", "HIGH", null, "2023-12-31", false, "2023-01-01", "2023-01-01")
            ),
            total = 1
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Search completed successfully",
            data = searchResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.SearchResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.SearchResponse>>(responseType).toJson(apiResponse)
        mockWebServer.enqueue(MockResponse().setBody(responseJson))

        // When
        val result = apiService.searchTasks("test")

        // Then
        assertTrue(result.success)
        assertEquals("Search completed successfully", result.message)
        assertEquals(searchResponse, result.data)
        assertEquals("GET", mockWebServer.takeRequest().method)
        val requestPath = mockWebServer.takeRequest().path!!
        assertTrue(requestPath.contains("/tasks/search"))
        assertTrue(requestPath.contains("q=test"))
    }

    @Test
    fun `getTaskAnalytics should make correct API call`() = runBlocking {
        // Given
        val analyticsResponse = TaskApiModel.AnalyticsResponse(
            total = 10,
            completed = 5,
            pending = 5,
            overdue = 2,
            byPriority = mapOf("HIGH" to 3, "MEDIUM" to 4, "LOW" to 3),
            byCategory = listOf(
                TaskApiModel.CategoryCount("WORK", 6),
                TaskApiModel.CategoryCount("PERSONAL", 4)
            ),
            completionRate = 50.0
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Analytics retrieved successfully",
            data = analyticsResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, TaskApiModel.AnalyticsResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<TaskApiModel.AnalyticsResponse>>(responseType).toJson(apiResponse)
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
        val feedbackRequest = FeedbackApiModel.SubmitFeedbackRequest(5, "Great app!", "general")
        val feedback = FeedbackApiModel.Feedback("feedback_1", 5, "Great app!", "general", "2023-01-01T00:00:00")
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback submitted successfully",
            data = feedback
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, FeedbackApiModel.Feedback::class.java)
        val responseJson = moshi.adapter<ApiResponse<FeedbackApiModel.Feedback>>(responseType).toJson(apiResponse)
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
            FeedbackApiModel.Feedback("feedback_1", 5, "Great app!", "general", "2023-01-01T00:00:00"),
            FeedbackApiModel.Feedback("feedback_2", 4, "Good app", "feature_request", "2023-01-02T00:00:00")
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback retrieved successfully",
            data = feedbackList
        )

        val listType = Types.newParameterizedType(List::class.java, FeedbackApiModel.Feedback::class.java)
        val responseType = Types.newParameterizedType(ApiResponse::class.java, listType)
        val responseJson = moshi.adapter<ApiResponse<List<FeedbackApiModel.Feedback>>>(responseType).toJson(apiResponse)
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
        val analyticsResponse = FeedbackApiModel.AnalyticsResponse(
            totalFeedback = 10,
            averageRating = 4.2,
            ratingDistribution = mapOf("5" to 4, "4" to 3, "3" to 2, "2" to 1, "1" to 0),
            categoryDistribution = mapOf("general" to 5, "feature_request" to 3, "bug_report" to 2)
        )
        val apiResponse = ApiResponse(
            success = true,
            message = "Feedback analytics retrieved successfully",
            data = analyticsResponse
        )

        val responseType = Types.newParameterizedType(ApiResponse::class.java, FeedbackApiModel.AnalyticsResponse::class.java)
        val responseJson = moshi.adapter<ApiResponse<FeedbackApiModel.AnalyticsResponse>>(responseType).toJson(apiResponse)
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