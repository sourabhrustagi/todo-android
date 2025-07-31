package com.mobizonetech.todo.domain.usecases.feedback

import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import javax.inject.Inject

class SubmitFeedbackUseCase @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) {
    suspend operator fun invoke(
        rating: Int,
        comment: String? = null,
        category: FeedbackCategory = FeedbackCategory.GENERAL
    ): Result<Feedback> {
        if (rating !in 1..5) {
            return Result.failure(IllegalArgumentException("Rating must be between 1 and 5"))
        }
        
        if (comment != null && comment.length > 1000) {
            return Result.failure(IllegalArgumentException("Comment cannot exceed 1000 characters"))
        }
        
        return feedbackRepository.submitFeedback(
            rating = rating,
            comment = comment?.trim(),
            category = category
        )
    }
} 