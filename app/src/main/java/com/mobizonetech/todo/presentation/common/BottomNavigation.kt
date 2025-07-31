package com.mobizonetech.todo.presentation.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobizonetech.todo.R
import com.mobizonetech.todo.navigation.NavRoutes

@Composable
fun BottomNavigation(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

    NavigationBar(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.List, contentDescription = stringResource(R.string.tasks)) },
            label = { Text(stringResource(R.string.tasks)) },
            selected = currentRoute == NavRoutes.Tasks.route,
            onClick = {
                if (currentRoute != NavRoutes.Tasks.route) {
                    navController.navigate(NavRoutes.Tasks.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Notifications, contentDescription = stringResource(R.string.feedback)) },
            label = { Text(stringResource(R.string.feedback)) },
            selected = currentRoute == NavRoutes.Feedback.route,
            onClick = {
                if (currentRoute != NavRoutes.Feedback.route) {
                    navController.navigate(NavRoutes.Feedback.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
        
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = stringResource(R.string.profile)) },
            label = { Text(stringResource(R.string.profile)) },
            selected = currentRoute == NavRoutes.Profile.route,
            onClick = {
                if (currentRoute != NavRoutes.Profile.route) {
                    navController.navigate(NavRoutes.Profile.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }
            }
        )
    }
} 