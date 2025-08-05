package com.mobizonetech.todo.presentation.tasks

import com.mobizonetech.todo.domain.models.SortOrder
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.models.TaskSortBy

/**
 * Enhanced UI state for Tasks screen with comprehensive state management
 */
data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val allTasks: List<Task> = emptyList(),
    val filteredTasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedPriority: TaskPriority? = null,
    val selectedCategory: String? = null,
    val sortBy: TaskSortBy = TaskSortBy.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC,
    val snackbarMessage: String? = null,
    val isCreatingTask: Boolean = false,
    val isDeletingTask: Boolean = false,
    val isCompletingTask: Boolean = false,
    val isUpdatingTask: Boolean = false,
    val selectedTaskId: String? = null,
    val updatingTaskId: String? = null,
    val showAddTaskDialog: Boolean = false,
    val showDeleteConfirmation: Boolean = false,
    val taskToDelete: Task? = null,
    val currentPage: Int = 1,
    val hasNextPage: Boolean = false,
    val isLoadingMore: Boolean = false
) {
    val isEmpty: Boolean
        get() = !isLoading && filteredTasks.isEmpty()

    val hasError: Boolean
        get() = error != null

    val isAnyLoading: Boolean
        get() = isLoading || isRefreshing || isCreatingTask || isDeletingTask || 
                isCompletingTask || isUpdatingTask || isLoadingMore

    fun copyWithLoading(loading: Boolean): TasksUiState {
        return copy(isLoading = loading, error = if (loading) null else error)
    }

    fun copyWithError(error: String?): TasksUiState {
        return copy(error = error, isLoading = false, isRefreshing = false)
    }

    fun copyWithSnackbar(message: String?): TasksUiState {
        return copy(snackbarMessage = message)
    }

    fun clearError(): TasksUiState {
        return copy(error = null)
    }

    fun clearSnackbar(): TasksUiState {
        return copy(snackbarMessage = null)
    }
} 