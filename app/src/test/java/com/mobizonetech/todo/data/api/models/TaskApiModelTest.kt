package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class TaskApiModelTest {

    private lateinit var moshi: Moshi

    @Before
    fun setUp() {
        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Test
    fun `Task data class should have correct properties`() {
        val category = TaskApiModel.Category(
            id = "cat_123",
            name = "Work",
            color = "#FF5733",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val task = TaskApiModel.Task(
            id = "task_123",
            title = "Complete project",
            description = "Finish the Android app",
            priority = "high",
            category = category,
            dueDate = "2024-01-20T18:00:00Z",
            completed = false,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )

        assertEquals("task_123", task.id)
        assertEquals("Complete project", task.title)
        assertEquals("Finish the Android app", task.description)
        assertEquals("high", task.priority)
        assertEquals(category, task.category)
        assertEquals("2024-01-20T18:00:00Z", task.dueDate)
        assertFalse(task.completed)
        assertEquals("2024-01-15T10:30:00Z", task.createdAt)
        assertEquals("2024-01-15T10:30:00Z", task.updatedAt)
    }

    @Test
    fun `Task should serialize and deserialize correctly`() {
        val category = TaskApiModel.Category(
            id = "cat_456",
            name = "Personal",
            color = "#33FF57",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val originalTask = TaskApiModel.Task(
            id = "task_456",
            title = "Buy groceries",
            description = "Milk, bread, eggs",
            priority = "medium",
            category = category,
            dueDate = "2024-01-18T12:00:00Z",
            completed = true,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-16T14:20:00Z"
        )

        val adapter = moshi.adapter(TaskApiModel.Task::class.java)
        val json = adapter.toJson(originalTask)
        val deserializedTask = adapter.fromJson(json)

        assertEquals(originalTask, deserializedTask)
    }

    @Test
    fun `Task with null optional fields should handle correctly`() {
        val task = TaskApiModel.Task(
            id = "task_789",
            title = "Simple task",
            description = null,
            priority = null,
            category = null,
            dueDate = null,
            completed = false,
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )

        assertEquals("task_789", task.id)
        assertEquals("Simple task", task.title)
        assertNull(task.description)
        assertNull(task.priority)
        assertNull(task.category)
        assertNull(task.dueDate)
        assertFalse(task.completed)
    }

    @Test
    fun `CreateTaskRequest should have correct properties`() {
        val request = TaskApiModel.CreateTaskRequest(
            title = "New task",
            description = "Task description",
            priority = "high",
            categoryId = "cat_123",
            dueDate = "2024-01-25T18:00:00Z"
        )

        assertEquals("New task", request.title)
        assertEquals("Task description", request.description)
        assertEquals("high", request.priority)
        assertEquals("cat_123", request.categoryId)
        assertEquals("2024-01-25T18:00:00Z", request.dueDate)
    }

    @Test
    fun `CreateTaskRequest should serialize and deserialize correctly`() {
        val originalRequest = TaskApiModel.CreateTaskRequest(
            title = "Important task",
            description = "Must complete this",
            priority = "urgent",
            categoryId = "cat_456",
            dueDate = "2024-01-22T12:00:00Z"
        )

        val adapter = moshi.adapter(TaskApiModel.CreateTaskRequest::class.java)
        val json = adapter.toJson(originalRequest)
        val deserializedRequest = adapter.fromJson(json)

        assertEquals(originalRequest, deserializedRequest)
    }

    @Test
    fun `UpdateTaskRequest should handle partial updates correctly`() {
        val request = TaskApiModel.UpdateTaskRequest(
            title = "Updated title",
            description = null,
            priority = "medium",
            categoryId = null,
            dueDate = "2024-01-30T18:00:00Z",
            completed = true
        )

        assertEquals("Updated title", request.title)
        assertNull(request.description)
        assertEquals("medium", request.priority)
        assertNull(request.categoryId)
        assertEquals("2024-01-30T18:00:00Z", request.dueDate)
        assertTrue(request.completed!!)
    }

    @Test
    fun `UpdateTaskRequest should serialize and deserialize correctly`() {
        val originalRequest = TaskApiModel.UpdateTaskRequest(
            title = "Updated task",
            description = "Updated description",
            priority = "low",
            categoryId = "cat_789",
            dueDate = null,
            completed = false
        )

        val adapter = moshi.adapter(TaskApiModel.UpdateTaskRequest::class.java)
        val json = adapter.toJson(originalRequest)
        val deserializedRequest = adapter.fromJson(json)

        assertEquals(originalRequest, deserializedRequest)
    }

    @Test
    fun `TaskListResponse should have correct properties`() {
        val tasks = listOf(
            TaskApiModel.Task(
                id = "task_1",
                title = "Task 1",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            ),
            TaskApiModel.Task(
                id = "task_2",
                title = "Task 2",
                createdAt = "2024-01-15T11:30:00Z",
                updatedAt = "2024-01-15T11:30:00Z"
            )
        )

        val pagination = TaskApiModel.Pagination(
            page = 1,
            limit = 10,
            total = 25,
            totalPages = 3
        )

        val response = TaskApiModel.TaskListResponse(
            tasks = tasks,
            pagination = pagination
        )

        assertEquals(2, response.tasks.size)
        assertEquals(1, response.pagination.page)
        assertEquals(10, response.pagination.limit)
        assertEquals(25, response.pagination.total)
        assertEquals(3, response.pagination.totalPages)
    }

    @Test
    fun `Pagination should have correct properties`() {
        val pagination = TaskApiModel.Pagination(
            page = 2,
            limit = 20,
            total = 100,
            totalPages = 5
        )

        assertEquals(2, pagination.page)
        assertEquals(20, pagination.limit)
        assertEquals(100, pagination.total)
        assertEquals(5, pagination.totalPages)
    }

    @Test
    fun `Category should have correct properties`() {
        val category = TaskApiModel.Category(
            id = "cat_123",
            name = "Work",
            color = "#FF5733",
            createdAt = "2024-01-15T10:30:00Z"
        )

        assertEquals("cat_123", category.id)
        assertEquals("Work", category.name)
        assertEquals("#FF5733", category.color)
        assertEquals("2024-01-15T10:30:00Z", category.createdAt)
    }

    @Test
    fun `Category should serialize and deserialize correctly`() {
        val originalCategory = TaskApiModel.Category(
            id = "cat_456",
            name = "Personal",
            color = "#33FF57",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val adapter = moshi.adapter(TaskApiModel.Category::class.java)
        val json = adapter.toJson(originalCategory)
        val deserializedCategory = adapter.fromJson(json)

        assertEquals(originalCategory, deserializedCategory)
    }

    @Test
    fun `CreateCategoryRequest should have correct properties`() {
        val request = TaskApiModel.CreateCategoryRequest(
            name = "Shopping",
            color = "#FFC300"
        )

        assertEquals("Shopping", request.name)
        assertEquals("#FFC300", request.color)
    }

    @Test
    fun `UpdateCategoryRequest should handle partial updates correctly`() {
        val request = TaskApiModel.UpdateCategoryRequest(
            name = "Updated Shopping",
            color = null
        )

        assertEquals("Updated Shopping", request.name)
        assertNull(request.color)
    }

    @Test
    fun `BulkOperationRequest should have correct properties`() {
        val request = TaskApiModel.BulkOperationRequest(
            operation = "update_priority",
            taskIds = listOf("task_1", "task_2", "task_3"),
            categoryId = "cat_123",
            priority = "high"
        )

        assertEquals("update_priority", request.operation)
        assertEquals(3, request.taskIds.size)
        assertEquals("cat_123", request.categoryId)
        assertEquals("high", request.priority)
    }

    @Test
    fun `BulkOperationResponse should have correct properties`() {
        val response = TaskApiModel.BulkOperationResponse(
            updatedCount = 5,
            message = "Successfully updated 5 tasks"
        )

        assertEquals(5, response.updatedCount)
        assertEquals("Successfully updated 5 tasks", response.message)
    }

    @Test
    fun `SearchResponse should have correct properties`() {
        val tasks = listOf(
            TaskApiModel.Task(
                id = "task_1",
                title = "Search result 1",
                createdAt = "2024-01-15T10:30:00Z",
                updatedAt = "2024-01-15T10:30:00Z"
            )
        )

        val response = TaskApiModel.SearchResponse(
            tasks = tasks,
            total = 1
        )

        assertEquals(1, response.tasks.size)
        assertEquals(1, response.total)
    }

    @Test
    fun `AnalyticsResponse should have correct properties`() {
        val byPriority = mapOf("high" to 5, "medium" to 10, "low" to 3)
        val byCategory = listOf(
            TaskApiModel.CategoryCount("Work", 8),
            TaskApiModel.CategoryCount("Personal", 5),
            TaskApiModel.CategoryCount("Shopping", 5)
        )

        val analytics = TaskApiModel.AnalyticsResponse(
            total = 18,
            completed = 12,
            pending = 6,
            overdue = 2,
            byPriority = byPriority,
            byCategory = byCategory,
            completionRate = 66.67
        )

        assertEquals(18, analytics.total)
        assertEquals(12, analytics.completed)
        assertEquals(6, analytics.pending)
        assertEquals(2, analytics.overdue)
        assertEquals(byPriority, analytics.byPriority)
        assertEquals(3, analytics.byCategory.size)
        assertEquals(66.67, analytics.completionRate, 0.01)
    }

    @Test
    fun `CategoryCount should have correct properties`() {
        val categoryCount = TaskApiModel.CategoryCount(
            category = "Work",
            count = 10
        )

        assertEquals("Work", categoryCount.category)
        assertEquals(10, categoryCount.count)
    }

    @Test
    fun `Task equals and hashCode should work correctly`() {
        val task1 = TaskApiModel.Task(
            id = "task_123",
            title = "Test task",
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )

        val task2 = TaskApiModel.Task(
            id = "task_123",
            title = "Test task",
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )

        val task3 = TaskApiModel.Task(
            id = "task_456",
            title = "Test task",
            createdAt = "2024-01-15T10:30:00Z",
            updatedAt = "2024-01-15T10:30:00Z"
        )

        assertEquals(task1, task2)
        assertNotEquals(task1, task3)
        assertEquals(task1.hashCode(), task2.hashCode())
        assertNotEquals(task1.hashCode(), task3.hashCode())
    }

    @Test
    fun `Category equals and hashCode should work correctly`() {
        val category1 = TaskApiModel.Category(
            id = "cat_123",
            name = "Work",
            color = "#FF5733",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val category2 = TaskApiModel.Category(
            id = "cat_123",
            name = "Work",
            color = "#FF5733",
            createdAt = "2024-01-15T10:30:00Z"
        )

        val category3 = TaskApiModel.Category(
            id = "cat_456",
            name = "Work",
            color = "#FF5733",
            createdAt = "2024-01-15T10:30:00Z"
        )

        assertEquals(category1, category2)
        assertNotEquals(category1, category3)
        assertEquals(category1.hashCode(), category2.hashCode())
        assertNotEquals(category1.hashCode(), category3.hashCode())
    }
} 