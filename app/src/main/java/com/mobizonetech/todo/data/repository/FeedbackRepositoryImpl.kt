package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.models.FeedbackApiModel
import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import java.time.LocalDateTime
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService
) : FeedbackRepository {

    override suspend fun submitFeedback(
        rating: Int,
        comment: String?,
        category: FeedbackCategory
    ): Result<Feedback> {
        return try {
            val response = apiService.submitFeedback(
                FeedbackApiModel.SubmitFeedbackRequest(
                    rating = rating,
                    comment = comment,
                    category = category.name
                )
            )
            if (response.success && response.data != null) {
                val feedback = Feedback(
                    id = response.data.id,
                    rating = response.data.rating,
                    comment = response.data.comment,
                    category = FeedbackCategory.valueOf(response.data.category),
                    createdAt = LocalDateTime.parse(response.data.createdAt)
                )
                Result.success(feedback)
            } else {
                Result.failure(Exception(response.message ?: "Failed to submit feedback"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getFeedback(): Flow<Result<List<Feedback>>> {
        return flowOf(Result.success(emptyList())) // TODO: Implement actual API call
    }

    override suspend fun getFeedbackAnalytics(): Result<Map<String, Any>> {
        return try {
            val response = apiService.getFeedbackAnalytics()
            if (response.success && response.data != null) {
                val analytics = mapOf(
                    "averageRating" to response.data.averageRating,
                    "totalFeedback" to response.data.totalFeedback,
                    "categoryBreakdown" to mapOf<String, Any>() // TODO: Implement category breakdown
                )
                Result.success(analytics)
            } else {
                Result.failure(Exception(response.message ?: "Failed to get feedback analytics"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 