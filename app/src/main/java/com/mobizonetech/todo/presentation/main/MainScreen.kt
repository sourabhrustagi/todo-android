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
import com.mobizonetech.todo.presentation.feedback.FeedbackScreen
import com.mobizonetech.todo.presentation.profile.ProfileScreen
import com.mobizonetech.todo.presentation.settings.SettingsScreen
import com.mobizonetech.todo.presentation.tasks.TasksScreen
import com.mobizonetech.todo.presentation.tasks.TaskDetailScreen

@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
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
                    onNavigateToFeedback = {
                        navController.navigate(NavRoutes.Feedback.route)
                    },
                    onNavigateToAddTask = {
                        navController.navigate(NavRoutes.AddTask.route)
                    },
                    onNavigateToTaskDetail = { taskId ->
                        navController.navigate(NavRoutes.TaskDetail.createRoute(taskId))
                    },
                    onLogout = onLogout
                )
            }
            
            composable(NavRoutes.Profile.route) {
                ProfileScreen(
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onNavigateToFeedback = {
                        navController.navigate(NavRoutes.Feedback.route)
                    },
                    onNavigateToSettings = {
                        navController.navigate(NavRoutes.Settings.route)
                    },
                    onLogout = onLogout
                )
            }
            
            composable(NavRoutes.Feedback.route) {
                FeedbackScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            
            composable(NavRoutes.Settings.route) {
                SettingsScreen(
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
            
            // Task Detail Screen
            composable(
                route = NavRoutes.TaskDetail.route,
                arguments = listOf(
                    navArgument("taskId") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
                TaskDetailScreen(
                    taskId = taskId,
                    onBackClick = { navController.popBackStack() },
                    onEditTask = { taskId ->
                        navController.navigate(NavRoutes.EditTask.createRoute(taskId))
                    }
                )
            }
        }
    }
} 