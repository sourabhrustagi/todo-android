package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class FeedbackApiModelTest {

    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun `Feedback data class should have correct properties`() {
        val feedback = FeedbackApiModel.Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = "general",
            createdAt = "2024-01-15T10:30:00Z"
        )

        assertEquals("feedback_123", feedback.id)
        assertEquals(5, feedback.rating)
        assertEquals("Great app!", feedback.comment)
        assertEquals("general", feedback.category)
        assertEquals("2024-01-15T10:30:00Z", feedback.createdAt)
    }

    @Test
    fun `Feedback should serialize and deserialize correctly`() {
        val originalFeedback = FeedbackApiModel.Feedback(
            id = "feedback_123",
            rating = 4,
            comment = "Good experience",
            category = "bug_report",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val adapter = moshi.adapter(FeedbackApiModel.Feedback::class.java)
        val json = adapter.toJson(originalFeedback)
        val deserializedFeedback = adapter.fromJson(json)

        assertEquals(originalFeedback, deserializedFeedback)
    }

    @Test
    fun `Feedback with null comment should handle correctly`() {
        val feedback = FeedbackApiModel.Feedback(
            id = "feedback_456",
            rating = 3,
            comment = null,
            category = "feature_request",
            createdAt = "2024-01-16T14:20:00Z"
        )

        assertEquals(null, feedback.comment)
        assertEquals(3, feedback.rating)
    }

    @Test
    fun `SubmitFeedbackRequest should have correct properties`() {
        val request = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 5,
            comment = "Excellent app",
            category = "general"
        )

        assertEquals(5, request.rating)
        assertEquals("Excellent app", request.comment)
        assertEquals("general", request.category)
    }

    @Test
    fun `SubmitFeedbackRequest with default category should work`() {
        val request = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 4,
            comment = "Good app"
        )

        assertEquals(4, request.rating)
        assertEquals("Good app", request.comment)
        assertEquals("general", request.category) // default value
    }

    @Test
    fun `SubmitFeedbackRequest should serialize and deserialize correctly`() {
        val originalRequest = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 5,
            comment = "Amazing experience",
            category = "bug_report"
        )

        val adapter = moshi.adapter(FeedbackApiModel.SubmitFeedbackRequest::class.java)
        val json = adapter.toJson(originalRequest)
        val deserializedRequest = adapter.fromJson(json)

        assertEquals(originalRequest, deserializedRequest)
    }

    @Test
    fun `SubmitFeedbackRequest with null comment should handle correctly`() {
        val request = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 2,
            comment = null,
            category = "feature_request"
        )

        assertEquals(2, request.rating)
        assertEquals(null, request.comment)
        assertEquals("feature_request", request.category)
    }

    @Test
    fun `AnalyticsResponse should have correct properties`() {
        val ratingDistribution = mapOf("1" to 5, "2" to 10, "3" to 15, "4" to 20, "5" to 25)
        val categoryDistribution = mapOf("general" to 30, "bug_report" to 15, "feature_request" to 10)

        val analytics = FeedbackApiModel.AnalyticsResponse(
            totalFeedback = 75,
            averageRating = 3.8,
            ratingDistribution = ratingDistribution,
            categoryDistribution = categoryDistribution
        )

        assertEquals(75, analytics.totalFeedback)
        assertEquals(3.8, analytics.averageRating, 0.01)
        assertEquals(ratingDistribution, analytics.ratingDistribution)
        assertEquals(categoryDistribution, analytics.categoryDistribution)
    }

    @Test
    fun `AnalyticsResponse should serialize and deserialize correctly`() {
        val ratingDistribution = mapOf("1" to 2, "2" to 3, "3" to 5, "4" to 8, "5" to 12)
        val categoryDistribution = mapOf("general" to 15, "bug_report" to 8, "feature_request" to 7)

        val originalAnalytics = FeedbackApiModel.AnalyticsResponse(
            totalFeedback = 30,
            averageRating = 4.2,
            ratingDistribution = ratingDistribution,
            categoryDistribution = categoryDistribution
        )

        val adapter = moshi.adapter(FeedbackApiModel.AnalyticsResponse::class.java)
        val json = adapter.toJson(originalAnalytics)
        val deserializedAnalytics = adapter.fromJson(json)

        assertEquals(originalAnalytics, deserializedAnalytics)
    }

    @Test
    fun `AnalyticsResponse with empty distributions should work`() {
        val analytics = FeedbackApiModel.AnalyticsResponse(
            totalFeedback = 0,
            averageRating = 0.0,
            ratingDistribution = emptyMap(),
            categoryDistribution = emptyMap()
        )

        assertEquals(0, analytics.totalFeedback)
        assertEquals(0.0, analytics.averageRating, 0.01)
        assertTrue(analytics.ratingDistribution.isEmpty())
        assertTrue(analytics.categoryDistribution.isEmpty())
    }

    @Test
    fun `Feedback equals and hashCode should work correctly`() {
        val feedback1 = FeedbackApiModel.Feedback(
            id = "test_123",
            rating = 4,
            comment = "Test comment",
            category = "general",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val feedback2 = FeedbackApiModel.Feedback(
            id = "test_123",
            rating = 4,
            comment = "Test comment",
            category = "general",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val feedback3 = FeedbackApiModel.Feedback(
            id = "test_456",
            rating = 4,
            comment = "Test comment",
            category = "general",
            createdAt = "2024-01-15T10:30:00Z"
        )

        assertEquals(feedback1, feedback2)
        assertNotEquals(feedback1, feedback3)
        assertEquals(feedback1.hashCode(), feedback2.hashCode())
        assertNotEquals(feedback1.hashCode(), feedback3.hashCode())
    }

    @Test
    fun `SubmitFeedbackRequest equals and hashCode should work correctly`() {
        val request1 = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 5,
            comment = "Great app",
            category = "general"
        )

        val request2 = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 5,
            comment = "Great app",
            category = "general"
        )

        val request3 = FeedbackApiModel.SubmitFeedbackRequest(
            rating = 4,
            comment = "Great app",
            category = "general"
        )

        assertEquals(request1, request2)
        assertNotEquals(request1, request3)
        assertEquals(request1.hashCode(), request2.hashCode())
        assertNotEquals(request1.hashCode(), request3.hashCode())
    }
} 