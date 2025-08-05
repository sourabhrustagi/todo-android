package com.mobizonetech.todo.domain.models

import java.time.LocalDateTime

/**
 * Domain models for task operations with proper validation
 */

data class CreateTaskParams(
    val title: String,
    val description: String?,
    val priority: TaskPriority,
    val categoryId: String?,
    val dueDate: LocalDateTime?
) {
    init {
        require(title.isNotBlank()) { "Title cannot be empty" }
        require(title.length <= 100) { "Title cannot exceed 100 characters" }
        description?.let {
            require(it.length <= 500) { "Description cannot exceed 500 characters" }
        }
    }
}

data class UpdateTaskParams(
    val id: String,
    val title: String?,
    val description: String?,
    val priority: TaskPriority?,
    val categoryId: String?,
    val dueDate: LocalDateTime?,
    val completed: Boolean?
) {
    init {
        require(id.isNotBlank()) { "Task ID cannot be empty" }
        title?.let {
            require(it.isNotBlank()) { "Title cannot be empty" }
            require(it.length <= 100) { "Title cannot exceed 100 characters" }
        }
        description?.let {
            require(it.length <= 500) { "Description cannot exceed 500 characters" }
        }
    }
}

data class TaskQueryParams(
    val page: Int = 1,
    val limit: Int = 20,
    val priority: TaskPriority? = null,
    val categoryId: String? = null,
    val dueDate: LocalDateTime? = null,
    val completed: Boolean? = null,
    val search: String? = null,
    val sortBy: TaskSortBy = TaskSortBy.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC
) {
    init {
        require(page > 0) { "Page must be greater than 0" }
        require(limit in 1..100) { "Limit must be between 1 and 100" }
        search?.let {
            require(it.length <= 50) { "Search query cannot exceed 50 characters" }
        }
    }
}

enum class TaskSortBy {
    TITLE, PRIORITY, DUE_DATE, CREATED_AT, UPDATED_AT
}

enum class SortOrder {
    ASC, DESC
}

data class ValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
) {
    fun isSuccess(): Boolean = isValid
    fun getErrorMessage(): String = errors.joinToString(", ")
} 