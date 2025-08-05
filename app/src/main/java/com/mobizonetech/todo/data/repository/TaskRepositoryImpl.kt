package com.mobizonetech.todo.data.repository

import com.mobizonetech.todo.data.api.TodoApiService
import com.mobizonetech.todo.data.api.models.TaskApiModel
import com.mobizonetech.todo.data.database.dao.TaskDao
import com.mobizonetech.todo.data.database.entities.TaskEntity
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import com.mobizonetech.todo.util.ApiLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskRepositoryImpl @Inject constructor(
    private val apiService: TodoApiService,
    private val taskDao: TaskDao,
    private val retryManager: RetryManager
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
    ): Flow<Result<List<Task>>> = flow {
        val apiName = "GET_TASKS"
        val requestData = mapOf(
            "page" to page,
            "limit" to limit,
            "priority" to priority?.name,
            "categoryId" to categoryId,
            "dueDate" to dueDate,
            "completed" to completed,
            "search" to search,
            "sortBy" to sortBy,
            "sortOrder" to sortOrder
        )
        
        ApiLogger.logApiCall(apiName, requestData)
        
        try {
            // Use retry manager for API call
            val tasks = retryManager.retry(operationName = apiName) {
                // Simulate API delay
                delay(2000)
                
                // Mock response - get tasks from local database
                taskDao.getAllTasks().first().map { it.toDomain() }
            }
            
            ApiLogger.logApiSuccess(apiName, "Retrieved ${tasks.size} tasks")
            emit(Result.success(tasks))
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            emit(Result.failure(e))
        }
    }

    override fun getTask(taskId: String): Flow<Result<Task>> = flow {
        val apiName = "GET_TASK"
        val requestData = mapOf("taskId" to taskId)
        
        ApiLogger.logApiCall(apiName, requestData)
        
        try {
            // Use retry manager for API call
            val task = retryManager.retry(operationName = apiName) {
                // Simulate API delay
                delay(2000)
                
                taskDao.getTaskById(taskId)?.toDomain()
                    ?: throw Exception("Task not found")
            }
            
            ApiLogger.logApiSuccess(apiName, "Task retrieved: ${task.title}")
            emit(Result.success(task))
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            emit(Result.failure(e))
        }
    }

    override suspend fun createTask(
        title: String,
        description: String?,
        priority: TaskPriority,
        categoryId: String?,
        dueDate: String?
    ): Result<Task> {
        val apiName = "CREATE_TASK"
        val requestData = mapOf("title" to title, "description" to description, "priority" to priority)
        ApiLogger.logApiCall(apiName, requestData)
        return try {
            // Simulate API delay
            delay(2000)
            val dueDateParsed = dueDate?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) }
            val now = LocalDateTime.now()
            val taskEntity = TaskEntity(
                id = UUID.randomUUID().toString(),
                title = title,
                description = description,
                priority = priority.name,
                categoryId = categoryId,
                dueDate = dueDateParsed,
                completed = false,
                createdAt = now,
                updatedAt = now
            )
            taskDao.insertTask(taskEntity)
            val createdTask = taskEntity.toDomain()
            ApiLogger.logApiSuccess(apiName, "Task created: ${createdTask.title}")
            Result.success(createdTask)
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
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
        val apiName = "UPDATE_TASK"
        val requestData = mapOf(
            "taskId" to taskId,
            "title" to title,
            "description" to description,
            "priority" to priority?.name,
            "categoryId" to categoryId,
            "dueDate" to dueDate,
            "completed" to completed
        )
        ApiLogger.logApiCall(apiName, requestData)
        return try {
            // Simulate API delay
            delay(2000)
            val existingTask = taskDao.getTaskById(taskId)
            if (existingTask != null) {
                val dueDateParsed = dueDate?.let { LocalDateTime.parse(it, DateTimeFormatter.ISO_LOCAL_DATE_TIME) } ?: existingTask.dueDate
                val updatedTask = existingTask.copy(
                    title = title ?: existingTask.title,
                    description = description ?: existingTask.description,
                    priority = priority?.name ?: existingTask.priority,
                    categoryId = categoryId ?: existingTask.categoryId,
                    dueDate = dueDateParsed,
                    completed = completed ?: existingTask.completed,
                    updatedAt = LocalDateTime.now()
                )
                taskDao.updateTask(updatedTask)
                val result = updatedTask.toDomain()
                ApiLogger.logApiSuccess(apiName, "Task updated: ${result.title}")
                Result.success(result)
            } else {
                val error = Exception("Task not found")
                ApiLogger.logApiError(apiName, error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }

    override suspend fun deleteTask(taskId: String): Result<Unit> {
        val apiName = "DELETE_TASK"
        val requestData = mapOf("taskId" to taskId)
        
        ApiLogger.logApiCall(apiName, requestData)
        
        return try {
            // Simulate API delay
            delay(2000)
            
            taskDao.deleteTaskById(taskId)
            
            ApiLogger.logApiSuccess(apiName, "Task deleted successfully")
            Result.success(Unit)
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
            Result.failure(e)
        }
    }

    override suspend fun completeTask(taskId: String): Result<Task> {
        val apiName = "COMPLETE_TASK"
        val requestData = mapOf("taskId" to taskId)
        ApiLogger.logApiCall(apiName, requestData)
        return try {
            // Simulate API delay
            delay(2000)
            val existingTask = taskDao.getTaskById(taskId)
            if (existingTask != null) {
                // Toggle the completion status
                val updatedTask = existingTask.copy(
                    completed = !existingTask.completed,
                    updatedAt = LocalDateTime.now()
                )
                taskDao.updateTask(updatedTask)
                val result = updatedTask.toDomain()
                val action = if (updatedTask.completed) "completed" else "uncompleted"
                ApiLogger.logApiSuccess(apiName, "Task $action: ${result.title}")
                Result.success(result)
            } else {
                val error = Exception("Task not found")
                ApiLogger.logApiError(apiName, error)
                Result.failure(error)
            }
        } catch (e: Exception) {
            ApiLogger.logApiError(apiName, e)
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