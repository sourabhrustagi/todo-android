package com.mobizonetech.todo.presentation.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditTaskUiState())
    val uiState: StateFlow<EditTaskUiState> = _uiState.asStateFlow()

    fun loadTask(taskId: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            taskRepository.getTask(taskId).collect { result ->
                result.fold(
                    onSuccess = { task ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            task = task,
                            title = task.title,
                            description = task.description ?: ""
                        )
                    },
                    onFailure = { exception ->
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = exception.message ?: "Failed to load task"
                        )
                    }
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(title = title)
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(description = description)
    }

    fun saveTask(onSuccess: () -> Unit) {
        val currentState = _uiState.value
        val task = currentState.task ?: return
        
        if (currentState.title.isBlank()) {
            _uiState.value = currentState.copy(error = "Title cannot be empty")
            return
        }

        viewModelScope.launch {
            _uiState.value = currentState.copy(isSaving = true, error = null)
            
            taskRepository.updateTask(
                taskId = task.id,
                title = currentState.title,
                description = currentState.description.takeIf { it.isNotBlank() }
            ).fold(
                onSuccess = {
                    _uiState.value = currentState.copy(isSaving = false)
                    onSuccess()
                },
                onFailure = { exception ->
                    _uiState.value = currentState.copy(
                        isSaving = false,
                        error = exception.message ?: "Failed to update task"
                    )
                }
            )
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class EditTaskUiState(
    val isLoading: Boolean = false,
    val isSaving: Boolean = false,
    val task: Task? = null,
    val title: String = "",
    val description: String = "",
    val error: String? = null
) 