package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.Feedback
import com.mobizonetech.todo.domain.models.FeedbackCategory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class FeedbackRepositoryTest {

    @Mock
    private lateinit var feedbackRepository: FeedbackRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `submitFeedback should return success result with feedback`() = runTest {
        // Given
        val rating = 5
        val comment = "Great app! Very user-friendly interface."
        val category = FeedbackCategory.GENERAL
        val feedback = Feedback(
            id = "feedback_123",
            rating = rating,
            comment = comment,
            category = category,
            createdAt = "2024-01-15T10:30:00Z"
        )
        val result = Result.success(feedback)

        `when`(feedbackRepository.submitFeedback(rating, comment, category)).thenReturn(result)

        // When
        val actualResult = feedbackRepository.submitFeedback(rating, comment, category)

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(feedback, actualResult.getOrNull())
        assertEquals(rating, feedback.rating)
        assertEquals(comment, feedback.comment)
        assertEquals(category, feedback.category)
        verify(feedbackRepository).submitFeedback(rating, comment, category)
    }

    @Test
    fun `submitFeedback with minimal parameters should work`() = runTest {
        // Given
        val rating = 4
        val feedback = Feedback(
            id = "feedback_456",
            rating = rating,
            comment = null,
            category = FeedbackCategory.GENERAL,
            createdAt = "2024-01-15T10:30:00Z"
        )
        val result = Result.success(feedback)

        `when`(feedbackRepository.submitFeedback(rating)).thenReturn(result)

        // When
        val actualResult = feedbackRepository.submitFeedback(rating)

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(feedback, actualResult.getOrNull())
        assertEquals(rating, feedback.rating)
        assertNull(feedback.comment)
        assertEquals(FeedbackCategory.GENERAL, feedback.category)
        verify(feedbackRepository).submitFeedback(rating)
    }

    @Test
    fun `submitFeedback with different categories should work`() = runTest {
        // Given
        val rating = 3
        val comment = "Found a bug in the task creation"
        val bugCategory = FeedbackCategory.BUG_REPORT
        val featureCategory = FeedbackCategory.FEATURE_REQUEST

        val bugFeedback = Feedback(
            id = "feedback_789",
            rating = rating,
            comment = comment,
            category = bugCategory,
            createdAt = "2024-01-15T10:30:00Z"
        )
        val featureFeedback = Feedback(
            id = "feedback_101",
            rating = rating,
            comment = comment,
            category = featureCategory,
            createdAt = "2024-01-15T10:30:00Z"
        )

        val bugResult = Result.success(bugFeedback)
        val featureResult = Result.success(featureFeedback)

        `when`(feedbackRepository.submitFeedback(rating, comment, bugCategory)).thenReturn(bugResult)
        `when`(feedbackRepository.submitFeedback(rating, comment, featureCategory)).thenReturn(featureResult)

        // When & Then
        val bugResponse = feedbackRepository.submitFeedback(rating, comment, bugCategory)
        val featureResponse = feedbackRepository.submitFeedback(rating, comment, featureCategory)

        assertTrue(bugResponse.isSuccess)
        assertTrue(featureResponse.isSuccess)
        assertEquals(bugCategory, bugResponse.getOrNull()?.category)
        assertEquals(featureCategory, featureResponse.getOrNull()?.category)
    }

    @Test
    fun `submitFeedback with different ratings should work`() = runTest {
        // Given
        val ratings = listOf(1, 2, 3, 4, 5)
        val comment = "Test feedback"
        val category = FeedbackCategory.GENERAL

        ratings.forEach { rating ->
            val feedback = Feedback(
                id = "feedback_$rating",
                rating = rating,
                comment = comment,
                category = category,
                createdAt = "2024-01-15T10:30:00Z"
            )
            val result = Result.success(feedback)

            `when`(feedbackRepository.submitFeedback(rating, comment, category)).thenReturn(result)

            // When
            val actualResult = feedbackRepository.submitFeedback(rating, comment, category)

            // Then
            assertTrue(actualResult.isSuccess)
            assertEquals(rating, actualResult.getOrNull()?.rating)
        }
    }

    @Test
    fun `submitFeedback should handle error`() = runTest {
        // Given
        val rating = 5
        val comment = "Great app!"
        val category = FeedbackCategory.GENERAL
        val error = Exception("Failed to submit feedback")
        val result = Result.failure<Feedback>(error)

        `when`(feedbackRepository.submitFeedback(rating, comment, category)).thenReturn(result)

        // When
        val actualResult = feedbackRepository.submitFeedback(rating, comment, category)

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(feedbackRepository).submitFeedback(rating, comment, category)
    }

    @Test
    fun `getFeedback should return Flow with feedback list`() = runTest {
        // Given
        val feedbackList = listOf(
            Feedback(
                id = "feedback_1",
                rating = 5,
                comment = "Excellent app!",
                category = FeedbackCategory.GENERAL,
                createdAt = "2024-01-15T10:30:00Z"
            ),
            Feedback(
                id = "feedback_2",
                rating = 4,
                comment = "Good app, but needs some improvements",
                category = FeedbackCategory.FEATURE_REQUEST,
                createdAt = "2024-01-16T10:30:00Z"
            ),
            Feedback(
                id = "feedback_3",
                rating = 2,
                comment = "Found a bug in task deletion",
                category = FeedbackCategory.BUG_REPORT,
                createdAt = "2024-01-17T10:30:00Z"
            )
        )
        val result = Result.success(feedbackList)
        val flow: Flow<Result<List<Feedback>>> = flowOf(result)

        `when`(feedbackRepository.getFeedback()).thenReturn(flow)

        // When
        val actualResult = feedbackRepository.getFeedback().first()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(feedbackList, actualResult.getOrNull())
        assertEquals(3, actualResult.getOrNull()?.size)
        verify(feedbackRepository).getFeedback()
    }

    @Test
    fun `getFeedback should return Flow with empty list`() = runTest {
        // Given
        val emptyList = emptyList<Feedback>()
        val result = Result.success(emptyList)
        val flow: Flow<Result<List<Feedback>>> = flowOf(result)

        `when`(feedbackRepository.getFeedback()).thenReturn(flow)

        // When
        val actualResult = feedbackRepository.getFeedback().first()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(emptyList, actualResult.getOrNull())
        assertTrue(actualResult.getOrNull()?.isEmpty() == true)
        verify(feedbackRepository).getFeedback()
    }

    @Test
    fun `getFeedback should handle error`() = runTest {
        // Given
        val error = Exception("Failed to fetch feedback")
        val result = Result.failure<List<Feedback>>(error)
        val flow: Flow<Result<List<Feedback>>> = flowOf(result)

        `when`(feedbackRepository.getFeedback()).thenReturn(flow)

        // When
        val actualResult = feedbackRepository.getFeedback().first()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(feedbackRepository).getFeedback()
    }

    @Test
    fun `getFeedbackAnalytics should return success result`() = runTest {
        // Given
        val analytics = mapOf(
            "totalFeedback" to 25,
            "averageRating" to 4.2,
            "ratingDistribution" to mapOf(
                "1" to 2,
                "2" to 3,
                "3" to 5,
                "4" to 8,
                "5" to 7
            ),
            "categoryDistribution" to mapOf(
                "general" to 12,
                "bug_report" to 6,
                "feature_request" to 7
            ),
            "completionRate" to 84.0
        )
        val result = Result.success(analytics)

        `when`(feedbackRepository.getFeedbackAnalytics()).thenReturn(result)

        // When
        val actualResult = feedbackRepository.getFeedbackAnalytics()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(analytics, actualResult.getOrNull())
        assertEquals(25, analytics["totalFeedback"])
        assertEquals(4.2, analytics["averageRating"])
        assertEquals(84.0, analytics["completionRate"])
        verify(feedbackRepository).getFeedbackAnalytics()
    }

    @Test
    fun `getFeedbackAnalytics should handle error`() = runTest {
        // Given
        val error = Exception("Failed to fetch analytics")
        val result = Result.failure<Map<String, Any>>(error)

        `when`(feedbackRepository.getFeedbackAnalytics()).thenReturn(result)

        // When
        val actualResult = feedbackRepository.getFeedbackAnalytics()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(feedbackRepository).getFeedbackAnalytics()
    }

    @Test
    fun `getFeedbackAnalytics with empty data should work`() = runTest {
        // Given
        val emptyAnalytics = mapOf(
            "totalFeedback" to 0,
            "averageRating" to 0.0,
            "ratingDistribution" to emptyMap<String, Int>(),
            "categoryDistribution" to emptyMap<String, Int>(),
            "completionRate" to 0.0
        )
        val result = Result.success(emptyAnalytics)

        `when`(feedbackRepository.getFeedbackAnalytics()).thenReturn(result)

        // When
        val actualResult = feedbackRepository.getFeedbackAnalytics()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(emptyAnalytics, actualResult.getOrNull())
        assertEquals(0, emptyAnalytics["totalFeedback"])
        assertEquals(0.0, emptyAnalytics["averageRating"])
        assertEquals(0.0, emptyAnalytics["completionRate"])
        verify(feedbackRepository).getFeedbackAnalytics()
    }

    @Test
    fun `complete feedback flow should work`() = runTest {
        // Given
        val rating = 5
        val comment = "Amazing app! Love the interface."
        val category = FeedbackCategory.GENERAL
        val feedback = Feedback(
            id = "feedback_123",
            rating = rating,
            comment = comment,
            category = category,
            createdAt = "2024-01-15T10:30:00Z"
        )
        val feedbackList = listOf(feedback)
        val analytics = mapOf(
            "totalFeedback" to 1,
            "averageRating" to 5.0,
            "completionRate" to 100.0
        )

        val submitResult = Result.success(feedback)
        val getFeedbackResult = Result.success(feedbackList)
        val analyticsResult = Result.success(analytics)

        `when`(feedbackRepository.submitFeedback(rating, comment, category)).thenReturn(submitResult)
        `when`(feedbackRepository.getFeedback()).thenReturn(flowOf(getFeedbackResult))
        `when`(feedbackRepository.getFeedbackAnalytics()).thenReturn(analyticsResult)

        // When - Submit feedback
        val submitResponse = feedbackRepository.submitFeedback(rating, comment, category)

        // Then - Submit feedback
        assertTrue(submitResponse.isSuccess)
        assertEquals(feedback, submitResponse.getOrNull())

        // When - Get feedback list
        val getFeedbackResponse = feedbackRepository.getFeedback().first()

        // Then - Get feedback list
        assertTrue(getFeedbackResponse.isSuccess)
        assertEquals(feedbackList, getFeedbackResponse.getOrNull())

        // When - Get analytics
        val analyticsResponse = feedbackRepository.getFeedbackAnalytics()

        // Then - Get analytics
        assertTrue(analyticsResponse.isSuccess)
        assertEquals(analytics, analyticsResponse.getOrNull())

        // Verify all calls
        verify(feedbackRepository).submitFeedback(rating, comment, category)
        verify(feedbackRepository).getFeedback()
        verify(feedbackRepository).getFeedbackAnalytics()
    }

    @Test
    fun `feedback flow with errors should handle correctly`() = runTest {
        // Given
        val rating = 5
        val comment = "Great app!"
        val category = FeedbackCategory.GENERAL
        val submitError = Exception("Failed to submit feedback")
        val getFeedbackError = Exception("Failed to get feedback")
        val analyticsError = Exception("Failed to get analytics")

        val submitResult = Result.failure<Feedback>(submitError)
        val getFeedbackResult = Result.failure<List<Feedback>>(getFeedbackError)
        val analyticsResult = Result.failure<Map<String, Any>>(analyticsError)

        `when`(feedbackRepository.submitFeedback(rating, comment, category)).thenReturn(submitResult)
        `when`(feedbackRepository.getFeedback()).thenReturn(flowOf(getFeedbackResult))
        `when`(feedbackRepository.getFeedbackAnalytics()).thenReturn(analyticsResult)

        // When & Then - Submit feedback failure
        val submitResponse = feedbackRepository.submitFeedback(rating, comment, category)
        assertTrue(submitResponse.isFailure)
        assertEquals(submitError, submitResponse.exceptionOrNull())

        // When & Then - Get feedback failure
        val getFeedbackResponse = feedbackRepository.getFeedback().first()
        assertTrue(getFeedbackResponse.isFailure)
        assertEquals(getFeedbackError, getFeedbackResponse.exceptionOrNull())

        // When & Then - Get analytics failure
        val analyticsResponse = feedbackRepository.getFeedbackAnalytics()
        assertTrue(analyticsResponse.isFailure)
        assertEquals(analyticsError, analyticsResponse.exceptionOrNull())

        // Verify all calls
        verify(feedbackRepository).submitFeedback(rating, comment, category)
        verify(feedbackRepository).getFeedback()
        verify(feedbackRepository).getFeedbackAnalytics()
    }

    @Test
    fun `FeedbackCategory enum should have correct values`() {
        // Then
        assertEquals("general", FeedbackCategory.GENERAL.value)
        assertEquals("bug_report", FeedbackCategory.BUG_REPORT.value)
        assertEquals("feature_request", FeedbackCategory.FEATURE_REQUEST.value)
    }

    @Test
    fun `FeedbackCategory fromValue should work correctly`() {
        // Then
        assertEquals(FeedbackCategory.GENERAL, FeedbackCategory.fromValue("general"))
        assertEquals(FeedbackCategory.BUG_REPORT, FeedbackCategory.fromValue("bug_report"))
        assertEquals(FeedbackCategory.FEATURE_REQUEST, FeedbackCategory.fromValue("feature_request"))
        assertEquals(FeedbackCategory.GENERAL, FeedbackCategory.fromValue("unknown"))
    }
} 