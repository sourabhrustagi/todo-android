package com.mobizonetech.todo.data.api.models

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

object TaskApiModel {

    @JsonClass(generateAdapter = true)
    data class Task(
        val id: String,
        val title: String,
        val description: String? = null,
        val priority: String? = null,
        val category: Category? = null,
        val dueDate: String? = null,
        val completed: Boolean = false,
        val createdAt: String,
        val updatedAt: String
    )

    @JsonClass(generateAdapter = true)
    data class CreateTaskRequest(
        val title: String,
        val description: String? = null,
        val priority: String? = null,
        val categoryId: String? = null,
        val dueDate: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class UpdateTaskRequest(
        val title: String? = null,
        val description: String? = null,
        val priority: String? = null,
        val categoryId: String? = null,
        val dueDate: String? = null,
        val completed: Boolean? = null
    )

    @JsonClass(generateAdapter = true)
    data class TaskListResponse(
        val tasks: List<Task>,
        val pagination: Pagination
    )

    @JsonClass(generateAdapter = true)
    data class Pagination(
        val page: Int,
        val limit: Int,
        val total: Int,
        val totalPages: Int
    )

    @JsonClass(generateAdapter = true)
    data class Category(
        val id: String,
        val name: String,
        val color: String,
        val createdAt: String
    )

    @JsonClass(generateAdapter = true)
    data class CreateCategoryRequest(
        val name: String,
        val color: String
    )

    @JsonClass(generateAdapter = true)
    data class UpdateCategoryRequest(
        val name: String? = null,
        val color: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class BulkOperationRequest(
        val operation: String,
        val taskIds: List<String>,
        val categoryId: String? = null,
        val priority: String? = null
    )

    @JsonClass(generateAdapter = true)
    data class BulkOperationResponse(
        val updatedCount: Int,
        val message: String
    )

    @JsonClass(generateAdapter = true)
    data class SearchResponse(
        val tasks: List<Task>,
        val total: Int
    )

    @JsonClass(generateAdapter = true)
    data class AnalyticsResponse(
        val total: Int,
        val completed: Int,
        val pending: Int,
        val overdue: Int,
        val byPriority: Map<String, Int>,
        val byCategory: List<CategoryCount>,
        val completionRate: Double
    )

    @JsonClass(generateAdapter = true)
    data class CategoryCount(
        val category: String,
        val count: Int
    )
} 