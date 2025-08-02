package com.mobizonetech.todo.domain.repository

import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class TaskRepositoryTest {

    @Mock
    private lateinit var taskRepository: TaskRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
    }

    @Test
    fun `getTasks should return Flow with task list`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "Task 1",
                description = "Description 1",
                priority = TaskPriority.HIGH,
                categoryId = "cat_1",
                dueDate = "2024-01-20T18:00:00Z",
                completed = false,
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            ),
            Task(
                id = "task_2",
                title = "Task 2",
                description = "Description 2",
                priority = TaskPriority.MEDIUM,
                categoryId = "cat_2",
                dueDate = "2024-01-25T18:00:00Z",
                completed = true,
                createdAt = "2024-01-16T10:30:00Z",
                updatedAt = "2024-01-16T10:30:00Z"
            )
        )
        val result = Result.success(tasks)
        val flow: Flow<Result<List<Task>>> = flowOf(result)

        `when`(taskRepository.getTasks()).thenReturn(flow)

        // When
        val actualResult = taskRepository.getTasks().first()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(tasks, actualResult.getOrNull())
        verify(taskRepository).getTasks()
    }

    @Test
    fun `getTasks with parameters should return filtered task list`() = runTest {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "High priority task",
                priority = TaskPriority.HIGH,
                categoryId = "cat_1",
                completed = false,
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            )
        )
        val result = Result.success(tasks)
        val flow: Flow<Result<List<Task>>> = flowOf(result)

        `when`(taskRepository.getTasks(
            page = 2,
            limit = 10,
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-20",
            completed = false,
            search = "important",
            sortBy = "dueDate",
            sortOrder = "asc"
        )).thenReturn(flow)

        // When
        val actualResult = taskRepository.getTasks(
            page = 2,
            limit = 10,
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-20",
            completed = false,
            search = "important",
            sortBy = "dueDate",
            sortOrder = "asc"
        ).first()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(tasks, actualResult.getOrNull())
        verify(taskRepository).getTasks(
            page = 2,
            limit = 10,
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-20",
            completed = false,
            search = "important",
            sortBy = "dueDate",
            sortOrder = "asc"
        )
    }

    @Test
    fun `getTasks should handle error`() = runTest {
        // Given
        val error = Exception("Network error")
        val result = Result.failure<List<Task>>(error)
        val flow: Flow<Result<List<Task>>> = flowOf(result)

        `when`(taskRepository.getTasks()).thenReturn(flow)

        // When
        val actualResult = taskRepository.getTasks().first()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).getTasks()
    }

    @Test
    fun `getTask should return Flow with single task`() = runTest {
        // Given
        val task = Task(
            id = "task_123",
            title = "Specific task",
            description = "Task description",
            priority = TaskPriority.MEDIUM,
            categoryId = "cat_1",
            dueDate = "2024-01-20T18:00:00Z",
            completed = false,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )
        val result = Result.success(task)
        val flow: Flow<Result<Task>> = flowOf(result)

        `when`(taskRepository.getTask("task_123")).thenReturn(flow)

        // When
        val actualResult = taskRepository.getTask("task_123").first()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(task, actualResult.getOrNull())
        verify(taskRepository).getTask("task_123")
    }

    @Test
    fun `getTask should handle error`() = runTest {
        // Given
        val error = Exception("Task not found")
        val result = Result.failure<Task>(error)
        val flow: Flow<Result<Task>> = flowOf(result)

        `when`(taskRepository.getTask("task_456")).thenReturn(flow)

        // When
        val actualResult = taskRepository.getTask("task_456").first()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).getTask("task_456")
    }

    @Test
    fun `createTask should return success result`() = runTest {
        // Given
        val createdTask = Task(
            id = "task_456",
            title = "New task",
            description = "Task description",
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-25T18:00:00Z",
            completed = false,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )
        val result = Result.success(createdTask)

        `when`(taskRepository.createTask(
            title = "New task",
            description = "Task description",
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-25T18:00:00Z"
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.createTask(
            title = "New task",
            description = "Task description",
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-25T18:00:00Z"
        )

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(createdTask, actualResult.getOrNull())
        verify(taskRepository).createTask(
            title = "New task",
            description = "Task description",
            priority = TaskPriority.HIGH,
            categoryId = "cat_1",
            dueDate = "2024-01-25T18:00:00Z"
        )
    }

    @Test
    fun `createTask with minimal parameters should work`() = runTest {
        // Given
        val createdTask = Task(
            id = "task_789",
            title = "Simple task",
            priority = TaskPriority.MEDIUM,
            completed = false,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )
        val result = Result.success(createdTask)

        `when`(taskRepository.createTask(title = "Simple task")).thenReturn(result)

        // When
        val actualResult = taskRepository.createTask(title = "Simple task")

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(createdTask, actualResult.getOrNull())
        verify(taskRepository).createTask(title = "Simple task")
    }

    @Test
    fun `createTask should handle error`() = runTest {
        // Given
        val error = Exception("Failed to create task")
        val result = Result.failure<Task>(error)

        `when`(taskRepository.createTask(title = "Failed task")).thenReturn(result)

        // When
        val actualResult = taskRepository.createTask(title = "Failed task")

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).createTask(title = "Failed task")
    }

    @Test
    fun `updateTask should return success result`() = runTest {
        // Given
        val updatedTask = Task(
            id = "task_123",
            title = "Updated task",
            description = "Updated description",
            priority = TaskPriority.MEDIUM,
            categoryId = "cat_2",
            dueDate = "2024-01-30T18:00:00Z",
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )
        val result = Result.success(updatedTask)

        `when`(taskRepository.updateTask(
            taskId = "task_123",
            title = "Updated task",
            description = "Updated description",
            priority = TaskPriority.MEDIUM,
            categoryId = "cat_2",
            dueDate = "2024-01-30T18:00:00Z",
            completed = true
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.updateTask(
            taskId = "task_123",
            title = "Updated task",
            description = "Updated description",
            priority = TaskPriority.MEDIUM,
            categoryId = "cat_2",
            dueDate = "2024-01-30T18:00:00Z",
            completed = true
        )

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(updatedTask, actualResult.getOrNull())
        verify(taskRepository).updateTask(
            taskId = "task_123",
            title = "Updated task",
            description = "Updated description",
            priority = TaskPriority.MEDIUM,
            categoryId = "cat_2",
            dueDate = "2024-01-30T18:00:00Z",
            completed = true
        )
    }

    @Test
    fun `updateTask with partial parameters should work`() = runTest {
        // Given
        val updatedTask = Task(
            id = "task_123",
            title = "Original title",
            description = "Updated description",
            priority = TaskPriority.HIGH,
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )
        val result = Result.success(updatedTask)

        `when`(taskRepository.updateTask(
            taskId = "task_123",
            description = "Updated description",
            priority = TaskPriority.HIGH,
            completed = true
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.updateTask(
            taskId = "task_123",
            description = "Updated description",
            priority = TaskPriority.HIGH,
            completed = true
        )

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(updatedTask, actualResult.getOrNull())
        verify(taskRepository).updateTask(
            taskId = "task_123",
            description = "Updated description",
            priority = TaskPriority.HIGH,
            completed = true
        )
    }

    @Test
    fun `updateTask should handle error`() = runTest {
        // Given
        val error = Exception("Failed to update task")
        val result = Result.failure<Task>(error)

        `when`(taskRepository.updateTask(taskId = "task_456")).thenReturn(result)

        // When
        val actualResult = taskRepository.updateTask(taskId = "task_456")

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).updateTask(taskId = "task_456")
    }

    @Test
    fun `deleteTask should return success result`() = runTest {
        // Given
        val result = Result.success(Unit)

        `when`(taskRepository.deleteTask("task_123")).thenReturn(result)

        // When
        val actualResult = taskRepository.deleteTask("task_123")

        // Then
        assertTrue(actualResult.isSuccess)
        verify(taskRepository).deleteTask("task_123")
    }

    @Test
    fun `deleteTask should handle error`() = runTest {
        // Given
        val error = Exception("Failed to delete task")
        val result = Result.failure<Unit>(error)

        `when`(taskRepository.deleteTask("task_456")).thenReturn(result)

        // When
        val actualResult = taskRepository.deleteTask("task_456")

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).deleteTask("task_456")
    }

    @Test
    fun `completeTask should return success result`() = runTest {
        // Given
        val completedTask = Task(
            id = "task_123",
            title = "Completed task",
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )
        val result = Result.success(completedTask)

        `when`(taskRepository.completeTask("task_123")).thenReturn(result)

        // When
        val actualResult = taskRepository.completeTask("task_123")

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(completedTask, actualResult.getOrNull())
        assertTrue(completedTask.completed)
        verify(taskRepository).completeTask("task_123")
    }

    @Test
    fun `completeTask should handle error`() = runTest {
        // Given
        val error = Exception("Failed to complete task")
        val result = Result.failure<Task>(error)

        `when`(taskRepository.completeTask("task_456")).thenReturn(result)

        // When
        val actualResult = taskRepository.completeTask("task_456")

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).completeTask("task_456")
    }

    @Test
    fun `searchTasks should return success result`() = runTest {
        // Given
        val searchResults = listOf(
            Task(
                id = "task_1",
                title = "Search result 1",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            ),
            Task(
                id = "task_2",
                title = "Search result 2",
                createdAt = "2024-01-16T10:30:00Z",
                updatedAt = "2024-01-16T10:30:00Z"
            )
        )
        val result = Result.success(searchResults)

        `when`(taskRepository.searchTasks(
            query = "important",
            fields = "title,description",
            fuzzy = true
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.searchTasks(
            query = "important",
            fields = "title,description",
            fuzzy = true
        )

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(searchResults, actualResult.getOrNull())
        verify(taskRepository).searchTasks(
            query = "important",
            fields = "title,description",
            fuzzy = true
        )
    }

    @Test
    fun `searchTasks should handle error`() = runTest {
        // Given
        val error = Exception("Search failed")
        val result = Result.failure<List<Task>>(error)

        `when`(taskRepository.searchTasks(query = "failed")).thenReturn(result)

        // When
        val actualResult = taskRepository.searchTasks(query = "failed")

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).searchTasks(query = "failed")
    }

    @Test
    fun `getTaskAnalytics should return success result`() = runTest {
        // Given
        val analytics = mapOf(
            "total" to 18,
            "completed" to 12,
            "pending" to 6,
            "overdue" to 2,
            "completionRate" to 66.67
        )
        val result = Result.success(analytics)

        `when`(taskRepository.getTaskAnalytics()).thenReturn(result)

        // When
        val actualResult = taskRepository.getTaskAnalytics()

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(analytics, actualResult.getOrNull())
        verify(taskRepository).getTaskAnalytics()
    }

    @Test
    fun `getTaskAnalytics should handle error`() = runTest {
        // Given
        val error = Exception("Analytics failed")
        val result = Result.failure<Map<String, Any>>(error)

        `when`(taskRepository.getTaskAnalytics()).thenReturn(result)

        // When
        val actualResult = taskRepository.getTaskAnalytics()

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).getTaskAnalytics()
    }

    @Test
    fun `bulkOperation should return success result`() = runTest {
        // Given
        val bulkResult = mapOf(
            "updatedCount" to 3,
            "message" to "Successfully updated 3 tasks"
        )
        val result = Result.success(bulkResult)

        `when`(taskRepository.bulkOperation(
            operation = "update_priority",
            taskIds = listOf("task_1", "task_2", "task_3"),
            categoryId = "cat_1",
            priority = TaskPriority.HIGH
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.bulkOperation(
            operation = "update_priority",
            taskIds = listOf("task_1", "task_2", "task_3"),
            categoryId = "cat_1",
            priority = TaskPriority.HIGH
        )

        // Then
        assertTrue(actualResult.isSuccess)
        assertEquals(bulkResult, actualResult.getOrNull())
        verify(taskRepository).bulkOperation(
            operation = "update_priority",
            taskIds = listOf("task_1", "task_2", "task_3"),
            categoryId = "cat_1",
            priority = TaskPriority.HIGH
        )
    }

    @Test
    fun `bulkOperation should handle error`() = runTest {
        // Given
        val error = Exception("Bulk operation failed")
        val result = Result.failure<Map<String, Any>>(error)

        `when`(taskRepository.bulkOperation(
            operation = "failed_operation",
            taskIds = listOf("task_1")
        )).thenReturn(result)

        // When
        val actualResult = taskRepository.bulkOperation(
            operation = "failed_operation",
            taskIds = listOf("task_1")
        )

        // Then
        assertTrue(actualResult.isFailure)
        assertEquals(error, actualResult.exceptionOrNull())
        verify(taskRepository).bulkOperation(
            operation = "failed_operation",
            taskIds = listOf("task_1")
        )
    }
} 