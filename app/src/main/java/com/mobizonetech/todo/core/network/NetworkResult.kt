package com.mobizonetech.todo.core.network

import com.mobizonetech.todo.core.error.*
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

sealed class NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error(val exception: AppException) : NetworkResult<Nothing>()
    object Loading : NetworkResult<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun exceptionOrNull(): AppException? = when (this) {
        is Error -> exception
        else -> null
    }

    fun fold(
        onSuccess: (T) -> Unit,
        onError: (AppException) -> Unit,
        onLoading: () -> Unit = {}
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
            is Loading -> onLoading()
        }
    }
}

inline fun <T> runNetworkCatching(crossinline block: () -> T): NetworkResult<T> = try {
    NetworkResult.Success(block())
} catch (e: Throwable) {
    NetworkResult.Error(e.toAppException())
}

inline fun <T> runNetworkCatchingAsync(crossinline block: suspend () -> T): suspend () -> NetworkResult<T> = {
    try {
        NetworkResult.Success(block())
    } catch (e: Throwable) {
        NetworkResult.Error(e.toAppException())
    }
}

fun <T> Response<T>.toNetworkResult(): NetworkResult<T> {
    return if (isSuccessful) {
        body()?.let { NetworkResult.Success(it) } 
            ?: NetworkResult.Error(DataNotFoundException("Response body is null"))
    } else {
        NetworkResult.Error(when (code()) {
            401 -> UnauthorizedException("Authentication required")
            403 -> UnauthorizedException("Access forbidden")
            404 -> DataNotFoundException("Resource not found")
            422 -> ValidationException("Invalid request data")
            500 -> ServerException("Internal server error")
            502, 503, 504 -> ServerException("Server temporarily unavailable")
            else -> UnknownException("Network error: ${code()}")
        })
    }
}

fun Throwable.toAppException(): AppException {
    return when (this) {
        is AppException -> this
        is SocketTimeoutException -> TimeoutException(cause = this)
        is UnknownHostException -> NetworkException("No internet connection", this)
        is java.net.ConnectException -> NetworkException("Unable to connect to server", this)
        is javax.net.ssl.SSLException -> NetworkException("SSL/TLS error", this)
        else -> UnknownException(message ?: "Unknown error occurred", this)
    }
}

fun NetworkResult<*>.getUserFriendlyMessage(): String {
    return when (this) {
        is NetworkResult.Success -> "Success"
        is NetworkResult.Error -> when (exception) {
            is NetworkException -> "Please check your internet connection and try again"
            is TimeoutException -> "Request timed out. Please try again"
            is ServerException -> "Server error. Please try again later"
            is UnauthorizedException -> "Please log in again"
            is DataNotFoundException -> "Data not found"
            is ValidationException -> exception.message ?: "Invalid data provided"
            else -> "An error occurred. Please try again"
        }
        is NetworkResult.Loading -> "Loading..."
    }
} 