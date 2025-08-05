package com.mobizonetech.todo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mobizonetech.todo.presentation.auth.LoginScreen
import com.mobizonetech.todo.presentation.main.MainScreen
import com.mobizonetech.todo.presentation.tasks.AddTaskScreen
import com.mobizonetech.todo.presentation.tasks.TaskDetailScreen
import com.mobizonetech.todo.presentation.tasks.EditTaskScreen
import com.mobizonetech.todo.presentation.settings.SettingsScreen
import com.mobizonetech.todo.presentation.feedback.FeedbackScreen
import com.mobizonetech.todo.presentation.auth.AuthStateManager
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun TodoNavGraph(
    navController: NavHostController,
    startDestination: String = NavRoutes.Login.route
) {

    
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Login Screen
        composable(NavRoutes.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(NavRoutes.Main.route) {
                        popUpTo(NavRoutes.Login.route) { inclusive = true }
                    }
                }
            )
        }
        
        // Main App Screen (with bottom navigation)
        composable(NavRoutes.Main.route) {
            MainScreen(
                onLogout = {
                    navController.navigate(NavRoutes.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                parentNavController = navController
            )
        }
        
                // Add Task Screen
        composable(NavRoutes.AddTask.route) {
            AddTaskScreen(
                onBackClick = { navController.popBackStack() },
                onTaskAdded = { navController.popBackStack() }
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
        
        // Edit Task Screen
        composable(
            route = NavRoutes.EditTask.route,
            arguments = listOf(
                navArgument("taskId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getString("taskId") ?: ""
            EditTaskScreen(
                taskId = taskId,
                onBackClick = { navController.popBackStack() },
                onTaskUpdated = { navController.popBackStack() }
            )
        }
        
        // Settings Screen
        composable(NavRoutes.Settings.route) {
            SettingsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onNavigateToFeedback = {
                    navController.navigate(NavRoutes.Feedback.route) {
                        launchSingleTop = true
                    }
                }
            )
        }
        
        // Feedback Screen
        composable(NavRoutes.Feedback.route) {
            FeedbackScreen(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
} 