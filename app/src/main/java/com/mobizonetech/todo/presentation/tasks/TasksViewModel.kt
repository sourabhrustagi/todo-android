package com.mobizonetech.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.domain.usecases.task.CompleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.CreateTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.DeleteTaskUseCase
import com.mobizonetech.todo.domain.usecases.task.GetTasksUseCase
import com.mobizonetech.todo.presentation.tasks.components.TaskFilterOption
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
    
    private val _allTasks = MutableStateFlow<List<Task>>(emptyList())
    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow(TaskFilterOption.ALL)

    fun loadTasks() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            getTasksUseCase().collect { result ->
                result.fold(
                    onSuccess = { tasks ->
                        _allTasks.value = tasks
                        _uiState.value = _uiState.value.copy(
                            tasks = filterAndSearchTasks(tasks, _searchQuery.value, _selectedFilter.value),
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
                        snackbarMessage = "Task created successfully"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        isCreatingTask = false,
                        error = exception.message ?: "Failed to create task"
                    )
                }
            )
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isUpdatingTask = true)
            
            completeTaskUseCase(taskId).fold(
                onSuccess = { task ->
                    // Reload tasks to show the updated task
                    loadTasks()
                    _uiState.value = _uiState.value.copy(
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
                        // Reload tasks to show the updated list
                        loadTasks()
                        _uiState.value = _uiState.value.copy(
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
        _searchQuery.value = query
        _uiState.value = _uiState.value.copy(
            tasks = filterAndSearchTasks(_allTasks.value, query, _selectedFilter.value)
        )
    }
    
    fun updateFilter(filter: TaskFilterOption) {
        _selectedFilter.value = filter
        _uiState.value = _uiState.value.copy(
            tasks = filterAndSearchTasks(_allTasks.value, _searchQuery.value, filter)
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
        searchQuery: String,
        filter: TaskFilterOption
    ): List<Task> {
        var filteredTasks = tasks
        
        // Apply search filter
        if (searchQuery.isNotBlank()) {
            filteredTasks = filteredTasks.filter { task ->
                task.title.contains(searchQuery, ignoreCase = true) ||
                (task.description?.contains(searchQuery, ignoreCase = true) == true)
            }
        }
        
        // Apply filter
        filteredTasks = when (filter) {
            TaskFilterOption.ALL -> filteredTasks
            TaskFilterOption.COMPLETED -> filteredTasks.filter { it.completed }
            TaskFilterOption.PENDING -> filteredTasks.filter { !it.completed }
            TaskFilterOption.HIGH_PRIORITY -> filteredTasks.filter { it.priority == TaskPriority.HIGH }
            TaskFilterOption.MEDIUM_PRIORITY -> filteredTasks.filter { it.priority == TaskPriority.MEDIUM }
            TaskFilterOption.LOW_PRIORITY -> filteredTasks.filter { it.priority == TaskPriority.LOW }
        }
        
        return filteredTasks
    }
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val isCreatingTask: Boolean = false,
    val isUpdatingTask: Boolean = false,
    val isDeletingTask: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
) 