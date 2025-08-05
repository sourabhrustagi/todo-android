package com.mobizonetech.todo.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Main application module that includes all other modules.
 * This module serves as a central point for all dependency injection.
 */
@Module(
    includes = [
        NetworkModule::class,
        DatabaseModule::class,
        RepositoryModule::class,
        UseCaseModule::class,
        UtilityModule::class,
        FeatureModule::class
    ]
)
@InstallIn(SingletonComponent::class)
object AppModule {
    // This module now serves as a coordinator for all other modules
    // Individual dependencies are provided by their respective modules
} 