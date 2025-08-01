package com.mobizonetech.todo.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.todo.presentation.common.*
import com.mobizonetech.todo.presentation.tasks.components.TaskItem
import com.mobizonetech.todo.presentation.tasks.components.AddTaskDialog
import com.mobizonetech.todo.presentation.tasks.components.SearchBar
import com.mobizonetech.todo.presentation.tasks.components.TaskFilter
import com.mobizonetech.todo.presentation.tasks.components.TaskFilterOption
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    onNavigateToAddTask: () -> Unit = {},
    onNavigateToTaskDetail: (String) -> Unit = {},
    onLogout: () -> Unit = {},
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(TaskFilterOption.ALL) }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val fabTooltipState = rememberFabTooltipState()
    
    // Handle snackbar messages
    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            viewModel.clearSnackbar()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("My Tasks") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    var showMenu by remember { mutableStateOf(false) }
                    
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }
                        
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Filter by Priority") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Settings,
                                        contentDescription = null
                                    )
                                },
                                onClick = { showMenu = false }
                            )
                            
                            HorizontalDivider()
                            
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
                                        viewModel.updateFilter(
                                            when (priority) {
                                                TaskPriority.HIGH -> TaskFilterOption.HIGH_PRIORITY
                                                TaskPriority.MEDIUM -> TaskFilterOption.MEDIUM_PRIORITY
                                                TaskPriority.LOW -> TaskFilterOption.LOW_PRIORITY
                                            }
                                        )
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
                                    viewModel.updateFilter(TaskFilterOption.ALL)
                                    showMenu = false
                                }
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    showAddTaskDialog = true
                    fabTooltipState.hide()
                }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    // Shimmer Loading State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        // Shimmer Search and Filter Section
                        Column(
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            ShimmerSearchBar()
                            ShimmerFilterChips()
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Shimmer Task List
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
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.tasks.isEmpty() -> {
                    EmptyState(
                        title = "No tasks yet",
                        message = "Tap the + button to add your first task",
                        actionText = "Add Task",
                        onAction = { showAddTaskDialog = true },
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        // Search and Filter Section
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            SearchBar(
                                query = searchQuery,
                                onQueryChange = { 
                                    searchQuery = it
                                    viewModel.updateSearchQuery(it)
                                }
                            )
                            
                            TaskFilter(
                                selectedFilter = selectedFilter,
                                onFilterChange = { 
                                    selectedFilter = it
                                    viewModel.updateFilter(it)
                                }
                            )
                        }
                        
                        // Task List
                        LazyColumn(
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
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
                                    isLoading = uiState.isUpdatingTask
                                )
                            }
                        }
                    }
                }
            }

            // Add Task Dialog
            if (showAddTaskDialog) {
                AddTaskDialog(
                    onDismiss = { showAddTaskDialog = false },
                    onTaskAdded = { title, description, priority ->
                        viewModel.createTask(title, description, priority)
                        showAddTaskDialog = false
                    },
                    isLoading = uiState.isCreatingTask
                )
            }

            // Delete Confirmation Dialog
            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { 
                        showDeleteConfirmation = false
                        taskToDelete = null
                    },
                    title = { Text("Delete Task") },
                    text = { Text("Are you sure you want to delete this task? This action cannot be undone.") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                taskToDelete?.let { taskId ->
                                    viewModel.deleteTask(taskId)
                                }
                                showDeleteConfirmation = false
                                taskToDelete = null
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { 
                                showDeleteConfirmation = false
                                taskToDelete = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            
            // FAB Tooltip
            FabTooltip(
                visible = fabTooltipState.visible.value,
                onDismiss = { fabTooltipState.hide() }
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadTasks()
    }
}

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun TasksScreenPreview() {
    // Simple preview without complex ViewModel mocking
    TasksScreen(
        modifier = androidx.compose.ui.Modifier,
        viewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    )
} 