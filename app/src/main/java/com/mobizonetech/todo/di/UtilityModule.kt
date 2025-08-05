package com.mobizonetech.todo.di

import android.content.Context
import androidx.work.WorkManager
import com.mobizonetech.todo.core.config.MockApiManager
import com.mobizonetech.todo.core.error.ErrorHandler
import com.mobizonetech.todo.data.repository.RetryManager
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
object UtilityModule {

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
    fun provideErrorHandler(
        @ApplicationContext context: Context
    ): ErrorHandler = ErrorHandler(context)

    @Provides
    @Singleton
    fun provideRetryManager(): RetryManager = RetryManager()

    @Provides
    @Singleton
    fun provideMockApiManager(
        @ApplicationContext context: Context
    ): MockApiManager = MockApiManager(context)
} 