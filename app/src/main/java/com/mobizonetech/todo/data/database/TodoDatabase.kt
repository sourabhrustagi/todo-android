package com.mobizonetech.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobizonetech.todo.data.database.dao.CategoryDao
import com.mobizonetech.todo.data.database.dao.FeedbackDao
import com.mobizonetech.todo.data.database.dao.TaskDao
import com.mobizonetech.todo.data.database.dao.UserDao
import com.mobizonetech.todo.data.database.entities.CategoryEntity
import com.mobizonetech.todo.data.database.entities.FeedbackEntity
import com.mobizonetech.todo.data.database.entities.TaskEntity
import com.mobizonetech.todo.data.database.entities.UserEntity

@Database(
    entities = [
        TaskEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        FeedbackEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun feedbackDao(): FeedbackDao
} 