package com.mobizonetech.todo.presentation.tasks

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.presentation.common.FadeInAnimation
import com.mobizonetech.todo.presentation.common.ScaleInAnimation
import com.mobizonetech.todo.presentation.common.ShimmerTaskItem
import com.mobizonetech.todo.ui.theme.*
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDetailScreen(
    taskId: String,
    onBackClick: () -> Unit = {},
    onEditTask: (String) -> Unit = {},
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val task = uiState.tasks.find { it.id == taskId }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    
    // Always refresh tasks when the screen is displayed
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
    
    // Refresh task data when completion state changes
    LaunchedEffect(uiState.updatingTaskId) {
        if (uiState.updatingTaskId == null) {
            // Reload tasks to get updated completion state
            viewModel.loadTasks()
        }
    }
    
    // Navigate back after successful deletion
    LaunchedEffect(uiState.snackbarMessage) {
        if (uiState.snackbarMessage?.contains("deleted successfully") == true) {
            onBackClick()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Task Details") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        if (task == null) {
            // Shimmer Loading State
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Shimmer Task Item
                ShimmerTaskItem(
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Shimmer Additional Details
                repeat(2) {
                    ShimmerTaskItem(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        } else {
            FadeInAnimation {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Task Header
                    ScaleInAnimation {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                                        ),
                                        color = if (task.completed) 
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        else 
                                            MaterialTheme.colorScheme.onSurface
                                    )
                                    
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        if (uiState.updatingTaskId == task.id) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(16.dp),
                                                strokeWidth = 2.dp,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                        
                                        Checkbox(
                                            checked = task.completed,
                                            onCheckedChange = { viewModel.toggleTaskCompletion(task.id) },
                                            enabled = uiState.updatingTaskId != task.id,
                                            colors = CheckboxDefaults.colors(
                                                checkedColor = MaterialTheme.colorScheme.primary
                                            )
                                        )
                                    }
                                }
                                
                                if (!task.description.isNullOrBlank()) {
                                    Text(
                                        text = task.description,
                                        style = MaterialTheme.typography.bodyLarge.copy(
                                            textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                                        ),
                                        color = if (task.completed) 
                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                        else 
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                    
                    // Task Metadata
                    ScaleInAnimation {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Text(
                                    text = "Task Information",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                // Priority
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = when (task.priority) {
                                            TaskPriority.HIGH -> Icons.Default.Warning
                                            TaskPriority.MEDIUM -> Icons.Default.Notifications
                                            TaskPriority.LOW -> Icons.Default.KeyboardArrowDown
                                        },
                                        contentDescription = "Priority",
                                        tint = when (task.priority) {
                                            TaskPriority.HIGH -> HighPriorityColor
                                            TaskPriority.MEDIUM -> MediumPriorityColor
                                            TaskPriority.LOW -> LowPriorityColor
                                        }
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Priority",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = task.priority.name.lowercase().capitalize(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                
                                // Due Date
                                task.dueDate?.let { dueDate ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.DateRange,
                                            contentDescription = "Due Date",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = "Due Date",
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                            )
                                            Text(
                                                text = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                                
                                // Created Date
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Created",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Created",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = task.createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                                
                                // Last Updated
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Last Updated",
                                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Last Updated",
                                            style = MaterialTheme.typography.labelMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = task.updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                    
                    // Action Buttons
                    ScaleInAnimation {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { showDeleteConfirmation = true },
                                modifier = Modifier.weight(1f),
                                enabled = !uiState.isDeletingTask,
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = MaterialTheme.colorScheme.error
                                )
                            ) {
                                if (uiState.isDeletingTask) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(18.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Deleting...")
                                } else {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Delete")
                                }
                            }
                            
                            Button(
                                onClick = { onEditTask(task.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit")
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Edit")
                            }
                        }
                    }
                }
            }
        }
        
        // Delete Confirmation Dialog
        if (showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteConfirmation = false },
                title = {
                    Text("Delete Task")
                },
                text = {
                    Text("Are you sure you want to delete this task? This action cannot be undone.")
                },
                confirmButton = {
                    Button(
                        onClick = {
                            task?.let { viewModel.deleteTask(it.id) }
                            showDeleteConfirmation = false
                            // Let LaunchedEffect handle navigation after successful deletion
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Delete")
                    }
                },
                dismissButton = {
                    OutlinedButton(
                        onClick = { showDeleteConfirmation = false }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
} 