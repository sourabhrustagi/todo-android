package com.mobizonetech.todo.data.api.models

import org.junit.Assert.*
import org.junit.Test

class ApiResponseTest {

    @Test
    fun `ApiResponse with success should have correct properties`() {
        // Given
        val data = "test data"
        val message = "Success message"
        val apiResponse = ApiResponse(
            success = true,
            data = data,
            message = message,
            errorResponse = null
        )

        // When & Then
        assertTrue(apiResponse.success)
        assertEquals(data, apiResponse.data)
        assertEquals(message, apiResponse.message)
        assertNull(apiResponse.errorResponse)
    }

    @Test
    fun `ApiResponse with failure should have correct properties`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "ERROR_001",
            message = "Something went wrong",
            details = mapOf("field" to "error")
        )
        val apiResponse = ApiResponse(
            success = false,
            data = null,
            message = null,
            errorResponse = errorResponse
        )

        // When & Then
        assertFalse(apiResponse.success)
        assertNull(apiResponse.data)
        assertNull(apiResponse.message)
        assertEquals(errorResponse, apiResponse.errorResponse)
    }

    @Test
    fun `ApiResponse with default values should work correctly`() {
        // Given
        val apiResponse = ApiResponse<Unit>(success = true)

        // When & Then
        assertTrue(apiResponse.success)
        assertNull(apiResponse.data)
        assertNull(apiResponse.message)
        assertNull(apiResponse.errorResponse)
    }

    @Test
    fun `ApiResponse should be equal to itself`() {
        // Given
        val apiResponse = ApiResponse(
            success = true,
            data = "test",
            message = "success"
        )

        // When & Then
        assertEquals(apiResponse, apiResponse)
    }

    @Test
    fun `ApiResponses with same properties should be equal`() {
        // Given
        val apiResponse1 = ApiResponse(
            success = true,
            data = "test",
            message = "success"
        )
        val apiResponse2 = ApiResponse(
            success = true,
            data = "test",
            message = "success"
        )

        // When & Then
        assertEquals(apiResponse1, apiResponse2)
    }

    @Test
    fun `ApiResponses with different properties should not be equal`() {
        // Given
        val apiResponse1 = ApiResponse(
            success = true,
            data = "test1",
            message = "success1"
        )
        val apiResponse2 = ApiResponse(
            success = false,
            data = "test2",
            message = "success2"
        )

        // When & Then
        assertNotEquals(apiResponse1, apiResponse2)
    }

    @Test
    fun `ApiResponse should have correct hash code`() {
        // Given
        val apiResponse1 = ApiResponse(
            success = true,
            data = "test",
            message = "success"
        )
        val apiResponse2 = ApiResponse(
            success = true,
            data = "test",
            message = "success"
        )

        // When & Then
        assertEquals(apiResponse1.hashCode(), apiResponse2.hashCode())
    }

    @Test
    fun `ApiResponse should have meaningful string representation`() {
        // Given
        val apiResponse = ApiResponse(
            success = true,
            data = "test data",
            message = "success message"
        )

        // When
        val stringRepresentation = apiResponse.toString()

        // Then
        assertTrue(stringRepresentation.contains("true"))
        assertTrue(stringRepresentation.contains("test data"))
        assertTrue(stringRepresentation.contains("success message"))
    }

    @Test
    fun `ErrorResponse should have all required properties`() {
        // Given
        val details = mapOf("field" to "error", "code" to "400")
        val errorResponse = ErrorResponse(
            code = "VALIDATION_ERROR",
            message = "Validation failed",
            details = details
        )

        // When & Then
        assertEquals("VALIDATION_ERROR", errorResponse.code)
        assertEquals("Validation failed", errorResponse.message)
        assertEquals(details, errorResponse.details)
    }

    @Test
    fun `ErrorResponse should handle null details`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "ERROR_001",
            message = "Something went wrong",
            details = null
        )

        // When & Then
        assertEquals("ERROR_001", errorResponse.code)
        assertEquals("Something went wrong", errorResponse.message)
        assertNull(errorResponse.details)
    }

    @Test
    fun `ErrorResponse should be equal to itself`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "ERROR_001",
            message = "Error message"
        )

        // When & Then
        assertEquals(errorResponse, errorResponse)
    }

    @Test
    fun `ErrorResponses with same properties should be equal`() {
        // Given
        val errorResponse1 = ErrorResponse(
            code = "ERROR_001",
            message = "Error message",
            details = mapOf("field" to "error")
        )
        val errorResponse2 = ErrorResponse(
            code = "ERROR_001",
            message = "Error message",
            details = mapOf("field" to "error")
        )

        // When & Then
        assertEquals(errorResponse1, errorResponse2)
    }

    @Test
    fun `ErrorResponses with different properties should not be equal`() {
        // Given
        val errorResponse1 = ErrorResponse(
            code = "ERROR_001",
            message = "Error message 1"
        )
        val errorResponse2 = ErrorResponse(
            code = "ERROR_002",
            message = "Error message 2"
        )

        // When & Then
        assertNotEquals(errorResponse1, errorResponse2)
    }

    @Test
    fun `ErrorResponse should have correct hash code`() {
        // Given
        val errorResponse1 = ErrorResponse(
            code = "ERROR_001",
            message = "Error message"
        )
        val errorResponse2 = ErrorResponse(
            code = "ERROR_001",
            message = "Error message"
        )

        // When & Then
        assertEquals(errorResponse1.hashCode(), errorResponse2.hashCode())
    }

    @Test
    fun `ErrorResponse should have meaningful string representation`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "VALIDATION_ERROR",
            message = "Validation failed",
            details = mapOf("field" to "error")
        )

        // When
        val stringRepresentation = errorResponse.toString()

        // Then
        assertTrue(stringRepresentation.contains("VALIDATION_ERROR"))
        assertTrue(stringRepresentation.contains("Validation failed"))
    }

    @Test
    fun `ErrorResponse should handle empty details map`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "ERROR_001",
            message = "Error message",
            details = emptyMap()
        )

        // When & Then
        assertNotNull(errorResponse.details)
        assertTrue(errorResponse.details!!.isEmpty())
    }

    @Test
    fun `ErrorResponse should handle special characters in code and message`() {
        // Given
        val errorResponse = ErrorResponse(
            code = "ERROR_001_SPECIAL@#$%",
            message = "Error with special chars: @#$%^&*()",
            details = null
        )

        // When & Then
        assertEquals("ERROR_001_SPECIAL@#$%", errorResponse.code)
        assertEquals("Error with special chars: @#$%^&*()", errorResponse.message)
    }
} 