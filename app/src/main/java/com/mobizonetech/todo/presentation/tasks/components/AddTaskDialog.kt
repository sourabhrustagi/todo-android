package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.domain.models.TaskPriority

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (title: String, description: String?, priority: TaskPriority) -> Unit,
    isLoading: Boolean = false,
    error: String? = null
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var isTitleError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { Text("Add New Task") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                OutlinedTextField(
                    value = title,
                    onValueChange = { 
                        title = it
                        isTitleError = false
                    },
                    label = { Text("Task Title") },
                    isError = isTitleError,
                    supportingText = if (isTitleError) {
                        { Text("Title is required") }
                    } else null,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                )
                
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    enabled = !isLoading
                )
                
                // Priority Selection
                Text(
                    text = "Priority",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TaskPriority.values().forEach { priority ->
                        FilterChip(
                            selected = selectedPriority == priority,
                            onClick = { if (!isLoading) selectedPriority = priority },
                            label = { Text(priority.getDisplayName()) },
                            modifier = Modifier.weight(1f),
                            enabled = !isLoading
                        )
                    }
                }
                
                // Error Display
                if (error != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Column(
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = "Error",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (title.isBlank()) {
                        isTitleError = true
                        return@TextButton
                    }
                    if (!isLoading) {
                        onTaskAdded(title.trim(), description.trim().takeIf { it.isNotBlank() }, selectedPriority)
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text(if (isLoading) "Adding..." else "Add Task")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Cancel")
            }
        }
    )
} 

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun AddTaskDialogPreview() {
    AddTaskDialog(
        onDismiss = {},
        onTaskAdded = { _, _, _ -> }
    )
} 