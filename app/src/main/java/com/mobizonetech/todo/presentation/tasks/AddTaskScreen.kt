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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.presentation.common.FadeInAnimation
import com.mobizonetech.todo.presentation.common.ScaleInAnimation
import com.mobizonetech.todo.presentation.common.ShimmerText
import com.mobizonetech.todo.presentation.common.ShimmerButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    onBackClick: () -> Unit = {},
    onTaskAdded: () -> Unit = {},
    viewModel: TasksViewModel = hiltViewModel()
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var dueDate by remember { mutableStateOf("") }
    
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            // Show snackbar and navigate back on success
            if (message.contains("successfully")) {
                onTaskAdded()
            }
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add New Task") },
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
        FadeInAnimation {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title Section
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
                            if (uiState.isCreatingTask) {
                                // Shimmer loading for title section
                                ShimmerText(width = 0.6f, height = 24)
                                Spacer(modifier = Modifier.height(16.dp))
                                ShimmerText(width = 0.9f, height = 56)
                                Spacer(modifier = Modifier.height(16.dp))
                                ShimmerText(width = 0.9f, height = 120)
                            } else {
                                Text(
                                    text = "Task Details",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                OutlinedTextField(
                                    value = title,
                                    onValueChange = { title = it },
                                    label = { Text("Task Title *") },
                                    placeholder = { Text("Enter task title") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                        imeAction = ImeAction.Next
                                    ),
                                    leadingIcon = {
                                        Icon(Icons.Default.Create, contentDescription = "Title")
                                    }
                                )
                                
                                OutlinedTextField(
                                    value = description,
                                    onValueChange = { description = it },
                                    label = { Text("Description") },
                                    placeholder = { Text("Enter task description (optional)") },
                                    modifier = Modifier.fillMaxWidth(),
                                    minLines = 3,
                                    maxLines = 5,
                                    keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    leadingIcon = {
                                        Icon(Icons.Default.Create, contentDescription = "Description")
                                    }
                                )
                            }
                        }
                    }
                }
                
                // Priority Section
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
                            if (uiState.isCreatingTask) {
                                // Shimmer loading for priority section
                                ShimmerText(width = 0.4f, height = 24)
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    repeat(3) {
                                        ShimmerText(width = 0.3f, height = 32)
                                    }
                                }
                            } else {
                                Text(
                                    text = "Priority",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    TaskPriority.values().forEach { priority ->
                                        FilterChip(
                                            selected = selectedPriority == priority,
                                            onClick = { selectedPriority = priority },
                                            label = {
                                                Row(
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = when (priority) {
                                                            TaskPriority.HIGH -> Icons.Default.Warning
                                                            TaskPriority.MEDIUM -> Icons.Default.Notifications
                                                            TaskPriority.LOW -> Icons.Default.KeyboardArrowDown
                                                        },
                                                        contentDescription = priority.name,
                                                        modifier = Modifier.size(16.dp)
                                                    )
                                                    Text(priority.name.lowercase().capitalize())
                                                }
                                            },
                                            colors = FilterChipDefaults.filterChipColors(
                                                selectedContainerColor = when (priority) {
                                                    TaskPriority.HIGH -> MaterialTheme.colorScheme.errorContainer
                                                    TaskPriority.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
                                                    TaskPriority.LOW -> MaterialTheme.colorScheme.tertiaryContainer
                                                }
                                            ),
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                
                // Due Date Section
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
                            if (uiState.isCreatingTask) {
                                // Shimmer loading for due date section
                                ShimmerText(width = 0.5f, height = 24)
                                Spacer(modifier = Modifier.height(16.dp))
                                ShimmerText(width = 0.9f, height = 56)
                            } else {
                                Text(
                                    text = "Due Date (Optional)",
                                    style = MaterialTheme.typography.titleMedium
                                )
                                
                                OutlinedTextField(
                                    value = dueDate,
                                    onValueChange = { dueDate = it },
                                    label = { Text("Due Date") },
                                    placeholder = { Text("YYYY-MM-DD") },
                                    modifier = Modifier.fillMaxWidth(),
                                    singleLine = true,
                                    leadingIcon = {
                                        Icon(Icons.Default.DateRange, contentDescription = "Due Date")
                                    }
                                )
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
                        if (uiState.isCreatingTask) {
                            // Shimmer loading for buttons
                            ShimmerButton(modifier = Modifier.weight(1f))
                            ShimmerButton(modifier = Modifier.weight(1f))
                        } else {
                            OutlinedButton(
                                onClick = onBackClick,
                                modifier = Modifier.weight(1f),
                                enabled = !uiState.isCreatingTask
                            ) {
                                Text("Cancel")
                            }
                            
                            Button(
                                onClick = {
                                    if (title.isNotBlank()) {
                                        viewModel.createTask(
                                            title = title,
                                            description = description.takeIf { it.isNotBlank() },
                                            priority = selectedPriority
                                        )
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = title.isNotBlank() && !uiState.isCreatingTask
                            ) {
                                if (uiState.isCreatingTask) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(16.dp),
                                        strokeWidth = 2.dp,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Creating...")
                                } else {
                                    Icon(Icons.Default.Add, contentDescription = "Add")
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Add Task")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
} 