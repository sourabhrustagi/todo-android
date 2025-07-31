package com.mobizonetech.todo.presentation.tasks.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mobizonetech.todo.domain.models.TaskPriority

@Composable
fun TaskFilter(
    selectedFilter: TaskFilterOption,
    onFilterChange: (TaskFilterOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskFilterOption.values().forEach { filterOption ->
            FilterChip(
                selected = selectedFilter == filterOption,
                onClick = { onFilterChange(filterOption) },
                label = { Text(filterOption.displayName) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

enum class TaskFilterOption(val displayName: String) {
    ALL("All"),
    COMPLETED("Completed"),
    PENDING("Pending"),
    HIGH_PRIORITY("High Priority"),
    MEDIUM_PRIORITY("Medium Priority"),
    LOW_PRIORITY("Low Priority")
} 