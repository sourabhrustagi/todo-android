package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class CategoryTest {

    @Test
    fun `category should have all required properties`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now,
            updatedAt = now
        )

        // When & Then
        assertEquals("cat_1", category.id)
        assertEquals("Work", category.name)
        assertEquals("#FF5722", category.color)
        assertEquals(now, category.createdAt)
        assertEquals(now, category.updatedAt)
    }

    @Test
    fun `category should have null updatedAt by default`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals("cat_1", category.id)
        assertEquals("Work", category.name)
        assertEquals("#FF5722", category.color)
        assertEquals(now, category.createdAt)
        assertNull(category.updatedAt)
    }

    @Test
    fun `category should be equal to itself`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals(category, category)
    }

    @Test
    fun `categories with same properties should be equal`() {
        // Given
        val now = LocalDateTime.now()
        val category1 = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )
        val category2 = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals(category1, category2)
    }

    @Test
    fun `categories with different properties should not be equal`() {
        // Given
        val now = LocalDateTime.now()
        val category1 = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )
        val category2 = Category(
            id = "cat_2",
            name = "Personal",
            color = "#4CAF50",
            createdAt = now
        )

        // When & Then
        assertNotEquals(category1, category2)
    }

    @Test
    fun `category should have correct hash code`() {
        // Given
        val now = LocalDateTime.now()
        val category1 = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )
        val category2 = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals(category1.hashCode(), category2.hashCode())
    }

    @Test
    fun `category should have meaningful string representation`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )

        // When
        val stringRepresentation = category.toString()

        // Then
        assertTrue(stringRepresentation.contains("cat_1"))
        assertTrue(stringRepresentation.contains("Work"))
        assertTrue(stringRepresentation.contains("#FF5722"))
    }

    @Test
    fun `category should handle empty name`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals("", category.name)
    }

    @Test
    fun `category should handle special characters in name`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work & Personal",
            color = "#FF5722",
            createdAt = now
        )

        // When & Then
        assertEquals("Work & Personal", category.name)
    }

    @Test
    fun `category should handle different color formats`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "rgb(255, 87, 34)",
            createdAt = now
        )

        // When & Then
        assertEquals("rgb(255, 87, 34)", category.color)
    }
} 