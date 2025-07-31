package com.mobizonetech.todo.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_preferences")

@Singleton
class ThemeManager @Inject constructor(
    private val context: Context
) {
    private val isDarkModeKey = booleanPreferencesKey("is_dark_mode")

    val isDarkMode: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[isDarkModeKey] ?: false
        }

    suspend fun setDarkMode(isDark: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[isDarkModeKey] = isDark
        }
    }
} 