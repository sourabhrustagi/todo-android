package com.mobizonetech.todo.core.error

sealed class AppException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)

// Network related exceptions
class NetworkException(
    message: String = "Network error occurred",
    cause: Throwable? = null
) : AppException(message, cause)

class TimeoutException(
    message: String = "Request timed out",
    cause: Throwable? = null
) : AppException(message, cause)

class ServerException(
    message: String = "Server error occurred",
    cause: Throwable? = null
) : AppException(message, cause)

// Authentication related exceptions
class AuthenticationException(
    message: String = "Authentication failed",
    cause: Throwable? = null
) : AppException(message, cause)

class UnauthorizedException(
    message: String = "Unauthorized access",
    cause: Throwable? = null
) : AppException(message, cause)

// Data related exceptions
class DataNotFoundException(
    message: String = "Data not found",
    cause: Throwable? = null
) : AppException(message, cause)

class ValidationException(
    message: String = "Validation failed",
    val fieldErrors: Map<String, String> = emptyMap(),
    cause: Throwable? = null
) : AppException(message, cause)

// Database related exceptions
class DatabaseException(
    message: String = "Database operation failed",
    cause: Throwable? = null
) : AppException(message, cause)

// Unknown exceptions
class UnknownException(
    message: String = "An unknown error occurred",
    cause: Throwable? = null
) : AppException(message, cause) 