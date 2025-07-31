package com.mobizonetech.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.mobizonetech.todo.presentation.auth.LoginScreen
import com.mobizonetech.todo.presentation.auth.LoginMethod
import com.mobizonetech.todo.presentation.feedback.FeedbackScreen
import com.mobizonetech.todo.presentation.profile.ProfileScreen
import com.mobizonetech.todo.presentation.tasks.TasksScreen
import com.mobizonetech.todo.ui.theme.TodoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodoActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var isLoggedIn by remember { mutableStateOf(false) }
                    var isLoading by remember { mutableStateOf(false) }
                    var error by remember { mutableStateOf<String?>(null) }
                    var currentScreen by remember { mutableStateOf("tasks") }
                    
                    if (isLoggedIn) {
                        when (currentScreen) {
                            "tasks" -> {
                                TasksScreen(
                                    onNavigateToProfile = { currentScreen = "profile" },
                                    onNavigateToFeedback = { currentScreen = "feedback" }
                                )
                            }
                            "profile" -> {
                                ProfileScreen(
                                    onBackClick = { currentScreen = "tasks" },
                                    onNavigateToFeedback = { currentScreen = "feedback" },
                                    onLogout = { isLoggedIn = false }
                                )
                            }
                            "feedback" -> {
                                FeedbackScreen(
                                    onBackClick = { currentScreen = "tasks" }
                                )
                            }
                        }
                    } else {
                        LoginScreen(
                            onLogin = { loginMethod, identifier, password ->
                                isLoading = true
                                error = null
                                
                                // Simulate login process
                                // In a real app, you would call your login use case here
                                when (loginMethod) {
                                    LoginMethod.PHONE -> {
                                        // Handle phone login
                                        println("Phone login: $identifier, OTP: $password")
                                        // For demo purposes, accept any valid phone number
                                        if (identifier.matches(Regex("^\\+[1-9]\\d{1,14}$"))) {
                                            isLoggedIn = true
                                        } else {
                                            error = "Invalid phone number"
                                        }
                                    }
                                    LoginMethod.EMAIL -> {
                                        // Handle email login
                                        println("Email login: $identifier, Password: $password")
                                        // For demo purposes, accept any valid email
                                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(identifier).matches()) {
                                            isLoggedIn = true
                                        } else {
                                            error = "Invalid email or password"
                                        }
                                    }
                                }
                                isLoading = false
                            },
                            isLoading = isLoading,
                            error = error
                        )
                    }
                }
            }
        }
    }
}