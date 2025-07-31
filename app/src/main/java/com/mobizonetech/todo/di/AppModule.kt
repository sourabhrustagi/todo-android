package com.mobizonetech.todo.di

import android.content.Context
import androidx.work.WorkManager
import com.mobizonetech.todo.domain.repository.TaskRepository
import com.mobizonetech.todo.domain.usecases.task.*
import com.mobizonetech.todo.util.SecurePreferences
import com.mobizonetech.todo.util.ThemeManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideThemeManager(
        @ApplicationContext context: Context
    ): ThemeManager = ThemeManager(context)

    @Provides
    @Singleton
    fun provideSecurePreferences(
        @ApplicationContext context: Context
    ): SecurePreferences = SecurePreferences(context)

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context
    ): WorkManager = WorkManager.getInstance(context)

    @Provides
    @Singleton
    fun provideGetTasksUseCase(
        repository: TaskRepository
    ): GetTasksUseCase = GetTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateTaskUseCase(
        repository: TaskRepository
    ): CreateTaskUseCase = CreateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateTaskUseCase(
        repository: TaskRepository
    ): UpdateTaskUseCase = UpdateTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideDeleteTaskUseCase(
        repository: TaskRepository
    ): DeleteTaskUseCase = DeleteTaskUseCase(repository)

    @Provides
    @Singleton
    fun provideCompleteTaskUseCase(
        repository: TaskRepository
    ): CompleteTaskUseCase = CompleteTaskUseCase(repository)
} 