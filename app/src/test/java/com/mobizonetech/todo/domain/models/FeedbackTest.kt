package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class FeedbackTest {

    @Test
    fun `feedback should have all required properties`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When & Then
        assertEquals("feedback_123", feedback.id)
        assertEquals(5, feedback.rating)
        assertEquals("Great app!", feedback.comment)
        assertEquals(FeedbackCategory.GENERAL, feedback.category)
        assertEquals(now, feedback.createdAt)
    }

    @Test
    fun `feedback should have default category when not specified`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 4,
            comment = "Good app",
            createdAt = now
        )

        // When & Then
        assertEquals(FeedbackCategory.GENERAL, feedback.category)
    }

    @Test
    fun `feedback should handle null comment`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 3,
            comment = null,
            category = FeedbackCategory.BUG_REPORT,
            createdAt = now
        )

        // When & Then
        assertNull(feedback.comment)
    }

    @Test
    fun `feedback should be equal to itself`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When & Then
        assertEquals(feedback, feedback)
    }

    @Test
    fun `feedbacks with same properties should be equal`() {
        // Given
        val now = LocalDateTime.now()
        val feedback1 = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )
        val feedback2 = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When & Then
        assertEquals(feedback1, feedback2)
    }

    @Test
    fun `feedbacks with different properties should not be equal`() {
        // Given
        val now = LocalDateTime.now()
        val feedback1 = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )
        val feedback2 = Feedback(
            id = "feedback_456",
            rating = 4,
            comment = "Good app",
            category = FeedbackCategory.FEATURE_REQUEST,
            createdAt = now
        )

        // When & Then
        assertNotEquals(feedback1, feedback2)
    }

    @Test
    fun `feedback should have correct hash code`() {
        // Given
        val now = LocalDateTime.now()
        val feedback1 = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )
        val feedback2 = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When & Then
        assertEquals(feedback1.hashCode(), feedback2.hashCode())
    }

    @Test
    fun `feedback should have meaningful string representation`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "Great app!",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When
        val stringRepresentation = feedback.toString()

        // Then
        assertTrue(stringRepresentation.contains("feedback_123"))
        assertTrue(stringRepresentation.contains("5"))
        assertTrue(stringRepresentation.contains("Great app!"))
        assertTrue(stringRepresentation.contains("GENERAL"))
    }

    @Test
    fun `feedback should handle all rating values`() {
        // Given
        val now = LocalDateTime.now()
        val ratings = listOf(1, 2, 3, 4, 5)

        ratings.forEach { rating ->
            val feedback = Feedback(
                id = "feedback_$rating",
                rating = rating,
                comment = "Rating $rating",
                category = FeedbackCategory.GENERAL,
                createdAt = now
            )

            // When & Then
            assertEquals(rating, feedback.rating)
        }
    }

    @Test
    fun `feedback should handle all categories`() {
        // Given
        val now = LocalDateTime.now()
        val categories = FeedbackCategory.values()

        categories.forEach { category ->
            val feedback = Feedback(
                id = "feedback_${category.name}",
                rating = 4,
                comment = "Category test",
                category = category,
                createdAt = now
            )

            // When & Then
            assertEquals(category, feedback.category)
        }
    }

    @Test
    fun `feedback should handle empty comment`() {
        // Given
        val now = LocalDateTime.now()
        val feedback = Feedback(
            id = "feedback_123",
            rating = 5,
            comment = "",
            category = FeedbackCategory.GENERAL,
            createdAt = now
        )

        // When & Then
        assertEquals("", feedback.comment)
    }

    @Test
    fun `feedback should handle long comments`() {
        // Given
        val now = LocalDateTime.now()
        val longComment = "This is a very long comment that contains multiple sentences. " +
                "It should be handled properly by the feedback system. " +
                "The comment can contain various characters and formatting."
        val feedback = Feedback(
            id = "feedback_123",
            rating = 4,
            comment = longComment,
            category = FeedbackCategory.IMPROVEMENT,
            createdAt = now
        )

        // When & Then
        assertEquals(longComment, feedback.comment)
    }

    @Test
    fun `feedback should handle special characters in comment`() {
        // Given
        val now = LocalDateTime.now()
        val specialComment = "App with special chars: @#$%^&*()_+-=[]{}|;':\",./<>?"
        val feedback = Feedback(
            id = "feedback_123",
            rating = 3,
            comment = specialComment,
            category = FeedbackCategory.BUG_REPORT,
            createdAt = now
        )

        // When & Then
        assertEquals(specialComment, feedback.comment)
    }
} 