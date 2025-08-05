package com.mobizonetech.todo.presentation.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Warning
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
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showLogoutConfirmation by remember { mutableStateOf(false) }

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
                        

                        
                        // Settings
                        ListItem(
                            headlineContent = { Text("Settings") },
                            supportingContent = { Text("Theme, notifications, and app preferences") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.Settings,
                                    contentDescription = "Settings",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            },
                            modifier = Modifier.clickable { onNavigateToSettings() }
                        )
                        
                        Divider()
                        
                        // Logout
                        ListItem(
                            headlineContent = { Text("Logout") },
                            supportingContent = { Text("Sign out of your account") },
                            leadingContent = {
                                Icon(
                                    imageVector = Icons.Default.ExitToApp,
                                    contentDescription = "Logout",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            },
                            modifier = Modifier.clickable { showLogoutConfirmation = true }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
        
        // Logout Confirmation Bottom Sheet
        if (showLogoutConfirmation) {
            ModalBottomSheet(
                onDismissRequest = { showLogoutConfirmation = false },
                containerColor = MaterialTheme.colorScheme.surface,
                dragHandle = { BottomSheetDefaults.DragHandle() }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Warning Icon
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Warning",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.error
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Title
                    Text(
                        text = "Logout",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    // Description
                    Text(
                        text = "Are you sure you want to logout? You'll need to log in again to access your tasks.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Cancel Button
                        OutlinedButton(
                            onClick = { showLogoutConfirmation = false },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        
                        // Logout Button
                        Button(
                            onClick = {
                                showLogoutConfirmation = false
                                onLogout()
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Logout")
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
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