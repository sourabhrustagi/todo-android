package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.models.FeedbackApiModel
import com.mobizonetech.todo.data.database.dao.FeedbackDao
import com.mobizonetech.todo.data.database.entities.FeedbackEntity
import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import com.mobizonetech.todo.util.ApiLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FeedbackRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val feedbackDao: FeedbackDao
) : FeedbackRepository {

    override suspend fun submitFeedback(
        rating: Int,
        comment: String?,
        category: FeedbackCategory
    ): Result<Feedback> {
        val apiName = "SUBMIT_FEEDBACK"
        val requestData = mapOf("rating" to rating, "comment" to comment, "category" to category.name)
        ApiLogger.logApiCall(apiName, requestData)

        return try {
            // Simulate API delay
            delay(2000)
            
            val request = FeedbackApiModel.SubmitFeedbackRequest(
                rating = rating,
                comment = comment,
                category = category.name
            )
            val response = apiService.submitFeedback(request)

            if (response.success && response.data != null) {
                val feedback = Feedback(
                    id = response.data.id,
                    rating = response.data.rating,
                    comment = response.data.comment,
                    category = FeedbackCategory.valueOf(response.data.category.uppercase()),
                    createdAt = LocalDateTime.now()
                )

                // Save to local database
                val feedbackEntity = FeedbackEntity(
                    id = feedback.id,
                    rating = feedback.rating,
                    comment = feedback.comment,
                    category = feedback.category.name,
                    createdAt = feedback.createdAt
                )
                feedbackDao.insertFeedback(feedbackEntity)

                ApiLogger.logApiSuccess(apiName, "Feedback submitted successfully: Rating $rating, Category ${category.name}")
                Result.success(feedback)
            } else {
                val error = Exception(response.message ?: "Failed to submit feedback")
                ApiLogger.logApiError(apiName, error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }



    private fun FeedbackEntity.toDomain(): Feedback {
        return Feedback(
            id = id,
            rating = rating,
            comment = comment,
            category = FeedbackCategory.valueOf(category.uppercase()),
            createdAt = createdAt
        )
    }
} 