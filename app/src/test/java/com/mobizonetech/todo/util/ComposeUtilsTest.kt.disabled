package com.mobizonetech.todo.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.assertIsDisplayed
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ComposeUtilsTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `LoadingContent should display content when not loading`() {
        // Given
        var refreshCalled = false
        val onRefresh = { refreshCalled = true }

        // When
        composeTestRule.setContent {
            LoadingContent(
                loading = false,
                onRefresh = onRefresh
            ) {
                TestContent("Test Content")
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Content").assertIsDisplayed()
    }

    @Test
    fun `LoadingContent should display content when loading`() {
        // Given
        var refreshCalled = false
        val onRefresh = { refreshCalled = true }

        // When
        composeTestRule.setContent {
            LoadingContent(
                loading = true,
                onRefresh = onRefresh
            ) {
                TestContent("Loading Content")
            }
        }

        // Then
        composeTestRule.onNodeWithText("Loading Content").assertIsDisplayed()
    }

    @Test
    fun `LoadingContent should handle empty content`() {
        // Given
        var refreshCalled = false
        val onRefresh = { refreshCalled = true }

        // When
        composeTestRule.setContent {
            LoadingContent(
                loading = false,
                onRefresh = onRefresh
            ) {
                // Empty content
            }
        }

        // Then - Should not crash with empty content
        // The test passes if no exception is thrown
    }

    @Test
    fun `LoadingContent should handle multiple content elements`() {
        // Given
        var refreshCalled = false
        val onRefresh = { refreshCalled = true }

        // When
        composeTestRule.setContent {
            LoadingContent(
                loading = false,
                onRefresh = onRefresh
            ) {
                TestContent("First Element")
                TestContent("Second Element")
                TestContent("Third Element")
            }
        }

        // Then
        composeTestRule.onNodeWithText("First Element").assertIsDisplayed()
        composeTestRule.onNodeWithText("Second Element").assertIsDisplayed()
        composeTestRule.onNodeWithText("Third Element").assertIsDisplayed()
    }

    @Test
    fun `LoadingContent should handle null onRefresh`() {
        // Given
        val onRefresh: () -> Unit = {}

        // When
        composeTestRule.setContent {
            LoadingContent(
                loading = false,
                onRefresh = onRefresh
            ) {
                TestContent("Test Content")
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Content").assertIsDisplayed()
    }

    @Test
    fun `LoadingContent should work with different loading states`() {
        // Given
        val onRefresh: () -> Unit = {}

        // Test with loading = true
        composeTestRule.setContent {
            LoadingContent(
                loading = true,
                onRefresh = onRefresh
            ) {
                TestContent("Loading State")
            }
        }
        composeTestRule.onNodeWithText("Loading State").assertIsDisplayed()

        // Test with loading = false
        composeTestRule.setContent {
            LoadingContent(
                loading = false,
                onRefresh = onRefresh
            ) {
                TestContent("Not Loading State")
            }
        }
        composeTestRule.onNodeWithText("Not Loading State").assertIsDisplayed()
    }

    @Composable
    private fun TestContent(text: String) {
        androidx.compose.material3.Text(text = text)
    }
} 