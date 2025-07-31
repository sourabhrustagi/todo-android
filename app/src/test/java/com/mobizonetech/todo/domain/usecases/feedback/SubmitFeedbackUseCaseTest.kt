package com.mobizonetech.todo.domain.usecases.feedback

import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class SubmitFeedbackUseCaseTest {

    private lateinit var feedbackRepository: FeedbackRepository
    private lateinit var submitFeedbackUseCase: SubmitFeedbackUseCase

    @Before
    fun setUp() {
        feedbackRepository = mockk()
        submitFeedbackUseCase = SubmitFeedbackUseCase(feedbackRepository)
    }

    @Test
    fun `submitFeedback with valid data should return success`() = runTest {
        // Given
        val rating = 5
        val comment = "Great app!"
        val category = FeedbackCategory.GENERAL
        val expectedFeedback = Feedback(
            id = "feedback_1",
            rating = rating,
            comment = comment,
            category = category,
            createdAt = LocalDateTime.now()
        )

        coEvery {
            feedbackRepository.submitFeedback(rating, comment, category)
        } returns Result.success(expectedFeedback)

        // When
        val result = submitFeedbackUseCase(rating, comment, category)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedFeedback, result.getOrNull())
    }

    @Test
    fun `submitFeedback with rating 1 should return success`() = runTest {
        // Given
        val rating = 1
        val comment = "Needs improvement"
        val expectedFeedback = Feedback(
            id = "feedback_1",
            rating = rating,
            comment = comment,
            category = FeedbackCategory.GENERAL,
            createdAt = LocalDateTime.now()
        )

        coEvery {
            feedbackRepository.submitFeedback(rating, comment, FeedbackCategory.GENERAL)
        } returns Result.success(expectedFeedback)

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedFeedback, result.getOrNull())
    }

    @Test
    fun `submitFeedback with rating 0 should return failure`() = runTest {
        // Given
        val rating = 0
        val comment = "Test comment"

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Rating must be between 1 and 5", result.exceptionOrNull()?.message)
    }

    @Test
    fun `submitFeedback with rating 6 should return failure`() = runTest {
        // Given
        val rating = 6
        val comment = "Test comment"

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Rating must be between 1 and 5", result.exceptionOrNull()?.message)
    }

    @Test
    fun `submitFeedback with comment exceeding 1000 characters should return failure`() = runTest {
        // Given
        val rating = 5
        val comment = "a".repeat(1001)

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Comment cannot exceed 1000 characters", result.exceptionOrNull()?.message)
    }

    @Test
    fun `submitFeedback with comment exactly 1000 characters should return success`() = runTest {
        // Given
        val rating = 5
        val comment = "a".repeat(1000)
        val expectedFeedback = Feedback(
            id = "feedback_1",
            rating = rating,
            comment = comment,
            category = FeedbackCategory.GENERAL,
            createdAt = LocalDateTime.now()
        )

        coEvery {
            feedbackRepository.submitFeedback(rating, comment, FeedbackCategory.GENERAL)
        } returns Result.success(expectedFeedback)

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedFeedback, result.getOrNull())
    }

    @Test
    fun `submitFeedback should trim comment`() = runTest {
        // Given
        val rating = 5
        val comment = "  Test comment  "
        val expectedFeedback = Feedback(
            id = "feedback_1",
            rating = rating,
            comment = "Test comment",
            category = FeedbackCategory.GENERAL,
            createdAt = LocalDateTime.now()
        )

        coEvery {
            feedbackRepository.submitFeedback(rating, "Test comment", FeedbackCategory.GENERAL)
        } returns Result.success(expectedFeedback)

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedFeedback, result.getOrNull())
    }

    @Test
    fun `submitFeedback with null comment should pass null to repository`() = runTest {
        // Given
        val rating = 5
        val comment: String? = null
        val expectedFeedback = Feedback(
            id = "feedback_1",
            rating = rating,
            comment = null,
            category = FeedbackCategory.GENERAL,
            createdAt = LocalDateTime.now()
        )

        coEvery {
            feedbackRepository.submitFeedback(rating, null, FeedbackCategory.GENERAL)
        } returns Result.success(expectedFeedback)

        // When
        val result = submitFeedbackUseCase(rating, comment)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedFeedback, result.getOrNull())
    }

    @Test
    fun `submitFeedback with different categories should work correctly`() = runTest {
        // Given
        val rating = 4
        val comment = "Feature request"
        val categories = listOf(
            FeedbackCategory.FEATURE_REQUEST,
            FeedbackCategory.BUG_REPORT,
            FeedbackCategory.IMPROVEMENT
        )

        categories.forEach { category ->
            val expectedFeedback = Feedback(
                id = "feedback_1",
                rating = rating,
                comment = comment,
                category = category,
                createdAt = LocalDateTime.now()
            )

            coEvery {
                feedbackRepository.submitFeedback(rating, comment, category)
            } returns Result.success(expectedFeedback)

            // When
            val result = submitFeedbackUseCase(rating, comment, category)

            // Then
            assertTrue("Category $category should work", result.isSuccess)
            assertEquals(expectedFeedback, result.getOrNull())
        }
    }
} 