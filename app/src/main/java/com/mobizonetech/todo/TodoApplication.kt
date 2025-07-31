package com.mobizonetech.todo

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.mobizonetech.todo.data.database.DataSeeder
import com.mobizonetech.todo.util.ApiLogger
import com.mobizonetech.todo.worker.TodoSyncWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TodoApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory
    
    @Inject
    lateinit var dataSeeder: DataSeeder

    override fun onCreate() {
        super.onCreate()

        // Configure API Logger
        ApiLogger.configure(
            enabled = true, // Set to false in production if needed
            level = ApiLogger.LogLevel.DEBUG // Set to INFO or WARNING in production
        )

        // Seed sample data
        dataSeeder.seedSampleData()

        // Schedule background sync
        TodoSyncWorker.schedulePeriodicSync(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}