package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val message: String? = null,
    @Json(name = "error") val errorResponse: ErrorResponse? = null
)

@JsonClass(generateAdapter = true)
data class ErrorResponse(
    val code: String,
    val message: String,
    val details: Map<String, String>? = null
) 