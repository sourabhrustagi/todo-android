package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class UpdateTaskUseCaseTest {

    private lateinit var updateTaskUseCase: UpdateTaskUseCase
    private lateinit var taskRepository: TaskRepository

    @Before
    fun setup() {
        taskRepository = mockk()
        updateTaskUseCase = UpdateTaskUseCase(taskRepository)
    }

    @Test
    fun `update task with valid data should return success`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        val description = "Updated description"
        val priority = TaskPriority.HIGH
        val categoryId = "1"
        val dueDate = "2024-01-01T10:00:00"
        val completed = false
        
        val expectedTask = Task(
            id = taskId,
            title = title,
            description = description,
            priority = priority,
            category = null,
            dueDate = LocalDateTime.parse("2024-01-01T10:00:00"),
            completed = completed,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = description,
                priority = priority,
                categoryId = categoryId,
                dueDate = dueDate,
                completed = completed
            ) 
        } returns Result.success(expectedTask)
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title,
            description = description,
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate,
            completed = completed
        )
        
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `update task with empty task ID should fail`() = runTest {
        val taskId = ""
        
        val result = updateTaskUseCase(taskId = taskId)
        
        assertTrue(result.isFailure)
        assertEquals("Task ID cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `update task with blank task ID should fail`() = runTest {
        val taskId = "   "
        
        val result = updateTaskUseCase(taskId = taskId)
        
        assertTrue(result.isFailure)
        assertEquals("Task ID cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `update task with partial data should succeed`() = runTest {
        val taskId = "1"
        val title = "Updated Title"
        
        val expectedTask = Task(
            id = taskId,
            title = title,
            description = null,
            priority = TaskPriority.MEDIUM,
            category = null,
            dueDate = null,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = null,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.success(expectedTask)
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title
        )
        
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `update task should handle repository failure`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        val errorMessage = "Task not found"
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = null,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.failure(Exception(errorMessage))
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title
        )
        
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `update task with all parameters should succeed`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        val description = "Updated description"
        val priority = TaskPriority.LOW
        val categoryId = "2"
        val dueDate = "2024-01-01T10:00:00"
        val completed = true
        
        val expectedTask = Task(
            id = taskId,
            title = title,
            description = description,
            priority = priority,
            category = null,
            dueDate = LocalDateTime.parse("2024-01-01T10:00:00"),
            completed = completed,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = description,
                priority = priority,
                categoryId = categoryId,
                dueDate = dueDate,
                completed = completed
            ) 
        } returns Result.success(expectedTask)
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title,
            description = description,
            priority = priority,
            categoryId = categoryId,
            dueDate = dueDate,
            completed = completed
        )
        
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `update task with trimmed strings should work`() = runTest {
        val taskId = "1"
        val title = "  Updated Task  "
        val description = "  Updated description  "
        
        val expectedTask = Task(
            id = taskId,
            title = "Updated Task",
            description = "Updated description",
            priority = TaskPriority.MEDIUM,
            category = null,
            dueDate = null,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = "Updated Task",
                description = "Updated description",
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.success(expectedTask)
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title,
            description = description
        )
        
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `network error should be handled properly`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        val errorMessage = "Network error occurred"
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = null,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.failure(Exception(errorMessage))
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title
        )
        
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `database error should be handled properly`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        val errorMessage = "Database error occurred"
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = null,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.failure(Exception(errorMessage))
        
        val result = updateTaskUseCase(
            taskId = taskId,
            title = title
        )
        
        assertTrue(result.isFailure)
        assertEquals(errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `multiple update calls should work correctly`() = runTest {
        val taskId = "1"
        val title = "Updated Task"
        
        val expectedTask = Task(
            id = taskId,
            title = title,
            description = null,
            priority = TaskPriority.MEDIUM,
            category = null,
            dueDate = null,
            completed = false,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        
        coEvery { 
            taskRepository.updateTask(
                taskId = taskId,
                title = title,
                description = null,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null
            ) 
        } returns Result.success(expectedTask)
        
        val result1 = updateTaskUseCase(taskId = taskId, title = title)
        val result2 = updateTaskUseCase(taskId = taskId, title = title)
        
        assertTrue(result1.isSuccess)
        assertTrue(result2.isSuccess)
        assertEquals(expectedTask, result1.getOrNull())
        assertEquals(expectedTask, result2.getOrNull())
    }
} 