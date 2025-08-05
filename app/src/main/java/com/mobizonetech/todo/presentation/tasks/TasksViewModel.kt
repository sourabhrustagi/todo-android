package com.mobizonetech.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.core.constants.AppConstants
import com.mobizonetech.todo.core.error.NetworkException
import com.mobizonetech.todo.core.error.TimeoutException
import com.mobizonetech.todo.core.error.ServerException
import com.mobizonetech.todo.core.error.ValidationException
import com.mobizonetech.todo.core.error.UnauthorizedException
import com.mobizonetech.todo.core.validation.ValidationUtils
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
                            tasks = filterAndSearchTasks(tasks, _uiState.value.searchQuery, _uiState.value.selectedPriority),
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
        // Validate input first
        val titleValidation = ValidationUtils.validateTaskTitle(title)
        val descriptionValidation = ValidationUtils.validateTaskDescription(description)
        
        if (!titleValidation.isSuccess()) {
            _uiState.value = _uiState.value.copy(
                error = titleValidation.getErrorMessage()
            )
            return
        }
        
        if (!descriptionValidation.isSuccess()) {
            _uiState.value = _uiState.value.copy(
                error = descriptionValidation.getErrorMessage()
            )
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isCreatingTask = true, error = null)
            
            createTaskUseCase(title, description, priority).fold(
                onSuccess = { _ ->
                    // Reload tasks to show the new task
                    loadTasks()
                    _uiState.value = _uiState.value.copy(
                        isCreatingTask = false,
                        snackbarMessage = AppConstants.SuccessMessages.TASK_CREATED,
                        error = null
                    )
                },
                onFailure = { exception ->
                    val errorMessage = when (exception) {
                        is NetworkException -> AppConstants.ErrorMessages.NETWORK_ERROR
                        is TimeoutException -> AppConstants.ErrorMessages.TIMEOUT_ERROR
                        is ServerException -> AppConstants.ErrorMessages.SERVER_ERROR
                        is UnauthorizedException -> AppConstants.ErrorMessages.UNAUTHORIZED_ERROR
                        is ValidationException -> exception.message ?: AppConstants.ErrorMessages.VALIDATION_ERROR
                        else -> "Failed to create task. Please try again"
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
        // Prevent multiple rapid clicks on the same task
        if (_uiState.value.updatingTaskId == taskId) {
            return
        }
        
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(updatingTaskId = taskId)
            
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
                        tasks = filterAndSearchTasks(currentAllTasks, _uiState.value.searchQuery, _uiState.value.selectedPriority),
                        updatingTaskId = null,
                        snackbarMessage = "Task updated successfully"
                    )
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        updatingTaskId = null,
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
                            tasks = filterAndSearchTasks(currentAllTasks, _uiState.value.searchQuery, _uiState.value.selectedPriority),
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
            tasks = filterAndSearchTasks(_uiState.value.allTasks, query, _uiState.value.selectedPriority)
        )
    }
    
    fun filterByPriority(priority: TaskPriority?) {
        _uiState.value = _uiState.value.copy(
            selectedPriority = priority,
            tasks = filterAndSearchTasks(_uiState.value.allTasks, _uiState.value.searchQuery, priority)
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
        priority: TaskPriority? = null
    ): List<Task> {
        var filteredTasks = tasks
        
        // Apply priority filter
        if (priority != null) {
            filteredTasks = filteredTasks.filter { task ->
                task.priority == priority
            }
        }
        
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
    val selectedPriority: TaskPriority? = null,
    val isLoading: Boolean = false,
    val isCreatingTask: Boolean = false,
    val updatingTaskId: String? = null,
    val isDeletingTask: Boolean = false,
    val error: String? = null,
    val snackbarMessage: String? = null
) 