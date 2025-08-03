package com.mobizonetech.todo.core.result

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

    fun exceptionOrNull(): Throwable? = when (this) {
        is Error -> exception
        else -> null
    }

    fun fold(
        onSuccess: (T) -> Unit,
        onError: (Throwable) -> Unit,
        onLoading: () -> Unit = {}
    ) {
        when (this) {
            is Success -> onSuccess(data)
            is Error -> onError(exception)
            is Loading -> onLoading()
        }
    }

    fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> Error(exception)
        is Loading -> Loading
    }

    fun mapError(transform: (Throwable) -> Throwable): Result<T> = when (this) {
        is Success -> this
        is Error -> Error(transform(exception))
        is Loading -> Loading
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