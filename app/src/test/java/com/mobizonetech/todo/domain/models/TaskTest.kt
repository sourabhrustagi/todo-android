package com.mobizonetech.todo.domain.models

import org.junit.Assert.*
import org.junit.Test
import java.time.LocalDateTime

class TaskTest {

    @Test
    fun `task with due date in past should be overdue`() {
        // Given
        val pastDate = LocalDateTime.now().minusDays(1)
        val task = Task(
            id = "task_1",
            title = "Overdue Task",
            dueDate = pastDate,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertTrue(task.isOverdue())
    }

    @Test
    fun `task with due date in future should not be overdue`() {
        // Given
        val futureDate = LocalDateTime.now().plusDays(1)
        val task = Task(
            id = "task_1",
            title = "Future Task",
            dueDate = futureDate,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isOverdue())
    }

    @Test
    fun `completed task should not be overdue even with past due date`() {
        // Given
        val pastDate = LocalDateTime.now().minusDays(1)
        val task = Task(
            id = "task_1",
            title = "Completed Overdue Task",
            dueDate = pastDate,
            completed = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isOverdue())
    }

    @Test
    fun `task with null due date should not be overdue`() {
        // Given
        val task = Task(
            id = "task_1",
            title = "No Due Date Task",
            dueDate = null,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isOverdue())
    }

    @Test
    fun `task with due date today should be due today`() {
        // Given
        val today = LocalDateTime.now()
        val task = Task(
            id = "task_1",
            title = "Today Task",
            dueDate = today,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertTrue(task.isDueToday())
    }

    @Test
    fun `task with due date tomorrow should not be due today`() {
        // Given
        val tomorrow = LocalDateTime.now().plusDays(1)
        val task = Task(
            id = "task_1",
            title = "Tomorrow Task",
            dueDate = tomorrow,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isDueToday())
    }

    @Test
    fun `completed task should not be due today`() {
        // Given
        val today = LocalDateTime.now()
        val task = Task(
            id = "task_1",
            title = "Completed Today Task",
            dueDate = today,
            completed = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isDueToday())
    }

    @Test
    fun `task with due date within 7 days should be due this week`() {
        // Given
        val withinWeek = LocalDateTime.now().plusDays(3)
        val task = Task(
            id = "task_1",
            title = "This Week Task",
            dueDate = withinWeek,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertTrue(task.isDueThisWeek())
    }

    @Test
    fun `task with due date more than 7 days should not be due this week`() {
        // Given
        val beyondWeek = LocalDateTime.now().plusDays(10)
        val task = Task(
            id = "task_1",
            title = "Beyond Week Task",
            dueDate = beyondWeek,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isDueThisWeek())
    }

    @Test
    fun `completed task should not be due this week`() {
        // Given
        val withinWeek = LocalDateTime.now().plusDays(3)
        val task = Task(
            id = "task_1",
            title = "Completed This Week Task",
            dueDate = withinWeek,
            completed = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isDueThisWeek())
    }

    @Test
    fun `task with null due date should not be due this week`() {
        // Given
        val task = Task(
            id = "task_1",
            title = "No Due Date Task",
            dueDate = null,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertFalse(task.isDueThisWeek())
    }

    @Test
    fun `task should have correct default values`() {
        // Given
        val task = Task(
            id = "task_1",
            title = "Test Task",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // When & Then
        assertEquals("Test Task", task.title)
        assertNull(task.description)
        assertEquals(TaskPriority.MEDIUM, task.priority)
        assertNull(task.category)
        assertNull(task.dueDate)
        assertFalse(task.completed)
    }

    @Test
    fun `task should have all properties set correctly`() {
        // Given
        val now = LocalDateTime.now()
        val category = Category(
            id = "cat_1",
            name = "Work",
            color = "#FF5722",
            createdAt = now
        )
        val dueDate = now.plusDays(1)
        
        val task = Task(
            id = "task_1",
            title = "Complete Task",
            description = "Task description",
            priority = TaskPriority.HIGH,
            category = category,
            dueDate = dueDate,
            completed = true,
            createdAt = now,
            updatedAt = now
        )

        // When & Then
        assertEquals("task_1", task.id)
        assertEquals("Complete Task", task.title)
        assertEquals("Task description", task.description)
        assertEquals(TaskPriority.HIGH, task.priority)
        assertEquals(category, task.category)
        assertEquals(dueDate, task.dueDate)
        assertTrue(task.completed)
        assertEquals(now, task.createdAt)
        assertEquals(now, task.updatedAt)
    }
} 