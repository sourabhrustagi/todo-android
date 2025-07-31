package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.models.AuthApiModel
import com.mobizonetech.todo.data.database.dao.UserDao
import com.mobizonetech.todo.data.database.entities.UserEntity
import com.mobizonetech.todo.domain.models.User
import com.mobizonetech.todo.domain.repository.AuthRepository
import com.mobizonetech.todo.domain.repository.AuthResult
import com.mobizonetech.todo.util.ApiLogger
import com.mobizonetech.todo.util.SecurePreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val userDao: UserDao,
    private val securePreferences: SecurePreferences
) : AuthRepository {

    override suspend fun login(phoneNumber: String): Result<String> {
        val apiName = "AUTH_LOGIN"
        val requestData = mapOf("phoneNumber" to phoneNumber)
        ApiLogger.logApiCall(apiName, requestData)

        return try {
            // Simulate API delay
            delay(2000)
            
            val response = apiService.login(AuthApiModel.LoginRequest(phoneNumber))
            if (response.success) {
                ApiLogger.logApiSuccess(apiName, "OTP sent successfully to $phoneNumber")
                Result.success("OTP sent successfully")
            } else {
                val error = Exception(response.message ?: "Login failed")
                ApiLogger.logApiError(apiName, error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }

    override suspend fun verifyOtp(phoneNumber: String, otp: String): Result<AuthResult> {
        val apiName = "AUTH_VERIFY_OTP"
        val requestData = mapOf("phoneNumber" to phoneNumber, "otp" to "***") // Mask OTP for security
        ApiLogger.logApiCall(apiName, requestData)

        return try {
            // Simulate API delay
            delay(2000)

            val response = apiService.verifyOtp(AuthApiModel.VerifyOtpRequest(phoneNumber, otp))
            if (response.success && response.data != null && response.data.token != null) {
                val user = User(
                    id = response.data.user.id,
                    phoneNumber = phoneNumber,
                    name = response.data.user.name
                )
                // Save user to local database
                val userEntity = UserEntity(
                    id = user.id,
                    phoneNumber = user.phoneNumber,
                    name = user.name,
                    createdAt = LocalDateTime.now(),
                    updatedAt = null
                )
                userDao.insertUser(userEntity)
                // Save auth token
                securePreferences.putString(SecurePreferences.KEY_AUTH_TOKEN, response.data.token)
                securePreferences.putString(SecurePreferences.KEY_USER_ID, user.id)
                securePreferences.putString(SecurePreferences.KEY_USER_NAME, user.name)
                securePreferences.putBoolean(SecurePreferences.KEY_IS_LOGGED_IN, true)
                ApiLogger.logApiSuccess(apiName, "User authenticated successfully: ${user.name}")
                Result.success(
                    AuthResult(
                        token = response.data.token,
                        refreshToken = response.data.refreshToken ?: "",
                        expiresIn = response.data.expiresIn ?: 0,
                        user = user
                    )
                )
            } else {
                val error = Exception(response.message ?: "OTP verification failed")
                ApiLogger.logApiError(apiName, error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }

    override suspend fun logout(): Result<Unit> {
        val apiName = "AUTH_LOGOUT"
        ApiLogger.logApiCall(apiName)

        return try {
            // Simulate API delay
            delay(2000)
            
            // Clear local data
            securePreferences.clear()
            
            ApiLogger.logApiSuccess(apiName, "User logged out successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }

    override fun getCurrentUser(): Flow<User?> = flow {
        val userId = securePreferences.getString(SecurePreferences.KEY_USER_ID)
        if (userId != null) {
            val userEntity = userDao.getUserById(userId)
            if (userEntity != null) {
                emit(User(
                    id = userEntity.id,
                    phoneNumber = userEntity.phoneNumber,
                    name = userEntity.name
                ))
            } else {
                emit(null)
            }
        } else {
            emit(null)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> = flow {
        emit(securePreferences.getBoolean(SecurePreferences.KEY_IS_LOGGED_IN))
    }

    override suspend fun saveAuthToken(token: String) {
        val apiName = "AUTH_SAVE_TOKEN"
        val requestData = mapOf("token" to "***") // Mask token for security
        ApiLogger.logApiCall(apiName, requestData)

        try {
            // Simulate API delay
            delay(2000)
            
            securePreferences.putString(SecurePreferences.KEY_AUTH_TOKEN, token)
            ApiLogger.logApiSuccess(apiName, "Auth token saved successfully")
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
        }
    }

    override suspend fun getAuthToken(): String? {
        val apiName = "AUTH_GET_TOKEN"
        ApiLogger.logApiCall(apiName)

        return try {
            // Simulate API delay
            delay(2000)
            
            val token = securePreferences.getString(SecurePreferences.KEY_AUTH_TOKEN)
            ApiLogger.logApiSuccess(apiName, "Auth token retrieved: ${if (token != null) "***" else "null"}")
            token
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            null
        }
    }

    override suspend fun clearAuthToken() {
        val apiName = "AUTH_CLEAR_TOKEN"
        ApiLogger.logApiCall(apiName)

        try {
            // Simulate API delay
            delay(2000)
            
            securePreferences.remove(SecurePreferences.KEY_AUTH_TOKEN)
            ApiLogger.logApiSuccess(apiName, "Auth token cleared successfully")
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
        }
    }
} 