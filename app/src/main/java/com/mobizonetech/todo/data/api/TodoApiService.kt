package com.mobizonetech.todo.data.api

import com.mobizonetech.todo.data.api.models.ApiResponse
import com.mobizonetech.todo.data.api.models.AuthApiModel
import com.mobizonetech.todo.data.api.models.FeedbackApiModel
import com.mobizonetech.todo.data.api.models.TaskApiModel
import retrofit2.http.*

interface TodoApiService {

    // Authentication endpoints
    @POST("auth/login")
    suspend fun login(@Body request: AuthApiModel.LoginRequest): ApiResponse<AuthApiModel.LoginResponse>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body request: AuthApiModel.VerifyOtpRequest): ApiResponse<AuthApiModel.VerifyOtpResponse>

    @POST("auth/logout")
    suspend fun logout(): ApiResponse<AuthApiModel.LogoutResponse>

    // Task endpoints
    @GET("tasks")
    suspend fun getTasks(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20,
        @Query("priority") priority: String? = null,
        @Query("category") category: String? = null,
        @Query("dueDate") dueDate: String? = null,
        @Query("completed") completed: Boolean? = null,
        @Query("search") search: String? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ApiResponse<TaskApiModel.TaskListResponse>

    @GET("tasks/{taskId}")
    suspend fun getTask(@Path("taskId") taskId: String): ApiResponse<TaskApiModel.Task>

    @POST("tasks")
    suspend fun createTask(@Body task: TaskApiModel.CreateTaskRequest): ApiResponse<TaskApiModel.Task>

    @PUT("tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: String,
        @Body task: TaskApiModel.UpdateTaskRequest
    ): ApiResponse<TaskApiModel.Task>

    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(@Path("taskId") taskId: String): ApiResponse<Unit>

    @PATCH("tasks/{taskId}/complete")
    suspend fun completeTask(@Path("taskId") taskId: String): ApiResponse<TaskApiModel.Task>

    // Category endpoints
    @GET("categories")
    suspend fun getCategories(): ApiResponse<List<TaskApiModel.Category>>

    @POST("categories")
    suspend fun createCategory(@Body category: TaskApiModel.CreateCategoryRequest): ApiResponse<TaskApiModel.Category>

    @PUT("categories/{categoryId}")
    suspend fun updateCategory(
        @Path("categoryId") categoryId: String,
        @Body category: TaskApiModel.UpdateCategoryRequest
    ): ApiResponse<TaskApiModel.Category>

    @DELETE("categories/{categoryId}")
    suspend fun deleteCategory(@Path("categoryId") categoryId: String): ApiResponse<Unit>

    // Bulk operations
    @POST("tasks/bulk")
    suspend fun bulkOperation(@Body request: TaskApiModel.BulkOperationRequest): ApiResponse<TaskApiModel.BulkOperationResponse>

    // Search and analytics
    @GET("tasks/search")
    suspend fun searchTasks(
        @Query("q") query: String,
        @Query("fields") fields: String? = null,
        @Query("fuzzy") fuzzy: Boolean? = null
    ): ApiResponse<TaskApiModel.SearchResponse>

    @GET("tasks/analytics")
    suspend fun getTaskAnalytics(): ApiResponse<TaskApiModel.AnalyticsResponse>

    // Feedback endpoints
    @POST("feedback")
    suspend fun submitFeedback(@Body feedback: FeedbackApiModel.SubmitFeedbackRequest): ApiResponse<FeedbackApiModel.Feedback>

    @GET("feedback")
    suspend fun getFeedback(): ApiResponse<List<FeedbackApiModel.Feedback>>

    @GET("feedback/analytics")
    suspend fun getFeedbackAnalytics(): ApiResponse<FeedbackApiModel.AnalyticsResponse>
} 