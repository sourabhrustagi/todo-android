package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(
        page: Int = 1,
        limit: Int = 20,
        priority: TaskPriority? = null,
        categoryId: String? = null,
        dueDate: String? = null,
        completed: Boolean? = null,
        search: String? = null,
        sortBy: String? = null,
        sortOrder: String? = null
    ): Flow<Result<List<Task>>>

    fun getTask(taskId: String): Flow<Result<Task>>

    suspend fun createTask(
        title: String,
        description: String? = null,
        priority: TaskPriority = TaskPriority.MEDIUM,
        categoryId: String? = null,
        dueDate: String? = null
    ): Result<Task>

    suspend fun updateTask(
        taskId: String,
        title: String? = null,
        description: String? = null,
        priority: TaskPriority? = null,
        categoryId: String? = null,
        dueDate: String? = null,
        completed: Boolean? = null
    ): Result<Task>

    suspend fun deleteTask(taskId: String): Result<Unit>

    suspend fun completeTask(taskId: String): Result<Task>

    suspend fun searchTasks(
        query: String,
        fields: String? = null,
        fuzzy: Boolean? = null
    ): Result<List<Task>>

    suspend fun getTaskAnalytics(): Result<Map<String, Any>>

    suspend fun bulkOperation(
        operation: String,
        taskIds: List<String>,
        categoryId: String? = null,
        priority: TaskPriority? = null
    ): Result<Map<String, Any>>

    suspend fun syncTasksFromServer(): Result<Unit>
} 