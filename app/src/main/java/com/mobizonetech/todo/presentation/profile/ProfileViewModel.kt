package com.mobizonetech.todo.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.usecases.task.GetTasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getTasksUseCase().collect { result ->
                result.fold(
                    onSuccess = { tasks ->
                        val completedTasks = tasks.count { it.completed }
                        val pendingTasks = tasks.count { !it.completed }
                        
                        _uiState.value = _uiState.value.copy(
                            totalTasks = tasks.size,
                            completedTasks = completedTasks,
                            pendingTasks = pendingTasks
                        )
                    },
                    onFailure = { exception ->
                        // Handle error silently for now
                    }
                )
            }
        }
    }

    fun toggleNotifications() {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = !_uiState.value.notificationsEnabled
        )
    }

    fun toggleDarkMode() {
        _uiState.value = _uiState.value.copy(
            darkModeEnabled = !_uiState.value.darkModeEnabled
        )
    }
}

data class ProfileUiState(
    val userName: String? = "Demo User",
    val userEmail: String? = "demo@example.com",
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val notificationsEnabled: Boolean = true,
    val darkModeEnabled: Boolean = false
) 