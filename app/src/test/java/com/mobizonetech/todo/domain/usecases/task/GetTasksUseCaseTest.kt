package com.mobizonetech.todo.domain.usecases.task

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.repository.TaskRepository
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class GetTasksUseCaseTest {

    private lateinit var taskRepository: TaskRepository
    private lateinit var getTasksUseCase: GetTasksUseCase

    @Before
    fun setUp() {
        taskRepository = mockk()
        getTasksUseCase = GetTasksUseCase(taskRepository)
    }

    @Test
    fun `getTasks with default parameters should return success`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "Test Task 1",
                description = "Test Description 1",
                priority = TaskPriority.HIGH,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery {
            taskRepository.getTasks(
                page = 1,
                limit = 20,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null,
                search = null,
                sortBy = null,
                sortOrder = null
            )
        } returns flowOf(Result.success(tasks))

        // When
        val result = getTasksUseCase().collect { result ->
            // Then
            assertTrue(result.isSuccess)
            assertEquals(tasks, result.getOrNull())
        }
    }

    @Test
    fun `getTasks with custom parameters should return success`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_2",
                title = "Test Task 2",
                description = "Test Description 2",
                priority = TaskPriority.MEDIUM,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery {
            taskRepository.getTasks(
                page = 2,
                limit = 10,
                priority = TaskPriority.HIGH,
                categoryId = "cat_1",
                dueDate = "2024-01-20",
                completed = false,
                search = "test",
                sortBy = "title",
                sortOrder = "asc"
            )
        } returns flowOf(Result.success(tasks))

        // When
        val result = getTasksUseCase(
            page = 2,
            limit = 10,
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-20",
            completed = false,
            search = "test",
            sortBy = "title",
            sortOrder = "asc"
        ).collect { result ->
            // Then
            assertTrue(result.isSuccess)
            assertEquals(tasks, result.getOrNull())
        }
    }

    @Test
    fun `getTasks with repository failure should return failure`() = runTest {
        // Given
        val exception = RuntimeException("Network error")

        coEvery {
            taskRepository.getTasks(
                page = 1,
                limit = 20,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null,
                search = null,
                sortBy = null,
                sortOrder = null
            )
        } returns flowOf(Result.failure(exception))

        // When
        val result = getTasksUseCase().collect { result ->
            // Then
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
        }
    }

    @Test
    fun `getTasks with empty list should return empty list`() = runTest {
        // Given
        val emptyTasks = emptyList<Task>()

        coEvery {
            taskRepository.getTasks(
                page = 1,
                limit = 20,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null,
                search = null,
                sortBy = null,
                sortOrder = null
            )
        } returns flowOf(Result.success(emptyTasks))

        // When
        val result = getTasksUseCase().collect { result ->
            // Then
            assertTrue(result.isSuccess)
            assertTrue(result.getOrNull()?.isEmpty() == true)
        }
    }

    @Test
    fun `getTasks with all priority filters should work correctly`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "High Priority Task",
                priority = TaskPriority.HIGH,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        TaskPriority.values().forEach { priority ->
            coEvery {
                taskRepository.getTasks(
                    page = 1,
                    limit = 20,
                    priority = priority,
                    categoryId = null,
                    dueDate = null,
                    completed = null,
                    search = null,
                    sortBy = null,
                    sortOrder = null
                )
            } returns flowOf(Result.success(tasks))

            // When
            val result = getTasksUseCase(priority = priority).collect { result ->
                // Then
                assertTrue("Should succeed for priority $priority", result.isSuccess)
                assertEquals(tasks, result.getOrNull())
            }
        }
    }

    @Test
    fun `getTasks with null parameters should pass null to repository`() = runTest {
        // Given
        val tasks = listOf<Task>()

        coEvery {
            taskRepository.getTasks(
                page = 1,
                limit = 20,
                priority = null,
                categoryId = null,
                dueDate = null,
                completed = null,
                search = null,
                sortBy = null,
                sortOrder = null
            )
        } returns flowOf(Result.success(tasks))

        // When
        val result = getTasksUseCase(
            priority = null,
            categoryId = null,
            dueDate = null,
            completed = null,
            search = null,
            sortBy = null,
            sortOrder = null
        ).collect { result ->
            // Then
            assertTrue(result.isSuccess)
        }
    }
} 