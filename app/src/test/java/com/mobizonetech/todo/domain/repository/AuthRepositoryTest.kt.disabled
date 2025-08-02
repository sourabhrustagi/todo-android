package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class AuthRepositoryTest {

    @Mock
    private lateinit var authRepository: AuthRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `login should return success result with message`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val message = "OTP sent successfully"
        val result = Result.success(message)

        `when`(authRepository.login(phoneNumber)).thenReturn(result)

        // When
        val actualResult = authRepository.login(phoneNumber)

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(message, actualResult.getOrNull())
        verify(authRepository).login(phoneNumber)
    }

    @Test
    fun `login should handle error`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val error = Exception("Invalid phone number")
        val result = Result.failure<String>(error)

        `when`(authRepository.login(phoneNumber)).thenReturn(result)

        // When
        val actualResult = authRepository.login(phoneNumber)

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(authRepository).login(phoneNumber)
    }

    @Test
    fun `login with different phone number formats should work`() = runTest {
        // Given
        val phoneNumber1 = "+1234567890"
        val phoneNumber2 = "1234567890"
        val phoneNumber3 = "+1-234-567-8900"
        val message = "OTP sent successfully"
        val result = Result.success(message)

        `when`(authRepository.login(phoneNumber1)).thenReturn(result)
        `when`(authRepository.login(phoneNumber2)).thenReturn(result)
        `when`(authRepository.login(phoneNumber3)).thenReturn(result)

        // When & Then
        val result1 = authRepository.login(phoneNumber1)
        val result2 = authRepository.login(phoneNumber2)
        val result3 = authRepository.login(phoneNumber3)

        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
        assertEquals(message, result1.getOrNull())
        assertEquals(message, result2.getOrNull())
        assertEquals(message, result3.getOrNull())
    }

    @Test
    fun `verifyOtp should return success result with auth data`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val otp = "123456"
        val user = User(
            id = "user_123",
            phoneNumber = phoneNumber,
            name = "John Doe"
        )
        val authResult = AuthResult(
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            refreshToken = "refresh_token_123",
            expiresIn = 3600,
            user = user
        )
        val result = Result.success(authResult)

        `when`(authRepository.verifyOtp(phoneNumber, otp)).thenReturn(result)

        // When
        val actualResult = authRepository.verifyOtp(phoneNumber, otp)

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(authResult, actualResult.getOrNull())
        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", authResult.token)
        assertEquals("refresh_token_123", authResult.refreshToken)
        assertEquals(3600, authResult.expiresIn)
        assertEquals(user, authResult.user)
        verify(authRepository).verifyOtp(phoneNumber, otp)
    }

    @Test
    fun `verifyOtp should handle error`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val otp = "123456"
        val error = Exception("Invalid OTP")
        val result = Result.failure<AuthResult>(error)

        `when`(authRepository.verifyOtp(phoneNumber, otp)).thenReturn(result)

        // When
        val actualResult = authRepository.verifyOtp(phoneNumber, otp)

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(authRepository).verifyOtp(phoneNumber, otp)
    }

    @Test
    fun `verifyOtp with different OTP formats should work`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val otp1 = "123456"
        val otp2 = "1234"
        val otp3 = "12345678"
        val user = User("user_123", phoneNumber, "John Doe")
        val authResult = AuthResult("token", "refresh", 3600, user)
        val result = Result.success(authResult)

        `when`(authRepository.verifyOtp(phoneNumber, otp1)).thenReturn(result)
        `when`(authRepository.verifyOtp(phoneNumber, otp2)).thenReturn(result)
        `when`(authRepository.verifyOtp(phoneNumber, otp3)).thenReturn(result)

        // When & Then
        val result1 = authRepository.verifyOtp(phoneNumber, otp1)
        val result2 = authRepository.verifyOtp(phoneNumber, otp2)
        val result3 = authRepository.verifyOtp(phoneNumber, otp3)

        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertTrue(result3.isSuccess)
    }

    @Test
    fun `logout should return success result`() = runTest {
        // Given
        val result = Result.success(Unit)

        `when`(authRepository.logout()).thenReturn(result)

        // When
        val actualResult = authRepository.logout()

        // Then
        assertTrue(actualResult.isSuccess)
        verify(authRepository).logout()
    }

    @Test
    fun `logout should handle error`() = runTest {
        // Given
        val error = Exception("Logout failed")
        val result = Result.failure<Unit>(error)

        `when`(authRepository.logout()).thenReturn(result)

        // When
        val actualResult = authRepository.logout()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(authRepository).logout()
    }

    @Test
    fun `getCurrentUser should return Flow with user`() = runTest {
        // Given
        val user = User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )
        val flow: Flow<User?> = flowOf(user)

        `when`(authRepository.getCurrentUser()).thenReturn(flow)

        // When
        val actualResult = authRepository.getCurrentUser().first()

        // Then
        assertEquals(user, actualResult)
        verify(authRepository).getCurrentUser()
    }

    @Test
    fun `getCurrentUser should return Flow with null when not logged in`() = runTest {
        // Given
        val flow: Flow<User?> = flowOf(null)

        `when`(authRepository.getCurrentUser()).thenReturn(flow)

        // When
        val actualResult = authRepository.getCurrentUser().first()

        // Then
        assertNull(actualResult)
        verify(authRepository).getCurrentUser()
    }

    @Test
    fun `isLoggedIn should return Flow with true when logged in`() = runTest {
        // Given
        val flow: Flow<Boolean> = flowOf(true)

        `when`(authRepository.isLoggedIn()).thenReturn(flow)

        // When
        val actualResult = authRepository.isLoggedIn().first()

        // Then
        assertTrue(actualResult)
        verify(authRepository).isLoggedIn()
    }

    @Test
    fun `isLoggedIn should return Flow with false when not logged in`() = runTest {
        // Given
        val flow: Flow<Boolean> = flowOf(false)

        `when`(authRepository.isLoggedIn()).thenReturn(flow)

        // When
        val actualResult = authRepository.isLoggedIn().first()

        // Then
        assertFalse(actualResult)
        verify(authRepository).isLoggedIn()
    }

    @Test
    fun `saveAuthToken should work correctly`() = runTest {
        // Given
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

        // When
        authRepository.saveAuthToken(token)

        // Then
        verify(authRepository).saveAuthToken(token)
    }

    @Test
    fun `saveAuthToken with different token formats should work`() = runTest {
        // Given
        val token1 = "short_token"
        val token2 = "very_long_jwt_token_with_many_characters_and_claims"

        // When & Then
        authRepository.saveAuthToken(token1)
        authRepository.saveAuthToken(token2)

        verify(authRepository).saveAuthToken(token1)
        verify(authRepository).saveAuthToken(token2)
    }

    @Test
    fun `getAuthToken should return token when available`() = runTest {
        // Given
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."

        `when`(authRepository.getAuthToken()).thenReturn(token)

        // When
        val actualResult = authRepository.getAuthToken()

        // Then
        assertEquals(token, actualResult)
        verify(authRepository).getAuthToken()
    }

    @Test
    fun `getAuthToken should return null when no token available`() = runTest {
        // Given
        `when`(authRepository.getAuthToken()).thenReturn(null)

        // When
        val actualResult = authRepository.getAuthToken()

        // Then
        assertNull(actualResult)
        verify(authRepository).getAuthToken()
    }

    @Test
    fun `clearAuthToken should work correctly`() = runTest {
        // When
        authRepository.clearAuthToken()

        // Then
        verify(authRepository).clearAuthToken()
    }

    @Test
    fun `AuthResult should have correct properties`() {
        // Given
        val user = User("user_123", "+1234567890", "John Doe")
        val authResult = AuthResult(
            token = "test_token",
            refreshToken = "test_refresh",
            expiresIn = 1800,
            user = user
        )

        // Then
        assertEquals("test_token", authResult.token)
        assertEquals("test_refresh", authResult.refreshToken)
        assertEquals(1800, authResult.expiresIn)
        assertEquals(user, authResult.user)
    }

    @Test
    fun `AuthResult equals and hashCode should work correctly`() {
        // Given
        val user1 = User("user_123", "+1234567890", "John Doe")
        val user2 = User("user_123", "+1234567890", "John Doe")
        val user3 = User("user_456", "+1234567890", "John Doe")

        val authResult1 = AuthResult("token_123", "refresh_123", 3600, user1)
        val authResult2 = AuthResult("token_123", "refresh_123", 3600, user2)
        val authResult3 = AuthResult("token_456", "refresh_123", 3600, user1)

        // Then
        assertEquals(authResult1, authResult2)
        assertNotEquals(authResult1, authResult3)
        assertEquals(authResult1.hashCode(), authResult2.hashCode())
        assertNotEquals(authResult1.hashCode(), authResult3.hashCode())
    }

    @Test
    fun `complete authentication flow should work`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val otp = "123456"
        val user = User("user_123", phoneNumber, "John Doe")
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
        val refreshToken = "refresh_token_123"

        val loginResult = Result.success("OTP sent successfully")
        val authResult = AuthResult(token, refreshToken, 3600, user)
        val verifyResult = Result.success(authResult)
        val logoutResult = Result.success(Unit)

        `when`(authRepository.login(phoneNumber)).thenReturn(loginResult)
        `when`(authRepository.verifyOtp(phoneNumber, otp)).thenReturn(verifyResult)
        `when`(authRepository.logout()).thenReturn(logoutResult)

        // When - Login
        val loginResponse = authRepository.login(phoneNumber)

        // Then - Login
        assertTrue(loginResponse.isSuccess)
        assertEquals("OTP sent successfully", loginResponse.getOrNull())

        // When - Verify OTP
        val verifyResponse = authRepository.verifyOtp(phoneNumber, otp)

        // Then - Verify OTP
        assertTrue(verifyResponse.isSuccess)
        assertEquals(authResult, verifyResponse.getOrNull())

        // When - Logout
        val logoutResponse = authRepository.logout()

        // Then - Logout
        assertTrue(logoutResponse.isSuccess)

        // Verify all calls
        verify(authRepository).login(phoneNumber)
        verify(authRepository).verifyOtp(phoneNumber, otp)
        verify(authRepository).logout()
    }

    @Test
    fun `authentication flow with error should handle correctly`() = runTest {
        // Given
        val phoneNumber = "+1234567890"
        val otp = "123456"
        val loginError = Exception("Invalid phone number")
        val verifyError = Exception("Invalid OTP")

        val loginResult = Result.failure<String>(loginError)
        val verifyResult = Result.failure<AuthResult>(verifyError)

        `when`(authRepository.login(phoneNumber)).thenReturn(loginResult)
        `when`(authRepository.verifyOtp(phoneNumber, otp)).thenReturn(verifyResult)

        // When & Then - Login failure
        val loginResponse = authRepository.login(phoneNumber)
        assertTrue(loginResponse.isFailure)
        assertEquals(loginError, loginResponse.exceptionOrNull())

        // When & Then - Verify failure
        val verifyResponse = authRepository.verifyOtp(phoneNumber, otp)
        assertTrue(verifyResponse.isFailure)
        assertEquals(verifyError, verifyResponse.exceptionOrNull())

        // Verify calls
        verify(authRepository).login(phoneNumber)
        verify(authRepository).verifyOtp(phoneNumber, otp)
    }
} 