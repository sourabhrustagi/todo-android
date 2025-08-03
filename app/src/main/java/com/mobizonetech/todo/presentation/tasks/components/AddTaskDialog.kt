package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onTaskAdded: (title: String, description: String?, priority: TaskPriority) -> Unit,
    isLoading: Boolean = false,
    error: String? = null,
    onReset: () -> Unit = {}
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var selectedPriority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var isTitleError by remember { mutableStateOf(false) }
    
    // Reset form when task creation is successful (isLoading changes from true to false and no error)
    LaunchedEffect(isLoading, error) {
        if (!isLoading && title.isNotBlank() && error == null) {
            // Task creation completed successfully, reset form
            title = ""
            description = ""
            selectedPriority = TaskPriority.MEDIUM
            isTitleError = false
            onReset()
        }
    }

    AlertDialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        title = { 
            Text(
                "Add New Task",
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            ) 
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
                
                // Enhanced Title Field
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
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                // Enhanced Description Field
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description (Optional)") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    maxLines = 5,
                    enabled = !isLoading,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                
                // Enhanced Priority Selection
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Priority",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Medium
                        ),
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
                                            modifier = Modifier.size(16.dp),
                                            tint = when (priority) {
                                                TaskPriority.HIGH -> HighPriorityColor
                                                TaskPriority.MEDIUM -> MediumPriorityColor
                                                TaskPriority.LOW -> LowPriorityColor
                                            }
                                        )
                                        Text(priority.getDisplayName())
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                enabled = !isLoading,
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = when (priority) {
                                        TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.1f)
                                        TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.1f)
                                        TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.1f)
                                    },
                                    selectedLabelColor = when (priority) {
                                        TaskPriority.HIGH -> HighPriorityColor
                                        TaskPriority.MEDIUM -> MediumPriorityColor
                                        TaskPriority.LOW -> LowPriorityColor
                                    }
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }
                
                // Enhanced Error Display
                if (error != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = ErrorColor.copy(alpha = 0.1f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            width = 1.dp,
                            color = ErrorColor.copy(alpha = 0.3f)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = ErrorColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = error,
                                style = MaterialTheme.typography.bodyMedium,
                                color = ErrorColor
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isBlank()) {
                        isTitleError = true
                        return@Button
                    }
                    onTaskAdded(title, description.takeIf { it.isNotBlank() }, selectedPriority)
                },
                enabled = !isLoading && title.isNotBlank(),
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.surface
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Adding...")
                } else {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add task",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add Task")
                }
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Cancel")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface,
        shape = RoundedCornerShape(20.dp)
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