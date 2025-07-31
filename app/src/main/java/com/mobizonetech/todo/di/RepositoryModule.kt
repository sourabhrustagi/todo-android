package com.mobizonetech.todo.di

import com.mobizonetech.todo.data.repository.AuthRepositoryImpl
import com.mobizonetech.todo.data.repository.FeedbackRepositoryImpl
import com.mobizonetech.todo.data.repository.TaskRepositoryImpl
import com.mobizonetech.todo.domain.repository.AuthRepository
import com.mobizonetech.todo.domain.repository.FeedbackRepository
import com.mobizonetech.todo.domain.repository.TaskRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindFeedbackRepository(
        feedbackRepositoryImpl: FeedbackRepositoryImpl
    ): FeedbackRepository
} 