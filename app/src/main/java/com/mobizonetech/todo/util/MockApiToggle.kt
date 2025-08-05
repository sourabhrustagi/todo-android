package com.mobizonetech.todo.util

import android.content.Context
import android.widget.Toast
import com.mobizonetech.todo.core.config.MockApiManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockApiToggle @Inject constructor(
    @ApplicationContext private val context: Context,
    private val mockApiManager: MockApiManager
) {
    
    /**
     * Toggle mock API on/off
     * Note: This requires app restart to take effect due to OkHttpClient being singleton
     */
    fun toggleMockApi() {
        val newState = !mockApiManager.isMockApiEnabled
        mockApiManager.setMockApiEnabled(newState)
        
        val message = if (newState) {
            "Mock API enabled. Restart app to apply changes."
        } else {
            "Mock API disabled. Restart app to apply changes."
        }
        
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
    
    /**
     * Enable mock API
     */
    fun enableMockApi() {
        mockApiManager.setMockApiEnabled(true)
        Toast.makeText(context, "Mock API enabled. Restart app to apply changes.", Toast.LENGTH_LONG).show()
    }
    
    /**
     * Disable mock API
     */
    fun disableMockApi() {
        mockApiManager.setMockApiEnabled(false)
        Toast.makeText(context, "Mock API disabled. Restart app to apply changes.", Toast.LENGTH_LONG).show()
    }
    
    /**
     * Reset to default environment setting
     */
    fun resetToDefault() {
        mockApiManager.resetToDefault()
        Toast.makeText(context, "Reset to default. Restart app to apply changes.", Toast.LENGTH_LONG).show()
    }
    
    /**
     * Get current mock API status
     */
    fun getMockApiStatus(): String {
        val status = mockApiManager.getMockApiStatus()
        return """
            Mock API Status:
            Enabled: ${status.isEnabled}
            Environment: ${status.environment}
            Has Override: ${status.hasOverride}
        """.trimIndent()
    }
    
    /**
     * Check if mock API is currently enabled
     */
    val isMockApiEnabled: Boolean
        get() = mockApiManager.isMockApiEnabled
} 