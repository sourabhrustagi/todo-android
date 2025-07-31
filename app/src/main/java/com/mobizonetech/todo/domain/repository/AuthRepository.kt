package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(phoneNumber: String): Result<String>
    
    suspend fun verifyOtp(phoneNumber: String, otp: String): Result<AuthResult>
    
    suspend fun logout(): Result<Unit>
    
    fun getCurrentUser(): Flow<User?>
    
    fun isLoggedIn(): Flow<Boolean>
    
    suspend fun saveAuthToken(token: String)
    
    suspend fun getAuthToken(): String?
    
    suspend fun clearAuthToken()
}

data class AuthResult(
    val token: String,
    val refreshToken: String,
    val expiresIn: Int,
    val user: User
) 