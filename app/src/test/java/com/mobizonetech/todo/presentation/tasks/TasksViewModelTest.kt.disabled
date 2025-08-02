package com.mobizonetech.todo.presentation.tasks

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.usecases.task.CompleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.CreateTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.DeleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.GetTasksUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class TasksViewModelTest {

    private lateinit var getTasksUseCase: GetTasksUseCase
    private lateinit var createTaskUseCase: CreateTaskUseCase
    private lateinit var completeTaskUseCase: CompleteTaskUseCase
    private lateinit var deleteTaskUseCase: DeleteTaskUseCase
    private lateinit var viewModel: TasksViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getTasksUseCase = mockk()
        createTaskUseCase = mockk()
        completeTaskUseCase = mockk()
        deleteTaskUseCase = mockk()
        viewModel = TasksViewModel(getTasksUseCase, createTaskUseCase, completeTaskUseCase, deleteTaskUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() = runTest {
        // When
        val initialState = viewModel.uiState.value

        // Then
        assertTrue(initialState.tasks.isEmpty())
        assertFalse(initialState.isLoading)
        assertNull(initialState.error)
    }

    @Test
    fun `loadTasks should update state to loading then success`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "Test Task",
                description = "Test Description",
                priority = TaskPriority.HIGH,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery { getTasksUseCase() } returns flowOf(Result.success(tasks))

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(tasks, finalState.tasks)
        assertFalse(finalState.isLoading)
        assertNull(finalState.error)
    }

    @Test
    fun `loadTasks should handle error state`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { getTasksUseCase() } returns flowOf(Result.failure(RuntimeException(errorMessage)))

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertTrue(finalState.tasks.isEmpty())
        assertFalse(finalState.isLoading)
        assertEquals("Unknown error occurred", finalState.error)
    }

    @Test
    fun `loadTasks should handle repository exception with message`() = runTest {
        // Given
        val errorMessage = "Custom error message"
        coEvery { getTasksUseCase() } returns flowOf(Result.failure(RuntimeException(errorMessage)))

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(errorMessage, finalState.error)
    }

    @Test
    fun `createTask should reload tasks on success`() = runTest {
        // Given
        val newTask = Task(
            id = "task_2",
            title = "New Task",
            description = "New Description",
            priority = TaskPriority.MEDIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { createTaskUseCase("New Task", "New Description") } returns Result.success(newTask)
        coEvery { getTasksUseCase() } returns flowOf(Result.success(listOf(newTask)))

        // When
        viewModel.createTask("New Task", "New Description")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(listOf(newTask), finalState.tasks)
        assertFalse(finalState.isLoading)
        assertNull(finalState.error)
    }

    @Test
    fun `createTask should handle error`() = runTest {
        // Given
        val errorMessage = "Failed to create task"
        coEvery { createTaskUseCase("New Task", "New Description") } returns Result.failure(RuntimeException(errorMessage))

        // When
        viewModel.createTask("New Task", "New Description")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(errorMessage, finalState.error)
    }

    @Test
    fun `createTask should handle null description`() = runTest {
        // Given
        val newTask = Task(
            id = "task_2",
            title = "New Task",
            description = null,
            priority = TaskPriority.MEDIUM,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { createTaskUseCase("New Task", null) } returns Result.success(newTask)
        coEvery { getTasksUseCase() } returns flowOf(Result.success(listOf(newTask)))

        // When
        viewModel.createTask("New Task", null)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(listOf(newTask), finalState.tasks)
        assertFalse(finalState.isLoading)
        assertNull(finalState.error)
    }

    @Test
    fun `toggleTaskCompletion should update specific task`() = runTest {
        // Given
        val initialTasks = listOf(
            Task(
                id = "task_1",
                title = "Test Task",
                completed = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        val updatedTask = Task(
            id = "task_1",
            title = "Test Task",
            completed = true,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        coEvery { getTasksUseCase() } returns flowOf(Result.success(initialTasks))
        coEvery { completeTaskUseCase("task_1") } returns Result.success(updatedTask)

        // Set up initial state
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // When
        viewModel.toggleTaskCompletion("task_1")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(1, finalState.tasks.size)
        assertTrue(finalState.tasks.first().completed)
        assertFalse(finalState.isUpdatingTask)
    }

    @Test
    fun `deleteTask should reload tasks`() = runTest {
        // Given
        val tasks = listOf<Task>()
        coEvery { getTasksUseCase() } returns flowOf(Result.success(tasks))

        // When
        viewModel.deleteTask("task_1")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertEquals(tasks, finalState.tasks)
    }

    @Test
    fun `loadTasks should set loading state initially`() = runTest {
        // Given
        val tasks = listOf<Task>()
        coEvery { getTasksUseCase() } returns flowOf(Result.success(tasks))

        // When
        viewModel.loadTasks()

        // Then
        val loadingState = viewModel.uiState.value
        assertTrue(loadingState.isLoading)
        assertNull(loadingState.error)
    }

    @Test
    fun `multiple loadTasks calls should work correctly`() = runTest {
        // Given
        val tasks1 = listOf(
            Task(
                id = "task_1",
                title = "Task 1",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        val tasks2 = listOf(
            Task(
                id = "task_2",
                title = "Task 2",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery { getTasksUseCase() } returnsMany listOf(
            flowOf(Result.success(tasks1)),
            flowOf(Result.success(tasks2))
        )

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val firstState = viewModel.uiState.value
        assertEquals(tasks1, firstState.tasks)

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val secondState = viewModel.uiState.value
        assertEquals(tasks2, secondState.tasks)
    }

    @Test
    fun `createTask should clear error on success`() = runTest {
        // Given
        val newTask = Task(
            id = "task_1",
            title = "New Task",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )

        // Set initial error state
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        coEvery { createTaskUseCase("New Task", null) } returns Result.success(newTask)
        coEvery { getTasksUseCase() } returns flowOf(Result.success(listOf(newTask)))

        // When
        viewModel.createTask("New Task", null)
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val finalState = viewModel.uiState.value
        assertNull(finalState.error)
    }

    @Test
    fun `uiState should be observable`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "Test Task",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        coEvery { getTasksUseCase() } returns flowOf(Result.success(tasks))

        // When
        viewModel.loadTasks()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.uiState.value
        assertNotNull(state)
        assertEquals(tasks, state.tasks)
    }
} 