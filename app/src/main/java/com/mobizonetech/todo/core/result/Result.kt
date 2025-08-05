package com.mobizonetech.todo.core.result

/**
 * A sealed class that represents the result of an operation.
 * This is a more comprehensive version of the existing Result class.
 */
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object Loading : Result<Nothing>()

    fun isSuccess(): Boolean = this is Success
    fun isError(): Boolean = this is Error
    fun isLoading(): Boolean = this is Loading

    fun getOrNull(): T? = when (this) {
        is Success -> data
        else -> null
    }

    fun getOrThrow(): T = when (this) {
        is Success -> data
        is Error -> throw exception
        is Loading -> throw IllegalStateException("Result is still loading")
    }

    fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) {
            action(data)
        }
        return this
    }

    fun onError(action: (Throwable) -> Unit): Result<T> {
        if (this is Error) {
            action(exception)
        }
        return this
    }

    fun onLoading(action: () -> Unit): Result<T> {
        if (this is Loading) {
            action()
        }
        return this
    }

    fun <R> fold(
        onSuccess: (T) -> R,
        onError: (Throwable) -> R,
        onLoading: () -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(exception)
        is Loading -> onLoading()
    }

    companion object {
        fun <T> success(data: T): Result<T> = Success(data)
        fun <T> error(exception: Throwable): Result<T> = Error(exception)
        fun <T> loading(): Result<T> = Loading
    }
}

inline fun <T> runCatching(crossinline block: () -> T): Result<T> = try {
    Result.Success(block())
} catch (e: Throwable) {
    Result.Error(e)
}

inline fun <T> runCatchingAsync(crossinline block: suspend () -> T): suspend () -> Result<T> = {
    try {
        Result.Success(block())
    } catch (e: Throwable) {
        Result.Error(e)
    }
} 