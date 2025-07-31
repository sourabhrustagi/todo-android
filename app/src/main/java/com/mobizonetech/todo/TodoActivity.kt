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
import com.mobizonetech.todo.navigation.TodoNavGraph
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
                    TodoNavGraph(
                        navController = navController,
                        startDestination = "login"
                    )
                }
            }
        }
    }
}