package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import kotlinx.coroutines.flow.Flow

interface FeedbackRepository {
    suspend fun submitFeedback(
        rating: Int,
        comment: String? = null,
        category: FeedbackCategory = FeedbackCategory.GENERAL
    ): Result<Feedback>
} 