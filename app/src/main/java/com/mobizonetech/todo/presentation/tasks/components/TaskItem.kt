package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.domain.models.Task
import com.mobizonetech.todo.domain.models.TaskPriority
import com.mobizonetech.todo.presentation.common.FadeInAnimation
import com.mobizonetech.todo.presentation.common.ScaleInAnimation
import com.mobizonetech.todo.ui.theme.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskItem(
    task: Task,
    onTaskClick: () -> Unit,
    onDeleteClick: () -> Unit,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier
) {
    var isVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isVisible = true
    }
    
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(
            initialOffsetY = { it / 2 },
            animationSpec = tween(300, easing = EaseOutCubic)
        ) + fadeIn(animationSpec = tween(300)) + scaleIn(
            initialScale = 0.8f,
            animationSpec = tween(300, easing = EaseOutBack)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(300, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300)) + scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(300, easing = EaseInBack)
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            ),
            elevation = CardDefaults.cardElevation(
                defaultElevation = if (task.priority == TaskPriority.HIGH) 4.dp else 2.dp
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Priority indicator
                Box(
                    modifier = Modifier
                        .size(4.dp, 40.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(
                            when (task.priority) {
                                TaskPriority.HIGH -> HighPriorityColor
                                TaskPriority.MEDIUM -> MediumPriorityColor
                                TaskPriority.LOW -> LowPriorityColor
                            }
                        )
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Checkbox or Loading indicator
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { onTaskClick() },
                        modifier = Modifier.size(24.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Task content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        color = if (task.completed) 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else 
                            MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    if (!task.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = if (task.completed) 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Task metadata
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Priority chip
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = when (task.priority) {
                                TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.1f)
                                TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.1f)
                                TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.1f)
                            }
                        ) {
                            Text(
                                text = task.priority.name.lowercase().capitalize(),
                                style = MaterialTheme.typography.labelSmall,
                                color = when (task.priority) {
                                    TaskPriority.HIGH -> HighPriorityColor
                                    TaskPriority.MEDIUM -> MediumPriorityColor
                                    TaskPriority.LOW -> LowPriorityColor
                                },
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.width(8.dp))
                        
                        // Due date if available
                        task.dueDate?.let { dueDate ->
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Due date",
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = dueDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                // Delete button
                if (!isLoading) {
                    IconButton(
                        onClick = onDeleteClick,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete task",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TaskItemPreview() {
    val sampleTask = Task(
        id = "1",
        title = "Complete Android Todo App",
        description = "Implement all features including animations, WorkManager, and secure storage",
        priority = TaskPriority.HIGH,
        completed = false,
        dueDate = LocalDateTime.of(2024, 1, 15, 12, 0),
        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
        updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
    )
    
    TaskItem(
        task = sampleTask,
        onTaskClick = {},
        onDeleteClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun TaskItemCompletedPreview() {
    val sampleTask = Task(
        id = "2",
        title = "Setup Project Structure",
        description = "Create basic project structure with MVVM architecture",
        priority = TaskPriority.MEDIUM,
        completed = true,
        dueDate = LocalDateTime.of(2024, 1, 10, 12, 0),
        createdAt = LocalDateTime.of(2024, 1, 1, 10, 0),
        updatedAt = LocalDateTime.of(2024, 1, 1, 10, 0)
    )
    
    TaskItem(
        task = sampleTask,
        onTaskClick = {},
        onDeleteClick = {}
    )
} 