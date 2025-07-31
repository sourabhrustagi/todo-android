package com.mobizonetech.todo.di

import android.content.Context
import androidx.room.Room
import com.mobizonetech.todo.data.database.TodoDatabase
import com.mobizonetech.todo.data.database.dao.CategoryDao
import com.mobizonetech.todo.data.database.dao.FeedbackDao
import com.mobizonetech.todo.data.database.dao.TaskDao
import com.mobizonetech.todo.data.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideTodoDatabase(
        @ApplicationContext context: Context
    ): TodoDatabase {
        return Room.databaseBuilder(
            context,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTaskDao(database: TodoDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    @Singleton
    fun provideCategoryDao(database: TodoDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: TodoDatabase): UserDao {
        return database.userDao()
    }

    @Provides
    @Singleton
    fun provideFeedbackDao(database: TodoDatabase): FeedbackDao {
        return database.feedbackDao()
    }
} 