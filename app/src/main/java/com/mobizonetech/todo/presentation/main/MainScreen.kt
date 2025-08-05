package com.mobizonetech.todo.presentation.main

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mobizonetech.todo.navigation.NavRoutes
import com.mobizonetech.todo.presentation.common.BottomNavigation
import com.mobizonetech.todo.presentation.profile.ProfileScreen
import com.mobizonetech.todo.presentation.tasks.TasksScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit = {},
    parentNavController: NavController? = null
) {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            BottomNavigation(navController = navController)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NavRoutes.Tasks.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(NavRoutes.Tasks.route) {
                TasksScreen(
                    onNavigateToProfile = {
                        navController.navigate(NavRoutes.Profile.route)
                    },
                    onNavigateToTaskDetail = { taskId ->
                        parentNavController?.navigate(NavRoutes.TaskDetail.createRoute(taskId)) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            
            composable(NavRoutes.Profile.route) {
                ProfileScreen(
                    onNavigateToSettings = {
                        parentNavController?.navigate(NavRoutes.Settings.route) {
                            launchSingleTop = true
                        }
                    }
                )
            }
            

            

        }
    }
} 