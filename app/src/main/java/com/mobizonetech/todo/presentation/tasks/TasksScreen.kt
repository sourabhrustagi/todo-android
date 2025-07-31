package com.mobizonetech.todo.presentation.tasks

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mobizonetech.todo.presentation.common.LoadingSpinner
import com.mobizonetech.todo.presentation.common.ErrorView
import com.mobizonetech.todo.presentation.common.EmptyState
import com.mobizonetech.todo.presentation.tasks.components.TaskItem
import com.mobizonetech.todo.presentation.tasks.components.AddTaskDialog
import com.mobizonetech.todo.presentation.tasks.components.SearchBar
import com.mobizonetech.todo.presentation.tasks.components.TaskFilter
import com.mobizonetech.todo.presentation.tasks.components.TaskFilterOption

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TasksScreen(
    modifier: Modifier = Modifier,
    onNavigateToProfile: () -> Unit = {},
    onNavigateToFeedback: () -> Unit = {},
    viewModel: TasksViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddTaskDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var taskToDelete by remember { mutableStateOf<String?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(TaskFilterOption.ALL) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Todo App") },
                actions = {
                    IconButton(onClick = onNavigateToFeedback) {
                        Icon(Icons.Default.Info, contentDescription = "Feedback")
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(Icons.Default.Person, contentDescription = "Profile")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddTaskDialog = true }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingSpinner(
                        message = "Loading tasks...",
                        modifier = Modifier.align(Alignment.Center)
                    )
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
                                onQueryChange = { searchQuery = it }
                            )
                            
                            TaskFilter(
                                selectedFilter = selectedFilter,
                                onFilterChange = { selectedFilter = it }
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
                                    onTaskClick = { viewModel.toggleTaskCompletion(task.id) },
                                    onDeleteClick = { 
                                        taskToDelete = task.id
                                        showDeleteConfirmation = true
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        if (showAddTaskDialog) {
            AddTaskDialog(
                onDismiss = { showAddTaskDialog = false },
                onTaskAdded = { title, description ->
                    viewModel.createTask(title, description)
                    showAddTaskDialog = false
                }
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