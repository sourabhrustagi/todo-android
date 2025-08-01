package com.mobizonetech.todo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.todo.navigation.NavRoutes
import com.mobizonetech.todo.navigation.TodoNavGraph
import com.mobizonetech.todo.presentation.auth.AuthState
import com.mobizonetech.todo.presentation.auth.AuthStateManager
import com.mobizonetech.todo.ui.theme.TodoTheme
import com.mobizonetech.todo.util.ThemeManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoActivity : ComponentActivity() {
    
    @Inject
    lateinit var themeManager: ThemeManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkMode by themeManager.isDarkMode.collectAsState(initial = false)
            val navController = rememberNavController()
            
            TodoTheme(darkTheme = isDarkMode) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val authStateManager: AuthStateManager = hiltViewModel()
                    val authState by authStateManager.authState.collectAsState()
                    
                    LaunchedEffect(authState) {
                        val startDestination = when (authState) {
                            is AuthState.Authenticated -> NavRoutes.Main.route
                            is AuthState.Unauthenticated -> NavRoutes.Login.route
                            is AuthState.Loading -> NavRoutes.Login.route
                        }
                        
                        // Navigate to the appropriate destination
                        if (navController.currentDestination?.route != startDestination) {
                            navController.navigate(startDestination) {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    }
                    
                    TodoNavGraph(
                        navController = navController,
                        startDestination = NavRoutes.Login.route // Default, will be overridden by LaunchedEffect
                    )
                }
            }
        }
    }
}