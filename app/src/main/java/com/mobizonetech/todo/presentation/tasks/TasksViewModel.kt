package com.mobizonetech.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.models.Task
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
                            tasks = tasks,
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

    fun createTask(title: String, description: String?) {
        viewModelScope.launch {
            createTaskUseCase(title, description).fold(
                onSuccess = { task ->
                    // Reload tasks to show the new task
                    loadTasks()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to create task"
                    )
                }
            )
        }
    }

    fun toggleTaskCompletion(taskId: String) {
        viewModelScope.launch {
            completeTaskUseCase(taskId).fold(
                onSuccess = { task ->
                    // Reload tasks to show the updated task
                    loadTasks()
                },
                onFailure = { exception ->
                    _uiState.value = _uiState.value.copy(
                        error = exception.message ?: "Failed to toggle task completion"
                    )
                }
            )
        }
    }

    fun deleteTask(taskId: String) {
        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId).fold(
                    onSuccess = {
                        // Reload tasks to show the updated list
                        loadTasks()
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            error = exception.message ?: "Failed to delete task"
                        )
                    }
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Exception during delete: ${e.message}"
                )
            }
        }
    }
}

data class TasksUiState(
    val tasks: List<Task> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) 