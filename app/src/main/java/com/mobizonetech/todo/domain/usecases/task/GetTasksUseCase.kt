package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(
    private val taskRepository: TaskRepository
) {
    operator fun invoke(
        page: Int = 1,
        limit: Int = 20,
        priority: TaskPriority? = null,
        categoryId: String? = null,
        dueDate: String? = null,
        completed: Boolean? = null,
        search: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ): Flow<Result<List<Task>>> {
        return taskRepository.getTasks(
            page = page,
            limit = limit,
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate,
            completed = completed,
            search = search,
            sortBy = sortBy,
            sortOrder = sortOrder
        )
    }
} 