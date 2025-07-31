package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class CreateTaskUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var createTaskUseCase: CreateTaskUseCase

    @Before
    fun setUp() {
        taskRepository = mockk()
        createTaskUseCase = CreateTaskUseCase(taskRepository)
    }

    @Test
    fun `createTask with valid data should return success`() = runTest {
        // Given
        val title = "Test Task"
        val description = "Test Description"
        val priority = TaskPriority.HIGH
        val expectedTask = Task(
            id = "task_1",
            title = title,
            description = description,
            priority = priority,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery {
            taskRepository.createTask(
                title = title,
                description = description,
                priority = priority,
                categoryId = null,
                dueDate = null
            )
        } returns Result.success(expectedTask)

        // When
        val result = createTaskUseCase(title, description, priority)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `createTask with empty title should return failure`() = runTest {
        // Given
        val title = ""
        val description = "Test Description"

        // When
        val result = createTaskUseCase(title, description)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Task title cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `createTask with blank title should return failure`() = runTest {
        // Given
        val title = "   "
        val description = "Test Description"

        // When
        val result = createTaskUseCase(title, description)

        // Then
        assertTrue(result.isFailure)
        assertEquals("Task title cannot be empty", result.exceptionOrNull()?.message)
    }

    @Test
    fun `createTask should trim title and description`() = runTest {
        // Given
        val title = "  Test Task  "
        val description = "  Test Description  "
        val expectedTask = Task(
            id = "task_1",
            title = "Test Task",
            description = "Test Description",
            priority = TaskPriority.MEDIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery {
            taskRepository.createTask(
                title = "Test Task",
                description = "Test Description",
                priority = TaskPriority.MEDIUM,
                categoryId = null,
                dueDate = null
            )
        } returns Result.success(expectedTask)

        // When
        val result = createTaskUseCase(title, description)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }

    @Test
    fun `createTask with null description should pass null to repository`() = runTest {
        // Given
        val title = "Test Task"
        val description: String? = null
        val expectedTask = Task(
            id = "task_1",
            title = title,
            description = null,
            priority = TaskPriority.MEDIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery {
            taskRepository.createTask(
                title = title,
                description = null,
                priority = TaskPriority.MEDIUM,
                categoryId = null,
                dueDate = null
            )
        } returns Result.success(expectedTask)

        // When
        val result = createTaskUseCase(title, description)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedTask, result.getOrNull())
    }
} 