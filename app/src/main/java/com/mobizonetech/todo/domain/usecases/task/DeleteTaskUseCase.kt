package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.repository.TaskRepository
import javax.inject.Inject

class DeleteTaskUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Result<Unit> {
        if (taskId.isBlank()) {
            return Result.failure(IllegalArgumentException("Task ID cannot be empty"))
        }
        
        return taskRepository.deleteTask(taskId)
    }
} 