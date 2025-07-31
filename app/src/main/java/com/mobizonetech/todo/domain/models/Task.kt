package com.mobizonetech.todo.domain.models

import java.time.LocalDateTime

data class Task(
    val id: String,
    val title: String,
    val description: String? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val category: Category? = null,
    val dueDate: LocalDateTime? = null,
    val completed: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    fun isOverdue(): Boolean {
        return dueDate != null && !completed && LocalDateTime.now().isAfter(dueDate)
    }

    fun isDueToday(): Boolean {
        return dueDate != null && !completed && dueDate.toLocalDate() == LocalDateTime.now().toLocalDate()
    }

    fun isDueThisWeek(): Boolean {
        return dueDate != null && !completed && dueDate.toLocalDate().isBefore(LocalDateTime.now().plusDays(7).toLocalDate())
    }
}

enum class TaskPriority {
    HIGH, MEDIUM, LOW;

    fun getDisplayName(): String {
        return when (this) {
            HIGH -> "High"
            MEDIUM -> "Medium"
            LOW -> "Low"
        }
    }

    fun getColor(): String {
        return when (this) {
            HIGH -> "#FF5722"
            MEDIUM -> "#FF9800"
            LOW -> "#4CAF50"
        }
    }
} 