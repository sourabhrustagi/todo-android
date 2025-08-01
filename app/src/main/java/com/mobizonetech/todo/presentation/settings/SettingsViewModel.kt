package com.mobizonetech.todo.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mobizonetech.todo.util.ThemeManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themeManager: ThemeManager
) : ViewModel() {

    val isDarkMode: Flow<Boolean> = themeManager.isDarkMode

    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch {
            themeManager.setDarkMode(isDark)
        }
    }
} 