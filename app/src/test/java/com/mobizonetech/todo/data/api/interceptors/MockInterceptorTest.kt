package com.mobizonetech.todo.data.api.interceptors

import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.net.HttpURLConnection

class MockInterceptorTest {

    private lateinit var mockInterceptor: MockInterceptor
    private lateinit var mockWebServer: MockWebServer

    @Before
    fun setUp() {
        mockInterceptor = MockInterceptor()
        mockWebServer = MockWebServer()
    }

    @Test
    fun `intercept should return login response for auth login endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/auth/login")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("OTP sent successfully") == true)
    }

    @Test
    fun `intercept should return verify OTP response for auth verify-otp endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/auth/verify-otp")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("mock_access_token_123") == true)
    }

    @Test
    fun `intercept should return logout response for auth logout endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/auth/logout")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Logged out successfully") == true)
    }

    @Test
    fun `intercept should return tasks response for GET tasks endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("Complete project documentation") == true)
        assertTrue(responseBody?.contains("Review code changes") == true)
    }

    @Test
    fun `intercept should return create task response for POST tasks endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(201, response.code)
        assertTrue(response.body?.string()?.contains("New Task") == true)
    }

    @Test
    fun `intercept should return update task response for PUT tasks endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123")
            .put(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Updated Task Title") == true)
    }

    @Test
    fun `intercept should return delete task response for DELETE tasks endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123")
            .delete()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Task deleted successfully") == true)
    }

    @Test
    fun `intercept should return complete task response for PATCH tasks endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123/complete")
            .patch(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("completed") == true)
    }

    @Test
    fun `intercept should return task analytics response for GET tasks analytics endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/analytics")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("total") == true)
        assertTrue(responseBody?.contains("completionRate") == true)
    }

    @Test
    fun `intercept should return task search response for GET tasks search endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/search?q=test")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Complete project documentation") == true)
    }

    @Test
    fun `intercept should return categories response for GET categories endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/categories")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("Work") == true)
        assertTrue(responseBody?.contains("Personal") == true)
    }

    @Test
    fun `intercept should return create category response for POST categories endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/categories")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(201, response.code)
        assertTrue(response.body?.string()?.contains("New Category") == true)
    }

    @Test
    fun `intercept should return update category response for PUT categories endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/categories/cat_1")
            .put(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Updated Work") == true)
    }

    @Test
    fun `intercept should return delete category response for DELETE categories endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/categories/cat_1")
            .delete()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("Category deleted successfully") == true)
    }

    @Test
    fun `intercept should return bulk operation response for POST tasks bulk endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/bulk")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("updatedCount") == true)
    }

    @Test
    fun `intercept should return submit feedback response for POST feedback endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/feedback")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(201, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("feedback_123") == true)
        assertTrue(responseBody?.contains("Great app!") == true)
    }

    @Test
    fun `intercept should return get feedback response for GET feedback endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/feedback")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("feedback_123") == true)
        assertTrue(responseBody?.contains("feedback_124") == true)
    }

    @Test
    fun `intercept should return feedback analytics response for GET feedback analytics endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/feedback/analytics")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        val responseBody = response.body?.string()
        assertTrue(responseBody?.contains("totalFeedback") == true)
        assertTrue(responseBody?.contains("averageRating") == true)
    }

    @Test
    fun `intercept should return not found response for unknown endpoint`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/unknown-endpoint")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(404, response.code)
        assertTrue(response.body?.string()?.contains("NOT_FOUND") == true)
    }

    @Test
    fun `intercept should handle different HTTP methods correctly`() {
        // Test GET method
        val getRequest = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks")
            .get()
            .build()
        val getResponse = mockInterceptor.intercept(okhttp3.Interceptor.Chain { getRequest })
        assertEquals(200, getResponse.code)

        // Test POST method
        val postRequest = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks")
            .post(okhttp3.RequestBody.create(null, "{}"))
            .build()
        val postResponse = mockInterceptor.intercept(okhttp3.Interceptor.Chain { postRequest })
        assertEquals(201, postResponse.code)

        // Test PUT method
        val putRequest = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123")
            .put(okhttp3.RequestBody.create(null, "{}"))
            .build()
        val putResponse = mockInterceptor.intercept(okhttp3.Interceptor.Chain { putRequest })
        assertEquals(200, putResponse.code)

        // Test DELETE method
        val deleteRequest = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123")
            .delete()
            .build()
        val deleteResponse = mockInterceptor.intercept(okhttp3.Interceptor.Chain { deleteRequest })
        assertEquals(200, deleteResponse.code)

        // Test PATCH method
        val patchRequest = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks/task_123/complete")
            .patch(okhttp3.RequestBody.create(null, "{}"))
            .build()
        val patchResponse = mockInterceptor.intercept(okhttp3.Interceptor.Chain { patchRequest })
        assertEquals(200, patchResponse.code)
    }

    @Test
    fun `intercept should return valid JSON responses`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals("application/json", response.body?.contentType()?.toString())
        val responseBody = response.body?.string()
        assertNotNull(responseBody)
        assertTrue(responseBody?.startsWith("{") == true)
        assertTrue(responseBody?.endsWith("}") == true)
    }

    @Test
    fun `intercept should handle request with query parameters`() {
        // Given
        val request = Request.Builder()
            .url("https://api.todoapp.com/v1/tasks?page=2&limit=10&priority=high")
            .get()
            .build()

        // When
        val response = mockInterceptor.intercept(okhttp3.Interceptor.Chain { request })

        // Then
        assertEquals(200, response.code)
        assertTrue(response.body?.string()?.contains("tasks") == true)
    }
} 