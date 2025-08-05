package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.core.result.Result
import com.mobizonetech.todo.domain.models.CreateTaskParams
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.models.ValidationResult
import com.mobizonetech.todo.domain.repository.TaskRepository
import com.mobizonetech.todo.domain.validation.TaskValidator
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val taskValidator: TaskValidator
) {
    suspend operator fun invoke(params: CreateTaskParams): Result<Task> {
        // Validate input first
        val validationResult = taskValidator.validateCreateTask(params)
        if (!validationResult.isValid) {
            return com.mobizonetech.todo.core.result.Result.error(ValidationException(validationResult.errors))
        }
        
        // Create task
        return taskRepository.createTask(
            title = params.title,
            description = params.description,
            priority = params.priority,
            categoryId = params.categoryId,
            dueDate = params.dueDate?.toString()
        ).fold(
            onSuccess = { task -> com.mobizonetech.todo.core.result.Result.success(task) },
            onFailure = { exception -> com.mobizonetech.todo.core.result.Result.error(exception) }
        )
    }

    // Backward compatibility method
    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        categoryId: String? = null,
        dueDate: String? = null
    ): Result<Task> {
        val params = CreateTaskParams(
            title = title,
            description = description,
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate?.let { java.time.LocalDateTime.parse(it) }
        )
        return invoke(params)
    }
}

class ValidationException(val errors: List<String>) : Exception("Validation failed: ${errors.joinToString(", ")}") 