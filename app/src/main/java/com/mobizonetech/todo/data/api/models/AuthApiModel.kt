package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.JsonClass

object AuthApiModel {

    @JsonClass(generateAdapter = true)
    data class LoginRequest(
        val phoneNumber: String
    )

    @JsonClass(generateAdapter = true)
    data class LoginResponse(
        val message: String,
        val expiresIn: Int
    )

    @JsonClass(generateAdapter = true)
    data class VerifyOtpRequest(
        val phoneNumber: String,
        val otp: String
    )

    @JsonClass(generateAdapter = true)
    data class VerifyOtpResponse(
        val token: String,
        val refreshToken: String,
        val expiresIn: Int,
        val user: User
    )

    @JsonClass(generateAdapter = true)
    data class User(
        val id: String,
        val phoneNumber: String,
        val name: String
    )

    @JsonClass(generateAdapter = true)
    data class LogoutResponse(
        val message: String
    )
} 