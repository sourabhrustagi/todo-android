package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.domain.models.Task
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.completed,
                onCheckedChange = { onTaskClick() },
                modifier = Modifier.padding(end = 12.dp)
            )
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (!task.description.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                if (task.dueDate != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Due: ${task.dueDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"))}",
                        style = MaterialTheme.typography.bodySmall,
                        color = if (task.isOverdue()) MaterialTheme.colorScheme.error 
                               else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                if (task.category != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    AssistChip(
                        onClick = { },
                        label = { Text(task.category.name) },
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                }
            }
            
            IconButton(
                onClick = onDeleteClick,
                modifier = Modifier.size(48.dp),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete task",
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
} 

@Composable
@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
fun TaskItemPreview() {
    val sampleTask = com.mobizonetech.todo.domain.models.Task(
        id = "1",
        title = "Sample Task",
        description = "This is a sample task description for preview.",
        priority = com.mobizonetech.todo.domain.models.TaskPriority.HIGH,
        category = com.mobizonetech.todo.domain.models.Category(
            id = "cat1",
            name = "Work",
            color = "#FF5722",
            createdAt = java.time.LocalDateTime.now()
        ),
        dueDate = java.time.LocalDateTime.now().plusDays(1),
        completed = false,
        createdAt = java.time.LocalDateTime.now().minusDays(1),
        updatedAt = java.time.LocalDateTime.now()
    )
    TaskItem(
        task = sampleTask,
        onTaskClick = {},
        onDeleteClick = {}
    )
} 