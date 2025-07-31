package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AuthApiModelTest {

    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun `LoginRequest should have correct properties`() {
        val request = AuthApiModel.LoginRequest(
            phoneNumber = "+1234567890"
        )

        assertEquals("+1234567890", request.phoneNumber)
    }

    @Test
    fun `LoginRequest should serialize and deserialize correctly`() {
        val originalRequest = AuthApiModel.LoginRequest(
            phoneNumber = "+9876543210"
        )

        val adapter = moshi.adapter(AuthApiModel.LoginRequest::class.java)
        val json = adapter.toJson(originalRequest)
        val deserializedRequest = adapter.fromJson(json)

        assertEquals(originalRequest, deserializedRequest)
    }

    @Test
    fun `LoginRequest with different phone number formats should work`() {
        val request1 = AuthApiModel.LoginRequest("+1234567890")
        val request2 = AuthApiModel.LoginRequest("1234567890")
        val request3 = AuthApiModel.LoginRequest("+1-234-567-8900")

        assertEquals("+1234567890", request1.phoneNumber)
        assertEquals("1234567890", request2.phoneNumber)
        assertEquals("+1-234-567-8900", request3.phoneNumber)
    }

    @Test
    fun `LoginResponse should have correct properties`() {
        val response = AuthApiModel.LoginResponse(
            message = "OTP sent successfully",
            expiresIn = 300
        )

        assertEquals("OTP sent successfully", response.message)
        assertEquals(300, response.expiresIn)
    }

    @Test
    fun `LoginResponse should serialize and deserialize correctly`() {
        val originalResponse = AuthApiModel.LoginResponse(
            message = "Verification code sent to your phone",
            expiresIn = 600
        )

        val adapter = moshi.adapter(AuthApiModel.LoginResponse::class.java)
        val json = adapter.toJson(originalResponse)
        val deserializedResponse = adapter.fromJson(json)

        assertEquals(originalResponse, deserializedResponse)
    }

    @Test
    fun `LoginResponse with different expiration times should work`() {
        val response1 = AuthApiModel.LoginResponse("OTP sent", 60)
        val response2 = AuthApiModel.LoginResponse("Code sent", 300)
        val response3 = AuthApiModel.LoginResponse("Verification sent", 900)

        assertEquals(60, response1.expiresIn)
        assertEquals(300, response2.expiresIn)
        assertEquals(900, response3.expiresIn)
    }

    @Test
    fun `VerifyOtpRequest should have correct properties`() {
        val request = AuthApiModel.VerifyOtpRequest(
            phoneNumber = "+1234567890",
            otp = "123456"
        )

        assertEquals("+1234567890", request.phoneNumber)
        assertEquals("123456", request.otp)
    }

    @Test
    fun `VerifyOtpRequest should serialize and deserialize correctly`() {
        val originalRequest = AuthApiModel.VerifyOtpRequest(
            phoneNumber = "+9876543210",
            otp = "654321"
        )

        val adapter = moshi.adapter(AuthApiModel.VerifyOtpRequest::class.java)
        val json = adapter.toJson(originalRequest)
        val deserializedRequest = adapter.fromJson(json)

        assertEquals(originalRequest, deserializedRequest)
    }

    @Test
    fun `VerifyOtpRequest with different OTP formats should work`() {
        val request1 = AuthApiModel.VerifyOtpRequest("+1234567890", "123456")
        val request2 = AuthApiModel.VerifyOtpRequest("+1234567890", "1234")
        val request3 = AuthApiModel.VerifyOtpRequest("+1234567890", "12345678")

        assertEquals("123456", request1.otp)
        assertEquals("1234", request2.otp)
        assertEquals("12345678", request3.otp)
    }

    @Test
    fun `User should have correct properties`() {
        val user = AuthApiModel.User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        assertEquals("user_123", user.id)
        assertEquals("+1234567890", user.phoneNumber)
        assertEquals("John Doe", user.name)
    }

    @Test
    fun `User should serialize and deserialize correctly`() {
        val originalUser = AuthApiModel.User(
            id = "user_456",
            phoneNumber = "+9876543210",
            name = "Jane Smith"
        )

        val adapter = moshi.adapter(AuthApiModel.User::class.java)
        val json = adapter.toJson(originalUser)
        val deserializedUser = adapter.fromJson(json)

        assertEquals(originalUser, deserializedUser)
    }

    @Test
    fun `User with different name formats should work`() {
        val user1 = AuthApiModel.User("user_1", "+1234567890", "John Doe")
        val user2 = AuthApiModel.User("user_2", "+1234567890", "J. Doe")
        val user3 = AuthApiModel.User("user_3", "+1234567890", "John")

        assertEquals("John Doe", user1.name)
        assertEquals("J. Doe", user2.name)
        assertEquals("John", user3.name)
    }

    @Test
    fun `VerifyOtpResponse should have correct properties`() {
        val user = AuthApiModel.User(
            id = "user_123",
            phoneNumber = "+1234567890",
            name = "John Doe"
        )

        val response = AuthApiModel.VerifyOtpResponse(
            token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
            refreshToken = "refresh_token_123",
            expiresIn = 3600,
            user = user
        )

        assertEquals("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...", response.token)
        assertEquals("refresh_token_123", response.refreshToken)
        assertEquals(3600, response.expiresIn)
        assertEquals(user, response.user)
    }

    @Test
    fun `VerifyOtpResponse should serialize and deserialize correctly`() {
        val user = AuthApiModel.User(
            id = "user_456",
            phoneNumber = "+9876543210",
            name = "Jane Smith"
        )

        val originalResponse = AuthApiModel.VerifyOtpResponse(
            token = "jwt_token_456",
            refreshToken = "refresh_token_456",
            expiresIn = 7200,
            user = user
        )

        val adapter = moshi.adapter(AuthApiModel.VerifyOtpResponse::class.java)
        val json = adapter.toJson(originalResponse)
        val deserializedResponse = adapter.fromJson(json)

        assertEquals(originalResponse, deserializedResponse)
    }

    @Test
    fun `VerifyOtpResponse with different token formats should work`() {
        val user = AuthApiModel.User("user_1", "+1234567890", "Test User")

        val response1 = AuthApiModel.VerifyOtpResponse(
            "short_token",
            "short_refresh",
            1800,
            user
        )

        val response2 = AuthApiModel.VerifyOtpResponse(
            "very_long_jwt_token_with_many_characters_and_claims",
            "very_long_refresh_token_with_many_characters",
            86400,
            user
        )

        assertEquals("short_token", response1.token)
        assertEquals("very_long_jwt_token_with_many_characters_and_claims", response2.token)
        assertEquals(1800, response1.expiresIn)
        assertEquals(86400, response2.expiresIn)
    }

    @Test
    fun `LogoutResponse should have correct properties`() {
        val response = AuthApiModel.LogoutResponse(
            message = "Logged out successfully"
        )

        assertEquals("Logged out successfully", response.message)
    }

    @Test
    fun `LogoutResponse should serialize and deserialize correctly`() {
        val originalResponse = AuthApiModel.LogoutResponse(
            message = "You have been logged out"
        )

        val adapter = moshi.adapter(AuthApiModel.LogoutResponse::class.java)
        val json = adapter.toJson(originalResponse)
        val deserializedResponse = adapter.fromJson(json)

        assertEquals(originalResponse, deserializedResponse)
    }

    @Test
    fun `LogoutResponse with different messages should work`() {
        val response1 = AuthApiModel.LogoutResponse("Logged out")
        val response2 = AuthApiModel.LogoutResponse("Session ended")
        val response3 = AuthApiModel.LogoutResponse("You have been successfully logged out of your account")

        assertEquals("Logged out", response1.message)
        assertEquals("Session ended", response2.message)
        assertEquals("You have been successfully logged out of your account", response3.message)
    }

    @Test
    fun `LoginRequest equals and hashCode should work correctly`() {
        val request1 = AuthApiModel.LoginRequest("+1234567890")
        val request2 = AuthApiModel.LoginRequest("+1234567890")
        val request3 = AuthApiModel.LoginRequest("+9876543210")

        assertEquals(request1, request2)
        assertNotEquals(request1, request3)
        assertEquals(request1.hashCode(), request2.hashCode())
        assertNotEquals(request1.hashCode(), request3.hashCode())
    }

    @Test
    fun `User equals and hashCode should work correctly`() {
        val user1 = AuthApiModel.User("user_123", "+1234567890", "John Doe")
        val user2 = AuthApiModel.User("user_123", "+1234567890", "John Doe")
        val user3 = AuthApiModel.User("user_456", "+1234567890", "John Doe")

        assertEquals(user1, user2)
        assertNotEquals(user1, user3)
        assertEquals(user1.hashCode(), user2.hashCode())
        assertNotEquals(user1.hashCode(), user3.hashCode())
    }

    @Test
    fun `VerifyOtpRequest equals and hashCode should work correctly`() {
        val request1 = AuthApiModel.VerifyOtpRequest("+1234567890", "123456")
        val request2 = AuthApiModel.VerifyOtpRequest("+1234567890", "123456")
        val request3 = AuthApiModel.VerifyOtpRequest("+1234567890", "654321")

        assertEquals(request1, request2)
        assertNotEquals(request1, request3)
        assertEquals(request1.hashCode(), request2.hashCode())
        assertNotEquals(request1.hashCode(), request3.hashCode())
    }

    @Test
    fun `VerifyOtpResponse equals and hashCode should work correctly`() {
        val user = AuthApiModel.User("user_123", "+1234567890", "John Doe")
        
        val response1 = AuthApiModel.VerifyOtpResponse("token_123", "refresh_123", 3600, user)
        val response2 = AuthApiModel.VerifyOtpResponse("token_123", "refresh_123", 3600, user)
        val response3 = AuthApiModel.VerifyOtpResponse("token_456", "refresh_123", 3600, user)

        assertEquals(response1, response2)
        assertNotEquals(response1, response3)
        assertEquals(response1.hashCode(), response2.hashCode())
        assertNotEquals(response1.hashCode(), response3.hashCode())
    }

    @Test
    fun `LoginResponse equals and hashCode should work correctly`() {
        val response1 = AuthApiModel.LoginResponse("OTP sent", 300)
        val response2 = AuthApiModel.LoginResponse("OTP sent", 300)
        val response3 = AuthApiModel.LoginResponse("OTP sent", 600)

        assertEquals(response1, response2)
        assertNotEquals(response1, response3)
        assertEquals(response1.hashCode(), response2.hashCode())
        assertNotEquals(response1.hashCode(), response3.hashCode())
    }

    @Test
    fun `LogoutResponse equals and hashCode should work correctly`() {
        val response1 = AuthApiModel.LogoutResponse("Logged out")
        val response2 = AuthApiModel.LogoutResponse("Logged out")
        val response3 = AuthApiModel.LogoutResponse("Session ended")

        assertEquals(response1, response2)
        assertNotEquals(response1, response3)
        assertEquals(response1.hashCode(), response2.hashCode())
        assertNotEquals(response1.hashCode(), response3.hashCode())
    }
} 