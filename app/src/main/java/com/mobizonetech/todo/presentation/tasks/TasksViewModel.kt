package com.mobizonetech.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.usecases.task.CompleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.CreateTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.DeleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val completeTaskUseCase: CompleteTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TasksUiState())
    val uiState: StateFlow<TasksUiState> = _uiState.asStateFlow()
    


    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getTasksUseCase().collect { result ->
                result.fold(
                    onSuccess = { tasks ->
                        _uiState.value = _uiState.value.copy(
                            allTasks = tasks,
                            tasks = filterAndSearchTasks(tasks, _uiState.value.searchQuery),
                            isLoading = false,
                            error = null
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Unknown error occurred"
                        )
                    }
                )
            }
        }
    }

    fun createTask(title: String, description: String?, priority: TaskPriority = TaskPriority.MEDIUM) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingTask = true)
            
            createTaskUseCase(title, description, priority).fold(
                onSuccess = { task ->
                    // Reload tasks to show the new task
                    loadTasks()
                    _uiState.value = _uiState.value.copy(
                        isCreatingTask = false,
                        snackbarMessage = "Task created successfully",
                        error = null // Clear any previous errors
                    )
                },
                onFailure = { exception ->
                    val errorMessage = when {
                        exception.message?.contains("network", ignoreCase = true) == true -> 
                            "Network error. Please check your connection and try again."
                        exception.message?.contains("timeout", ignoreCase = true) == true -> 
                            "Request timed out. Please try again."
                        exception.message?.contains("server", ignoreCase = true) == true -> 
                            "Server error. Please try again later."
                        exception.message?.contains("unauthorized", ignoreCase = true) == true -> 
                            "Authentication error. Please log in again."
                        else -> exception.message ?: "Failed to create task. Please try again."
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        isCreatingTask = false,
                        error = errorMessage
                    )
                }
            )
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        // Prevent multiple rapid clicks
        if (_uiState.value.isUpdatingTask) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdatingTask = true)
            
            completeTaskUseCase(taskId).fold(
                onSuccess = { updatedTask ->
                    // Update only the specific task in the UI state
                    val currentAllTasks = _uiState.value.allTasks.toMutableList()
                    val currentFilteredTasks = _uiState.value.tasks.toMutableList()
                    
                    // Find and update the task in allTasks
                    val allTasksIndex = currentAllTasks.indexOfFirst { it.id == taskId }
                    if (allTasksIndex != -1) {
                        currentAllTasks[allTasksIndex] = updatedTask
                    }
                    
                    // Find and update the task in filtered tasks
                    val filteredTasksIndex = currentFilteredTasks.indexOfFirst { it.id == taskId }
                    if (filteredTasksIndex != -1) {
                        currentFilteredTasks[filteredTasksIndex] = updatedTask
                    }
                    
                    _uiState.value = _uiState.value.copy(
                        allTasks = currentAllTasks,
                        tasks = currentFilteredTasks,
                        isUpdatingTask = false,
                        snackbarMessage = "Task updated successfully"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isUpdatingTask = false,
                        error = exception.message ?: "Failed to toggle task completion"
                    )
                }
            )
        }
    }



    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isDeletingTask = true)
            
            try {
                deleteTaskUseCase(taskId).fold(
                    onSuccess = {
                        // Remove only the specific task from the UI state
                        val currentAllTasks = _uiState.value.allTasks.toMutableList()
                        val currentFilteredTasks = _uiState.value.tasks.toMutableList()
                        
                        // Remove the task from allTasks
                        currentAllTasks.removeAll { it.id == taskId }
                        
                        // Remove the task from filtered tasks
                        currentFilteredTasks.removeAll { it.id == taskId }
                        
                        _uiState.value = _uiState.value.copy(
                            allTasks = currentAllTasks,
                            tasks = currentFilteredTasks,
                            isDeletingTask = false,
                            snackbarMessage = "Task deleted successfully"
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isDeletingTask = false,
                            error = exception.message ?: "Failed to delete task"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isDeletingTask = false,
                    error = "Exception during delete: ${e.message}"
                )
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(
            searchQuery = query,
            tasks = filterAndSearchTasks(_uiState.value.allTasks, query)
        )
    }
    
    fun clearSnackbar() {
        _uiState.value = _uiState.value.copy(snackbarMessage = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    private fun filterAndSearchTasks(
        tasks: List<Task>,
        searchQuery: String
    ): List<Task> {
        var filteredTasks = tasks
        
        // Apply search filter
        if (searchQuery.isNotBlank()) {
            filteredTasks = filteredTasks.filter { task ->
                task.title.contains(searchQuery, ignoreCase = true) ||
                (task.description?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
        
        return filteredTasks
    }
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val allTasks: List<Task> = emptyList(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isCreatingTask: Boolean = false,
    val isUpdatingTask: Boolean = false,
    val isDeletingTask: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
) 