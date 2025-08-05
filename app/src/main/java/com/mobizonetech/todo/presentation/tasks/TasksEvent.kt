package com.mobizonetech.todo.presentation.tasks

import com.mobizonetech.todo.domain.models.Task

/**
 * Events for one-time actions in the Tasks screen
 * These are used for navigation, showing snackbars, and other one-time UI actions
 */
sealed class TasksEvent {
    data class NavigateToTaskDetail(val taskId: String) : TasksEvent()
    data class ShowSnackbar(val message: String) : TasksEvent()
    data class ShowError(val message: String) : TasksEvent()
    data class ShowDeleteConfirmation(val task: Task) : TasksEvent()
    data class HideDeleteConfirmation(val task: Task) : TasksEvent()
    data class ShowAddTaskDialog(val show: Boolean) : TasksEvent()
    data class TaskCreated(val task: Task) : TasksEvent()
    data class TaskDeleted(val taskId: String) : TasksEvent()
    data class TaskCompleted(val task: Task) : TasksEvent()
    data class TaskUpdated(val task: Task) : TasksEvent()
    object RefreshTasks : TasksEvent()
    object LoadMoreTasks : TasksEvent()
    object ClearError : TasksEvent()
    object ClearSnackbar : TasksEvent()
} 