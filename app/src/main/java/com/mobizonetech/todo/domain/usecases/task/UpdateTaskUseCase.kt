package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(
        taskId: String,
        title: String? = null,
        description: String? = null,
        priority: TaskPriority? = null,
        categoryId: String? = null,
        dueDate: String? = null,
        completed: Boolean? = null
    ): Result<Task> {
        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("Task ID cannot be empty"))
        }
        
        return taskRepository.updateTask(
            taskId = taskId,
            title = title?.trim(),
            description = description?.trim(),
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate,
            completed = completed
        )
    }
} 