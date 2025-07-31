package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.database.dao.TaskDao
import com.mobizonetech.todo.data.database.entities.TaskEntity
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getTasks(
        page: Int,
        limit: Int,
        priority: TaskPriority?,
        categoryId: String?,
        dueDate: String?,
        completed: Boolean?,
        search: String?,
        sortBy: String?,
        sortOrder: String?
    ): Flow<Result<List<Task>>> {
        return taskDao.getAllTasks().map { taskEntities ->
            try {
                val tasks = taskEntities.map { it.toDomain() }
                Result.success(tasks)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override fun getTask(taskId: String): Flow<Result<Task>> {
        return taskDao.getAllTasks().map { taskEntities ->
            try {
                val task = taskEntities.find { it.id == taskId }?.toDomain()
                if (task != null) {
                    Result.success(task)
                } else {
                    Result.failure(Exception("Task not found"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    override suspend fun createTask(
        title: String,
        description: String?,
        priority: TaskPriority,
        categoryId: String?,
        dueDate: String?
    ): Result<Task> {
        return try {
            val taskEntity = TaskEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                priority = priority.name,
                categoryId = categoryId,
                dueDate = dueDate?.let { LocalDateTime.parse(it) },
                completed = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
            taskDao.insertTask(taskEntity)
            Result.success(taskEntity.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateTask(
        taskId: String,
        title: String?,
        description: String?,
        priority: TaskPriority?,
        categoryId: String?,
        dueDate: String?,
        completed: Boolean?
    ): Result<Task> {
        return try {
            val existingTask = taskDao.getTaskById(taskId)
            if (existingTask != null) {
                val updatedTask = existingTask.copy(
                    title = title ?: existingTask.title,
                    description = description ?: existingTask.description,
                    priority = priority?.name ?: existingTask.priority,
                    categoryId = categoryId ?: existingTask.categoryId,
                    dueDate = dueDate?.let { LocalDateTime.parse(it) } ?: existingTask.dueDate,
                    completed = completed ?: existingTask.completed,
                    updatedAt = LocalDateTime.now()
                )
                taskDao.updateTask(updatedTask)
                Result.success(updatedTask.toDomain())
            } else {
                Result.failure(Exception("Task not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        return try {
            taskDao.deleteTaskById(taskId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun completeTask(taskId: String): Result<Task> {
        return try {
            val existingTask = taskDao.getTaskById(taskId)
            if (existingTask != null) {
                val newCompletedState = !existingTask.completed
                taskDao.updateTaskCompletion(taskId, newCompletedState)
                val updatedTask = existingTask.copy(
                    completed = newCompletedState,
                    updatedAt = LocalDateTime.now()
                )
                Result.success(updatedTask.toDomain())
            } else {
                Result.failure(Exception("Task not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun searchTasks(
        query: String,
        fields: String?,
        fuzzy: Boolean?
    ): Result<List<Task>> {
        return try {
            // TODO: Implement actual search logic
            Result.success(emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTaskAnalytics(): Result<Map<String, Any>> {
        return try {
            // TODO: Implement actual analytics logic
            val analytics = mapOf(
                "total" to 0,
                "completed" to 0,
                "pending" to 0,
                "overdue" to 0
            )
            Result.success(analytics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun bulkOperation(
        operation: String,
        taskIds: List<String>,
        categoryId: String?,
        priority: TaskPriority?
    ): Result<Map<String, Any>> {
        return try {
            val results = mutableListOf<String>()
            for (taskId in taskIds) {
                when (operation) {
                    "delete" -> {
                        taskDao.deleteTaskById(taskId)
                        results.add("Deleted task $taskId")
                    }
                    "complete" -> {
                        taskDao.updateTaskCompletion(taskId, true)
                        results.add("Completed task $taskId")
                    }
                    "update" -> {
                        val existingTask = taskDao.getTaskById(taskId)
                        if (existingTask != null) {
                            val updatedTask = existingTask.copy(
                                categoryId = categoryId ?: existingTask.categoryId,
                                priority = priority?.name ?: existingTask.priority,
                                updatedAt = LocalDateTime.now()
                            )
                            taskDao.updateTask(updatedTask)
                            results.add("Updated task $taskId")
                        }
                    }
                }
            }
            Result.success(mapOf("results" to results))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun TaskEntity.toDomain(): Task {
        return Task(
            id = id,
            title = title,
            description = description,
            priority = TaskPriority.valueOf(priority),
            category = null, // TODO: Implement category mapping
            dueDate = dueDate,
            completed = completed,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
} 