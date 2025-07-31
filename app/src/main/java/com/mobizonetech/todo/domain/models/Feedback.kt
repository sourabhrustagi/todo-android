package com.mobizonetech.todo.domain.models

import java.time.LocalDateTime

data class Feedback(
    val id: String,
    val rating: Int,
    val comment: String? = null,
    val category: FeedbackCategory = FeedbackCategory.GENERAL,
    val createdAt: LocalDateTime
)

enum class FeedbackCategory {
    GENERAL, FEATURE_REQUEST, BUG_REPORT, IMPROVEMENT;

    fun getDisplayName(): String {
        return when (this) {
            GENERAL -> "General"
            FEATURE_REQUEST -> "Feature Request"
            BUG_REPORT -> "Bug Report"
            IMPROVEMENT -> "Improvement"
        }
    }
} 