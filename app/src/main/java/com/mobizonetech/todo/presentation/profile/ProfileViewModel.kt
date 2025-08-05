package com.mobizonetech.todo.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.domain.usecases.task.GetTasksUseCase
import com.mobizonetech.todo.util.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val themeManager: ThemeManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
        loadThemeState()
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
                    onFailure = { _ ->
                        // Handle error silently for now
                    }
                )
            }
        }
    }

    private fun loadThemeState() {
        viewModelScope.launch {
            themeManager.isDarkMode.collect { isDarkMode ->
                _uiState.value = _uiState.value.copy(
                    darkModeEnabled = isDarkMode
                )
            }
        }
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