package com.mobizonetech.todo.domain.validation

import com.mobizonetech.todo.domain.models.CreateTaskParams
import com.mobizonetech.todo.domain.models.UpdateTaskParams
import com.mobizonetech.todo.domain.models.ValidationResult
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskValidator @Inject constructor() {

    fun validateCreateTask(params: CreateTaskParams): ValidationResult {
        val errors = mutableListOf<String>()

        // Title validation
        if (params.title.isBlank()) {
            errors.add("Title cannot be empty")
        } else if (params.title.length > 100) {
            errors.add("Title cannot exceed 100 characters")
        }

        // Description validation
        params.description?.let { description ->
            if (description.length > 500) {
                errors.add("Description cannot exceed 500 characters")
            }
        }

        // Due date validation
        params.dueDate?.let { dueDate ->
            if (dueDate.isBefore(LocalDateTime.now())) {
                errors.add("Due date cannot be in the past")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    fun validateUpdateTask(params: UpdateTaskParams): ValidationResult {
        val errors = mutableListOf<String>()

        // ID validation
        if (params.id.isBlank()) {
            errors.add("Task ID cannot be empty")
        }

        // Title validation
        params.title?.let { title ->
            when {
                title.isBlank() -> errors.add("Title cannot be empty")
                title.length > 100 -> errors.add("Title cannot exceed 100 characters")
                else -> { /* Title is valid */ }
            }
        }

        // Description validation
        params.description?.let { description ->
            if (description.length > 500) {
                errors.add("Description cannot exceed 500 characters")
            }
        }

        // Due date validation
        params.dueDate?.let { dueDate ->
            if (dueDate.isBefore(LocalDateTime.now())) {
                errors.add("Due date cannot be in the past")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }

    fun validateTaskQuery(params: com.mobizonetech.todo.domain.models.TaskQueryParams): ValidationResult {
        val errors = mutableListOf<String>()

        // Page validation
        if (params.page <= 0) {
            errors.add("Page must be greater than 0")
        }

        // Limit validation
        if (params.limit !in 1..100) {
            errors.add("Limit must be between 1 and 100")
        }

        // Search query validation
        params.search?.let { search ->
            if (search.length > 50) {
                errors.add("Search query cannot exceed 50 characters")
            }
        }

        return ValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
} 