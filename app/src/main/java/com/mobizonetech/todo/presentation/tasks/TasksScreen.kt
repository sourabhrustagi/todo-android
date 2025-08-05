package com.mobizonetech.todo.presentation.tasks

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.presentation.common.EmptyState
import com.mobizonetech.todo.presentation.common.ErrorView
import com.mobizonetech.todo.presentation.common.ShimmerFilterChips
import com.mobizonetech.todo.presentation.common.ShimmerSearchBar
import com.mobizonetech.todo.presentation.common.ShimmerTaskList
import com.mobizonetech.todo.presentation.tasks.components.AddTaskDialog
import com.mobizonetech.todo.presentation.tasks.components.SearchBar
import com.mobizonetech.todo.presentation.tasks.components.TaskItem
import com.mobizonetech.todo.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    viewModel: TasksViewModel = hiltViewModel(),
    onNavigateToTaskDetail: (String) -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<String?>(null) }
    var showMenu by remember { mutableStateOf(false) }

    // Handle snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "My Tasks",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Filter tasks"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        TaskPriority.values().forEach { priority ->
                            DropdownMenuItem(
                                text = { Text("Show ${priority.getDisplayName()} Priority") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (priority) {
                                            TaskPriority.HIGH -> Icons.Default.Warning
                                            TaskPriority.MEDIUM -> Icons.Default.Notifications
                                            TaskPriority.LOW -> Icons.Default.KeyboardArrowDown
                                        },
                                        contentDescription = null,
                                        tint = when (priority) {
                                            TaskPriority.HIGH -> HighPriorityColor
                                            TaskPriority.MEDIUM -> MediumPriorityColor
                                            TaskPriority.LOW -> LowPriorityColor
                                        }
                                    )
                                },
                                onClick = {
                                    viewModel.filterByPriority(priority)
                                    showMenu = false
                                }
                            )
                        }
                        
                        HorizontalDivider()
                        
                        DropdownMenuItem(
                            text = { Text("Show All Tasks") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.List,
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                viewModel.filterByPriority(null)
                                showMenu = false
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface,
                    actionIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            if (!uiState.isLoading && !uiState.isCreatingTask) {
                FloatingActionButton(
                    onClick = { 
                        showAddTaskDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
                modifier = Modifier.shadow(
                    elevation = 8.dp,
                    shape = CircleShape
                )
            ) {
                Icon(
                    Icons.Default.Add, 
                    contentDescription = "Add Task",
                    modifier = Modifier.size(24.dp)
                )
            }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = paddingValues.calculateStartPadding(LocalLayoutDirection.current),
                    end = paddingValues.calculateEndPadding(LocalLayoutDirection.current),
                    bottom = paddingValues.calculateBottomPadding(),
                    top = paddingValues.calculateTopPadding()
                )
        ) {
            when {
                uiState.isLoading -> {
                    // Enhanced Shimmer Loading State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    ) {
                        // Enhanced Shimmer Search and Filter Section
                        Column(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            ShimmerSearchBar()
                            ShimmerFilterChips()
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Enhanced Shimmer Task List
                        ShimmerTaskList(
                            itemCount = 6,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                uiState.error != null -> {
                    val error = uiState.error
                    ErrorView(
                        message = error ?: "Unknown error occurred",
                        onRetry = { viewModel.loadTasks() },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    )
                }
                uiState.tasks.isEmpty() && uiState.searchQuery.isBlank() && uiState.selectedPriority == null -> {
                    // Enhanced Empty State
                    EmptyState(
                        title = "No tasks yet",
                        message = "Tap the + button to add your first task",
                        actionText = "Add Task",
                        onAction = { showAddTaskDialog = true },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Enhanced Search and Filter Section
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            SearchBar(
                                query = uiState.searchQuery,
                                onQueryChange = { 
                                    viewModel.updateSearchQuery(it)
                                }
                            )
                            
                            // Priority Filter Indicator
                            if (uiState.selectedPriority != null) {
                                Surface(
                                    modifier = Modifier.fillMaxWidth(),
                                    color = when (uiState.selectedPriority) {
                                        TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.1f)
                                        TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.1f)
                                        TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.1f)
                                        null -> MaterialTheme.colorScheme.surfaceVariant
                                    },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(
                                        width = 1.dp,
                                        color = when (uiState.selectedPriority) {
                                            TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.3f)
                                            TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.3f)
                                            TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.3f)
                                            null -> MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                        }
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = when (uiState.selectedPriority) {
                                                TaskPriority.HIGH -> Icons.Default.Warning
                                                TaskPriority.MEDIUM -> Icons.Default.Notifications
                                                TaskPriority.LOW -> Icons.Default.KeyboardArrowDown
                                                null -> Icons.Default.List
                                            },
                                            contentDescription = null,
                                            tint = when (uiState.selectedPriority) {
                                                TaskPriority.HIGH -> HighPriorityColor
                                                TaskPriority.MEDIUM -> MediumPriorityColor
                                                TaskPriority.LOW -> LowPriorityColor
                                                null -> MaterialTheme.colorScheme.onSurfaceVariant
                                            },
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = "Showing ${uiState.selectedPriority?.getDisplayName()} Priority Tasks",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = when (uiState.selectedPriority) {
                                                TaskPriority.HIGH -> HighPriorityColor
                                                TaskPriority.MEDIUM -> MediumPriorityColor
                                                TaskPriority.LOW -> LowPriorityColor
                                                null -> MaterialTheme.colorScheme.onSurfaceVariant
                                            }
                                        )
                                        Spacer(modifier = Modifier.weight(1f))
                                        TextButton(
                                            onClick = { viewModel.filterByPriority(null) }
                                        ) {
                                            Text("Clear")
                                        }
                                    }
                                }
                            }
                        }
                        
                        // Enhanced Task List
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(uiState.tasks) { task ->
                                TaskItem(
                                    task = task,
                                    onTaskClick = { 
                                        onNavigateToTaskDetail(task.id)
                                    },
                                    onCheckboxClick = { 
                                        viewModel.toggleTaskCompletion(task.id)
                                    },
                                    onDeleteClick = { 
                                        taskToDelete = task.id
                                        showDeleteConfirmation = true
                                    },
                                    isLoading = uiState.updatingTaskId == task.id
                                )
                            }
                        }
                    }
                }
            }

            // Enhanced Add Task Dialog
            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { 
                        showAddTaskDialog = false
                        viewModel.clearError() // Clear error when dialog is dismissed
                    },
                    onTaskAdded = { title, description, priority ->
                        viewModel.createTask(title, description, priority)
                        // Dialog will be closed only after successful task creation
                    },
                    isLoading = uiState.isCreatingTask,
                    error = uiState.error,
                    onReset = {
                        // Form is reset automatically when task is created successfully
                    }
                )
            }

            // Enhanced Delete Confirmation Dialog
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { 
                        showDeleteConfirmation = false
                        taskToDelete = null
                    },
                    title = { 
                        Text(
                            "Delete Task",
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.SemiBold
                            )
                        ) 
                    },
                    text = { 
                        Text(
                            "Are you sure you want to delete this task? This action cannot be undone.",
                            style = MaterialTheme.typography.bodyMedium
                        ) 
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                taskToDelete?.let { taskId ->
                                    viewModel.deleteTask(taskId)
                                }
                                showDeleteConfirmation = false
                                taskToDelete = null
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.error
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        OutlinedButton(
                            onClick = { 
                                showDeleteConfirmation = false
                                taskToDelete = null
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cancel")
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }

    // Load tasks when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
    
    // Handle snackbar messages and dialog state
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            // Close dialog when task is successfully created (with a small delay)
            if (message.contains("created successfully") && showAddTaskDialog) {
                kotlinx.coroutines.delay(1500) // Show success message for 1.5 seconds
                showAddTaskDialog = false
            }
            
            // Refresh tasks when returning from other screens (e.g., after deletion from detail screen)
            if (message.contains("deleted successfully")) {
                viewModel.loadTasks()
            }
        }
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun TasksScreenPreview() {
    // Simple preview without complex ViewModel mocking
    TasksScreen(
        viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    )
} 