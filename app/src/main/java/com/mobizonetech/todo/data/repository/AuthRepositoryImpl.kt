package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.models.AuthApiModel
import com.mobizonetech.todo.domain.models.User
import com.mobizonetech.todo.domain.repository.AuthRepository
import com.mobizonetech.todo.domain.repository.AuthResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService
) : AuthRepository {

    override suspend fun login(phoneNumber: String): Result<String> {
        return try {
            val response = apiService.login(AuthApiModel.LoginRequest(phoneNumber))
            if (response.success) {
                Result.success("OTP sent successfully")
            } else {
                Result.failure(Exception(response.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<AuthResult> {
        return try {
            val response = apiService.verifyOtp(AuthApiModel.VerifyOtpRequest(phoneNumber, otp))
            if (response.success && response.data != null) {
                val authResult = AuthResult(
                    token = response.data.token,
                    refreshToken = response.data.refreshToken,
                    expiresIn = response.data.expiresIn,
                    user = User(
                        id = response.data.user.id,
                        name = response.data.user.name,
                        phoneNumber = response.data.user.phoneNumber
                    )
                )
                Result.success(authResult)
            } else {
                Result.failure(Exception(response.message ?: "OTP verification failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        return try {
            val response = apiService.logout()
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Logout failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> {
        // TODO: Implement local storage for current user
        return flowOf(null)
    }

    override fun isLoggedIn(): Flow<Boolean> {
        // TODO: Implement token validation
        return flowOf(false)
    }

    override suspend fun saveAuthToken(token: String) {
        // TODO: Implement secure token storage
    }

    override suspend fun getAuthToken(): String? {
        // TODO: Implement secure token retrieval
        return null
    }

    override suspend fun clearAuthToken() {
        // TODO: Implement secure token clearing
    }
} 