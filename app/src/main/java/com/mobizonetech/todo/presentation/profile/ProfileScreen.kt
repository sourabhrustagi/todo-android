package com.mobizonetech.todo.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.foundation.clickable
import com.mobizonetech.todo.presentation.common.ShimmerText
import com.mobizonetech.todo.presentation.common.ShimmerButton
import androidx.compose.ui.res.stringResource
import com.mobizonetech.todo.R
import com.mobizonetech.todo.presentation.profile.ProfileViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Profile Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (false) { // Replace isLoading with false since it doesn't exist in ViewModel
                        // Shimmer loading for profile header
                        ShimmerText(width = 0.3f, height = 64)
                        Spacer(modifier = Modifier.height(16.dp))
                        ShimmerText(width = 0.6f, height = 24)
                        Spacer(modifier = Modifier.height(8.dp))
                        ShimmerText(width = 0.8f, height = 16)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = uiState.userName ?: "User",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        
                        Text(
                            text = uiState.userEmail ?: "user@example.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Statistics Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (false) { // Replace isLoading with false since it doesn't exist in ViewModel
                        // Shimmer loading for statistics
                        ShimmerText(width = 0.4f, height = 24)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            repeat(3) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    ShimmerText(width = 0.2f, height = 24)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    ShimmerText(width = 0.3f, height = 20)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    ShimmerText(width = 0.4f, height = 14)
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Statistics",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatItem(
                                icon = Icons.Default.CheckCircle,
                                title = "Completed",
                                value = uiState.completedTasks.toString()
                            )
                            StatItem(
                                icon = Icons.Default.Person,
                                title = "Pending",
                                value = uiState.pendingTasks.toString()
                            )
                            StatItem(
                                icon = Icons.Default.List,
                                title = "Total",
                                value = uiState.totalTasks.toString()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Settings Section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    if (false) { // Replace isLoading with false since it doesn't exist in ViewModel
                        // Shimmer loading for settings
                        ShimmerText(width = 0.3f, height = 24)
                        Spacer(modifier = Modifier.height(16.dp))
                        repeat(3) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                ShimmerText(width = 0.1f, height = 24)
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    ShimmerText(width = 0.4f, height = 16)
                                    ShimmerText(width = 0.6f, height = 14)
                                }
                                Spacer(modifier = Modifier.weight(1f))
                                ShimmerText(width = 0.15f, height = 24)
                            }
                            if (it < 2) Spacer(modifier = Modifier.height(8.dp))
                        }
                    } else {
                        Text(
                            text = "Settings",
                            style = MaterialTheme.typography.titleMedium
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Feedback Option
                        ListItem(
                            headlineContent = { Text("Send Feedback") },
                            supportingContent = { Text("Help us improve the app") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Feedback",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.clickable { onNavigateToFeedback() }
                        )
                        
                        Divider()
                        
                        // Notifications
                        ListItem(
                            headlineContent = { Text("Notifications") },
                            supportingContent = { Text("Manage notification preferences") },
                            leadingContent = {
                                if (uiState.notificationsEnabled) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = stringResource(R.string.notifications_enabled),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = stringResource(R.string.notifications_disabled),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            trailingContent = {
                                Switch(
                                    checked = uiState.notificationsEnabled,
                                    onCheckedChange = { viewModel.toggleNotifications() }
                                )
                            }
                        )
                        
                        Divider()
                        
                        // Dark Mode
                        ListItem(
                            headlineContent = { Text("Dark Mode") },
                            supportingContent = { Text("Toggle dark/light theme") },
                            leadingContent = {
                                if (uiState.darkModeEnabled) {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = stringResource(R.string.dark_mode_enabled),
                                        tint = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Notifications,
                                        contentDescription = stringResource(R.string.light_mode_enabled),
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            },
                            trailingContent = {
                                Switch(
                                    checked = uiState.darkModeEnabled,
                                    onCheckedChange = { viewModel.toggleDarkMode() }
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleLarge
        )
        
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
} 