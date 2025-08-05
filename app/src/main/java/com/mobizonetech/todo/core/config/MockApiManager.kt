package com.mobizonetech.todo.core.config

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    companion object {
        private const val PREF_NAME = "mock_api_preferences"
        private const val KEY_MOCK_API_ENABLED = "mock_api_enabled"
        private const val KEY_MOCK_API_OVERRIDE = "mock_api_override"
    }
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    
    /**
     * Check if mock API is currently enabled
     * This considers both the default environment setting and any user override
     */
    val isMockApiEnabled: Boolean
        get() {
            val hasOverride = sharedPreferences.getBoolean(KEY_MOCK_API_OVERRIDE, false)
            return if (hasOverride) {
                sharedPreferences.getBoolean(KEY_MOCK_API_ENABLED, ApiConfig.useMockApi)
            } else {
                ApiConfig.useMockApi
            }
        }
    
    /**
     * Enable or disable mock API with override
     * This will override the default environment setting
     */
    fun setMockApiEnabled(enabled: Boolean, override: Boolean = true) {
        sharedPreferences.edit {
            putBoolean(KEY_MOCK_API_ENABLED, enabled)
            putBoolean(KEY_MOCK_API_OVERRIDE, override)
        }
        
        // Also update the ApiConfig for immediate effect
        ApiConfig.setMockApiEnabled(enabled)
    }
    
    /**
     * Toggle mock API state
     */
    fun toggleMockApi() {
        val newState = !isMockApiEnabled
        setMockApiEnabled(newState)
    }
    
    /**
     * Reset to default environment setting
     */
    fun resetToDefault() {
        sharedPreferences.edit {
            putBoolean(KEY_MOCK_API_OVERRIDE, false)
        }
    }
    
    /**
     * Get current mock API status information
     */
    fun getMockApiStatus(): MockApiStatus {
        val hasOverride = sharedPreferences.getBoolean(KEY_MOCK_API_OVERRIDE, false)
        val currentState = sharedPreferences.getBoolean(KEY_MOCK_API_ENABLED, ApiConfig.useMockApi)
        
        return MockApiStatus(
            isEnabled = isMockApiEnabled,
            hasOverride = hasOverride,
            defaultState = ApiConfig.useMockApi,
            currentState = currentState,
            environment = ApiConfig.currentEnvironment
        )
    }
    
    /**
     * Get debug information about mock API configuration
     */
    fun getDebugInfo(): String {
        val status = getMockApiStatus()
        return """
            Mock API Configuration:
            - Environment: ${status.environment}
            - Default State: ${status.defaultState}
            - Has Override: ${status.hasOverride}
            - Current State: ${status.currentState}
            - Is Enabled: ${status.isEnabled}
            - Base URL: ${ApiConfig.baseUrl}
        """.trimIndent()
    }
}

data class MockApiStatus(
    val isEnabled: Boolean,
    val hasOverride: Boolean,
    val defaultState: Boolean,
    val currentState: Boolean,
    val environment: ApiConfig.Environment
) 