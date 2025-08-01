package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.border
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
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
    onCheckboxClick: () -> Unit,
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
            animationSpec = tween(300, easing = EaseOutCubic)
        ),
        exit = slideOutVertically(
            targetOffsetY = { -it / 2 },
            animationSpec = tween(300, easing = EaseInCubic)
        ) + fadeOut(animationSpec = tween(300)) + scaleOut(
            targetScale = 0.8f,
            animationSpec = tween(300, easing = EaseInCubic)
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .clickable { onTaskClick() }
                .shadow(
                    elevation = if (task.priority == TaskPriority.HIGH) 8.dp else 4.dp,
                    shape = RoundedCornerShape(16.dp),
                    ambientColor = when (task.priority) {
                        TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.3f)
                        TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.3f)
                        TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.3f)
                    }
                )
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
                defaultElevation = 0.dp
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Enhanced Priority indicator with gradient
                Box(
                    modifier = Modifier
                        .size(8.dp, 48.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(
                            when (task.priority) {
                                TaskPriority.HIGH -> Brush.verticalGradient(
                                    colors = listOf(HighPriorityColor, HighPriorityColor.copy(alpha = 0.7f))
                                )
                                TaskPriority.MEDIUM -> Brush.verticalGradient(
                                    colors = listOf(MediumPriorityColor, MediumPriorityColor.copy(alpha = 0.7f))
                                )
                                TaskPriority.LOW -> Brush.verticalGradient(
                                    colors = listOf(LowPriorityColor, LowPriorityColor.copy(alpha = 0.7f))
                                )
                            }
                        )
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Enhanced Checkbox or Loading indicator
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(28.dp),
                        strokeWidth = 3.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Checkbox(
                        checked = task.completed,
                        onCheckedChange = { onCheckboxClick() },
                        modifier = Modifier.size(28.dp),
                        colors = CheckboxDefaults.colors(
                            checkedColor = MaterialTheme.colorScheme.primary,
                            uncheckedColor = MaterialTheme.colorScheme.outline,
                            checkmarkColor = MaterialTheme.colorScheme.surface
                        )
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Enhanced Task content
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (task.completed) androidx.compose.ui.text.font.FontWeight.Normal else androidx.compose.ui.text.font.FontWeight.Medium
                        ),
                        textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (task.completed) 
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        else 
                            MaterialTheme.colorScheme.onSurface,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    if (!task.description.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                textDecoration = if (task.completed) TextDecoration.LineThrough else TextDecoration.None
                            ),
                            color = if (task.completed) 
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                            else 
                                MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // Enhanced Task metadata
                    Row(
                        modifier = Modifier.padding(top = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Enhanced Priority chip
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = when (task.priority) {
                                TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.1f)
                                TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.1f)
                                TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.1f)
                            },
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = when (task.priority) {
                                    TaskPriority.HIGH -> HighPriorityColor.copy(alpha = 0.3f)
                                    TaskPriority.MEDIUM -> MediumPriorityColor.copy(alpha = 0.3f)
                                    TaskPriority.LOW -> LowPriorityColor.copy(alpha = 0.3f)
                                }
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (task.priority) {
                                        TaskPriority.HIGH -> Icons.Default.Warning
                                        TaskPriority.MEDIUM -> Icons.Default.Notifications
                                        TaskPriority.LOW -> Icons.Default.KeyboardArrowDown
                                    },
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = when (task.priority) {
                                        TaskPriority.HIGH -> HighPriorityColor
                                        TaskPriority.MEDIUM -> MediumPriorityColor
                                        TaskPriority.LOW -> LowPriorityColor
                                    }
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = task.priority.getDisplayName(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when (task.priority) {
                                        TaskPriority.HIGH -> HighPriorityColor
                                        TaskPriority.MEDIUM -> MediumPriorityColor
                                        TaskPriority.LOW -> LowPriorityColor
                                    }
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        // Enhanced Due date if available
                        task.dueDate?.let { dueDate ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Due date",
                                    modifier = Modifier.size(14.dp),
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = dueDate.format(DateTimeFormatter.ofPattern("MMM dd")),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                // Enhanced Delete button
                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete task",
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f),
                        modifier = Modifier.size(20.dp)
                    )
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
        onCheckboxClick = {},
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
        onCheckboxClick = {},
        onDeleteClick = {}
    )
} 