package com.mobizonetech.todo.di

import com.mobizonetech.todo.data.database.DataSeeder
import com.mobizonetech.todo.data.database.dao.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeatureModule {

    @Provides
    @Singleton
    fun provideDataSeeder(taskDao: TaskDao): DataSeeder = DataSeeder(taskDao)
} 