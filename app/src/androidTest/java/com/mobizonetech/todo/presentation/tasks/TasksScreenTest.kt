package com.mobizonetech.todo.presentation.tasks

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.presentation.tasks.TasksScreen
import com.mobizonetech.todo.presentation.tasks.TasksUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class TasksScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun tasksScreen_loadingState_showsProgressIndicator() {
        // Given
        val uiState = TasksUiState(isLoading = true)

        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithTag("loading_indicator").assertExists()
    }

    @Test
    fun tasksScreen_emptyState_showsEmptyMessage() {
        // Given
        val uiState = TasksUiState(tasks = emptyList())

        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithText("No tasks yet").assertExists()
        composeTestRule.onNodeWithText("Tap the + button to add your first task").assertExists()
    }

    @Test
    fun tasksScreen_errorState_showsErrorAndRetryButton() {
        // Given
        val errorMessage = "Network error occurred"
        val uiState = TasksUiState(error = errorMessage)

        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithText("Error: $errorMessage").assertExists()
        composeTestRule.onNodeWithText("Retry").assertExists()
    }

    @Test
    fun tasksScreen_withTasks_showsTaskList() {
        // Given
        val tasks = listOf(
            Task(
                id = "task_1",
                title = "Test Task 1",
                description = "Test Description 1",
                priority = TaskPriority.HIGH,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            ),
            Task(
                id = "task_2",
                title = "Test Task 2",
                description = "Test Description 2",
                priority = TaskPriority.MEDIUM,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithText("Test Task 1").assertExists()
        composeTestRule.onNodeWithText("Test Task 2").assertExists()
        composeTestRule.onNodeWithText("Test Description 1").assertExists()
        composeTestRule.onNodeWithText("Test Description 2").assertExists()
    }

    @Test
    fun tasksScreen_floatingActionButton_exists() {
        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Add Task").assertExists()
    }

    @Test
    fun tasksScreen_topBar_showsCorrectTitle() {
        // When
        composeTestRule.setContent {
            TasksScreen()
        }

        // Then
        composeTestRule.onNodeWithText("Todo App").assertExists()
    }
} 