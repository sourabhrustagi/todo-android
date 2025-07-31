package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.JsonClass

object FeedbackApiModel {

    @JsonClass(generateAdapter = true)
    data class Feedback(
        val id: String,
        val rating: Int,
        val comment: String? = null,
        val category: String,
        val createdAt: String
    )

    @JsonClass(generateAdapter = true)
    data class SubmitFeedbackRequest(
        val rating: Int,
        val comment: String? = null,
        val category: String = "general"
    )

    @JsonClass(generateAdapter = true)
    data class AnalyticsResponse(
        val totalFeedback: Int,
        val averageRating: Double,
        val ratingDistribution: Map<String, Int>,
        val categoryDistribution: Map<String, Int>
    )
} 