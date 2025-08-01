package com.mobizonetech.todo.data.database

import com.mobizonetech.todo.data.database.dao.TaskDao
import com.mobizonetech.todo.data.database.entities.TaskEntity
import com.mobizonetech.todo.domain.models.TaskPriority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataSeeder @Inject constructor(
    private val taskDao: TaskDao
) {
    
    fun seedSampleData() {
        CoroutineScope(Dispatchers.IO).launch {
            // Check if data already exists
            val existingTasks = taskDao.getAllTasks().first()
            if (existingTasks.isNotEmpty()) {
                return@launch // Data already seeded
            }
            
            // Create sample tasks
            val sampleTasks = listOf(
                TaskEntity(
                    id = UUID.randomUUID().toString(),
                    title = "Complete Android Todo App",
                    description = "Implement all features including animations, WorkManager, and secure storage. Make sure to test all functionality thoroughly.",
                    priority = TaskPriority.HIGH.name,
                    categoryId = null,
                    dueDate = LocalDateTime.of(2024, 1, 15, 12, 0),
                    completed = false,
                    createdAt = LocalDateTime.now().minusDays(2),
                    updatedAt = LocalDateTime.now().minusDays(2)
                ),
                TaskEntity(
                    id = UUID.randomUUID().toString(),
                    title = "Implement Navigation",
                    description = "Set up proper navigation between screens using Jetpack Navigation Compose.",
                    priority = TaskPriority.HIGH.name,
                    categoryId = null,
                    dueDate = LocalDateTime.of(2024, 1, 12, 12, 0),
                    completed = false,
                    createdAt = LocalDateTime.now().minusDays(3),
                    updatedAt = LocalDateTime.now().minusDays(3)
                ),
                TaskEntity(
                    id = UUID.randomUUID().toString(),
                    title = "Add Animations",
                    description = "Implement smooth animations and transitions throughout the app for better user experience.",
                    priority = TaskPriority.MEDIUM.name,
                    categoryId = null,
                    dueDate = LocalDateTime.of(2024, 1, 14, 12, 0),
                    completed = false,
                    createdAt = LocalDateTime.now().minusDays(1),
                    updatedAt = LocalDateTime.now().minusDays(1)
                ),
                TaskEntity(
                    id = UUID.randomUUID().toString(),
                    title = "Prepare for Release",
                    description = "Finalize app for production release including app store assets and documentation.",
                    priority = TaskPriority.HIGH.name,
                    categoryId = null,
                    dueDate = LocalDateTime.of(2024, 1, 20, 12, 0),
                    completed = false,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
            
            // Insert sample tasks
            sampleTasks.forEach { task ->
                taskDao.insertTask(task)
            }
        }
    }
} 