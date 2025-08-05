package com.mobizonetech.todo.di

import com.mobizonetech.todo.domain.repository.AuthRepository
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import com.mobizonetech.todo.domain.repository.TaskRepository
import com.mobizonetech.todo.domain.usecases.auth.LoginUseCase
import com.mobizonetech.todo.domain.usecases.feedback.SubmitFeedbackUseCase
import com.mobizonetech.todo.domain.usecases.task.*
import com.mobizonetech.todo.domain.validation.TaskValidator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // Task Use Cases
    @Provides
    @Singleton
    fun provideGetTasksUseCase(
        repository: TaskRepository
    ): GetTasksUseCase = GetTasksUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateTaskUseCase(
        repository: TaskRepository,
        taskValidator: TaskValidator
    ): CreateTaskUseCase = CreateTaskUseCase(repository, taskValidator)

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

    // Auth Use Cases
    @Provides
    @Singleton
    fun provideLoginUseCase(
        repository: AuthRepository
    ): LoginUseCase = LoginUseCase(repository)

    // Feedback Use Cases
    @Provides
    @Singleton
    fun provideSubmitFeedbackUseCase(
        repository: FeedbackRepository
    ): SubmitFeedbackUseCase = SubmitFeedbackUseCase(repository)
} 