package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import javax.inject.Inject

class CreateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        categoryId: String? = null,
        dueDate: String? = null
    ): Result<Task> {
        if (title.isBlank()) {
            return Result.failure(IllegalArgumentException("Task title cannot be empty"))
        }
        
        return taskRepository.createTask(
            title = title.trim(),
            description = description?.trim(),
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate
        )
    }
} 