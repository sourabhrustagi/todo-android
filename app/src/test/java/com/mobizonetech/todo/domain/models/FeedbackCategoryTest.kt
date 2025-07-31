package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test

class FeedbackCategoryTest {

    @Test
    fun `GENERAL category should have correct display name`() {
        // When & Then
        assertEquals("General", FeedbackCategory.GENERAL.getDisplayName())
    }

    @Test
    fun `FEATURE_REQUEST category should have correct display name`() {
        // When & Then
        assertEquals("Feature Request", FeedbackCategory.FEATURE_REQUEST.getDisplayName())
    }

    @Test
    fun `BUG_REPORT category should have correct display name`() {
        // When & Then
        assertEquals("Bug Report", FeedbackCategory.BUG_REPORT.getDisplayName())
    }

    @Test
    fun `IMPROVEMENT category should have correct display name`() {
        // When & Then
        assertEquals("Improvement", FeedbackCategory.IMPROVEMENT.getDisplayName())
    }

    @Test
    fun `all categories should have unique display names`() {
        // Given
        val displayNames = FeedbackCategory.values().map { it.getDisplayName() }

        // When & Then
        assertEquals(4, displayNames.size)
        assertEquals(4, displayNames.toSet().size) // All unique
    }

    @Test
    fun `category values should be in correct order`() {
        // When & Then
        assertEquals(FeedbackCategory.GENERAL, FeedbackCategory.values()[0])
        assertEquals(FeedbackCategory.FEATURE_REQUEST, FeedbackCategory.values()[1])
        assertEquals(FeedbackCategory.BUG_REPORT, FeedbackCategory.values()[2])
        assertEquals(FeedbackCategory.IMPROVEMENT, FeedbackCategory.values()[3])
    }

    @Test
    fun `category enum should have exactly 4 values`() {
        // When & Then
        assertEquals(4, FeedbackCategory.values().size)
    }

    @Test
    fun `display names should not be empty`() {
        // When & Then
        FeedbackCategory.values().forEach { category ->
            assertTrue("Display name for $category should not be empty", 
                category.getDisplayName().isNotEmpty())
        }
    }

    @Test
    fun `display names should not contain special characters`() {
        // When & Then
        FeedbackCategory.values().forEach { category ->
            val displayName = category.getDisplayName()
            assertFalse("Display name '$displayName' should not contain special characters",
                displayName.contains("_") || displayName.contains("-"))
        }
    }
} 