package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test

class TaskPriorityTest {

    @Test
    fun `HIGH priority should have correct display name`() {
        // When & Then
        assertEquals("High", TaskPriority.HIGH.getDisplayName())
    }

    @Test
    fun `MEDIUM priority should have correct display name`() {
        // When & Then
        assertEquals("Medium", TaskPriority.MEDIUM.getDisplayName())
    }

    @Test
    fun `LOW priority should have correct display name`() {
        // When & Then
        assertEquals("Low", TaskPriority.LOW.getDisplayName())
    }

    @Test
    fun `HIGH priority should have correct color`() {
        // When & Then
        assertEquals("#FF5722", TaskPriority.HIGH.getColor())
    }

    @Test
    fun `MEDIUM priority should have correct color`() {
        // When & Then
        assertEquals("#FF9800", TaskPriority.MEDIUM.getColor())
    }

    @Test
    fun `LOW priority should have correct color`() {
        // When & Then
        assertEquals("#4CAF50", TaskPriority.LOW.getColor())
    }

    @Test
    fun `all priorities should have unique display names`() {
        // Given
        val displayNames = TaskPriority.values().map { it.getDisplayName() }

        // When & Then
        assertEquals(3, displayNames.size)
        assertEquals(3, displayNames.toSet().size) // All unique
    }

    @Test
    fun `all priorities should have unique colors`() {
        // Given
        val colors = TaskPriority.values().map { it.getColor() }

        // When & Then
        assertEquals(3, colors.size)
        assertEquals(3, colors.toSet().size) // All unique
    }

    @Test
    fun `priority values should be in correct order`() {
        // When & Then
        assertEquals(TaskPriority.HIGH, TaskPriority.values()[0])
        assertEquals(TaskPriority.MEDIUM, TaskPriority.values()[1])
        assertEquals(TaskPriority.LOW, TaskPriority.values()[2])
    }

    @Test
    fun `priority enum should have exactly 3 values`() {
        // When & Then
        assertEquals(3, TaskPriority.values().size)
    }
} 