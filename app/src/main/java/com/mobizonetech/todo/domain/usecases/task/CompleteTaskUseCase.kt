package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.repository.TaskRepository
import javax.inject.Inject

class CompleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Result<Task> {
        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("Task ID cannot be empty"))
        }
        
        return taskRepository.completeTask(taskId)
    }
} 