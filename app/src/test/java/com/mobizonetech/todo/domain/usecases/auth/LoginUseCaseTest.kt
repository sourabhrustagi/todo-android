package com.mobizonetech.todo.domain.usecases.auth

import com.mobizonetech.todo.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LoginUseCaseTest {

    private lateinit var authRepository: AuthRepository
    private lateinit var loginUseCase: LoginUseCase

    @Before
    fun setUp() {
        authRepository = mockk()
        loginUseCase = LoginUseCase(authRepository)
    }

    @Test
    fun `login with valid phone number should return success`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val expectedMessage = "OTP sent successfully"

        coEvery {
            authRepository.login(phoneNumber)
        } returns Result.success(expectedMessage)

        // When
        val result = loginUseCase(phoneNumber)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedMessage, result.getOrNull())
    }

    @Test
    fun `login with empty phone number should return failure`() = runTest {
        // Given
        val phoneNumber = ""

        // When
        val result = loginUseCase(phoneNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Phone number cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login with blank phone number should return failure`() = runTest {
        // Given
        val phoneNumber = "   "

        // When
        val result = loginUseCase(phoneNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Phone number cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login with invalid phone number format should return failure`() = runTest {
        // Given
        val phoneNumber = "123456" // Too short

        // When
        val result = loginUseCase(phoneNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid phone number format", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login with phone number containing letters should return failure`() = runTest {
        // Given
        val phoneNumber = "1234567890a" // Contains letters

        // When
        val result = loginUseCase(phoneNumber)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Invalid phone number format", result.exceptionOrNull()?.message)
    }

    @Test
    fun `login with valid phone numbers should pass validation`() = runTest {
        // Given
        val validPhoneNumbers = listOf(
            "1234567890",
            "44123456789",
            "61412345678",
            "919876543210"
        )

        validPhoneNumbers.forEach { phoneNumber ->
            coEvery {
                authRepository.login(phoneNumber)
            } returns Result.success("OTP sent successfully")

            // When
            val result = loginUseCase(phoneNumber)

            // Then
            assertTrue("Phone number $phoneNumber should be valid", result.isSuccess)
        }
    }

    @Test
    fun `login with invalid phone numbers should fail validation`() = runTest {
        // Given
        val invalidPhoneNumbers = listOf(
            "123456",          // Too short
            "12345678901234567890", // Too long
            "abc123",          // Contains letters
            "123456789a",      // Contains letters
            "1234567890+"      // Contains special characters
        )

        invalidPhoneNumbers.forEach { phoneNumber ->
            // When
            val result = loginUseCase(phoneNumber)

            // Then
            assertTrue("Phone number $phoneNumber should be invalid", result.isFailure)
            assertEquals("Invalid phone number format", result.exceptionOrNull()?.message)
        }
    }
} 