package com.mobizonetech.todo.core.error

import android.content.Context
import com.mobizonetech.todo.R
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErrorHandler @Inject constructor(
    private val context: Context
) {
    
    fun handle(throwable: Throwable): String {
        return when (throwable) {
            is ValidationException -> handleValidationException(throwable)
            is NetworkException -> handleNetworkException(throwable)
            is DatabaseException -> handleDatabaseException(throwable)
            is AuthenticationException -> handleAuthenticationException(throwable)
            is UnauthorizedException -> handleUnauthorizedException(throwable)
            is TimeoutException -> handleTimeoutException(throwable)
            is ServerException -> handleServerException(throwable)
            else -> handleGenericException(throwable)
        }
    }
    
    fun isRetryable(throwable: Throwable): Boolean {
        return when (throwable) {
            is NetworkException -> true
            is TimeoutException -> true
            is ServerException -> true // Server exceptions are generally retryable
            is DatabaseException -> false
            is ValidationException -> false
            is AuthenticationException -> false
            is UnauthorizedException -> false
            else -> false
        }
    }
    
    fun getErrorTitle(throwable: Throwable): String {
        return when (throwable) {
            is ValidationException -> context.getString(R.string.error_validation_title)
            is NetworkException -> context.getString(R.string.error_network_title)
            is DatabaseException -> context.getString(R.string.error_database_title)
            is AuthenticationException -> context.getString(R.string.error_authentication_title)
            is UnauthorizedException -> context.getString(R.string.error_unauthorized_title)
            is TimeoutException -> context.getString(R.string.error_timeout_title)
            is ServerException -> context.getString(R.string.error_server_title)
            else -> context.getString(R.string.error_generic_title)
        }
    }
    
    private fun handleValidationException(exception: ValidationException): String {
        return if (exception.fieldErrors.isNotEmpty()) {
            exception.fieldErrors.values.joinToString(", ")
        } else {
            context.getString(R.string.error_validation_generic)
        }
    }
    
    private fun handleNetworkException(exception: NetworkException): String {
        return exception.message ?: context.getString(R.string.error_network_generic)
    }
    
    private fun handleDatabaseException(exception: DatabaseException): String {
        return exception.message ?: context.getString(R.string.error_database_generic)
    }
    
    private fun handleAuthenticationException(exception: AuthenticationException): String {
        return exception.message ?: context.getString(R.string.error_authentication_generic)
    }
    
    private fun handleUnauthorizedException(exception: UnauthorizedException): String {
        return exception.message ?: context.getString(R.string.error_unauthorized_generic)
    }
    
    private fun handleTimeoutException(exception: TimeoutException): String {
        return exception.message ?: context.getString(R.string.error_timeout_generic)
    }
    
    private fun handleServerException(exception: ServerException): String {
        return exception.message ?: context.getString(R.string.error_server_generic)
    }
    
    private fun handleGenericException(throwable: Throwable): String {
        return throwable.message ?: context.getString(R.string.error_generic_message)
    }
} 