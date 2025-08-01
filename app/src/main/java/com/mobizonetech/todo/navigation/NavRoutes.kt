package com.mobizonetech.todo.navigation

sealed class NavRoutes(val route: String) {
    object Login : NavRoutes("login")
    object Main : NavRoutes("main")
    object Tasks : NavRoutes("tasks")
    object Profile : NavRoutes("profile")
    object Feedback : NavRoutes("feedback")
    object Settings : NavRoutes("settings")
    object TaskDetail : NavRoutes("task/{taskId}") {
        fun createRoute(taskId: String) = "task/$taskId"
    }
    object AddTask : NavRoutes("add_task")
    object EditTask : NavRoutes("edit_task/{taskId}") {
        fun createRoute(taskId: String) = "edit_task/$taskId"
    }
} 