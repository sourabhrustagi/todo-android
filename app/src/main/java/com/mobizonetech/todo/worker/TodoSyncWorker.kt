package com.mobizonetech.todo.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.mobizonetech.todo.domain.repository.TaskRepository
import com.mobizonetech.todo.util.ApiLogger
import com.mobizonetech.todo.util.SecurePreferences
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.time.Duration
import java.util.concurrent.TimeUnit

@HiltWorker
class TodoSyncWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskRepository: TaskRepository,
    private val securePreferences: SecurePreferences
) : CoroutineWorker(appContext, workerParams) {

    companion object {
        private const val WORK_NAME = "todo_sync_worker"
        private const val KEY_FORCE_SYNC = "force_sync"
        
        fun schedulePeriodicSync(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build()
            
            val syncWorkRequest = PeriodicWorkRequestBuilder<TodoSyncWorker>(
                repeatInterval = 2,
                repeatIntervalTimeUnit = TimeUnit.HOURS
            )
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .addTag("todo_sync")
                .build()
            
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
        }
        
        fun scheduleOneTimeSync(context: Context, forceSync: Boolean = false) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            
            val inputData = Data.Builder()
                .putBoolean(KEY_FORCE_SYNC, forceSync)
                .build()
            
            val syncWorkRequest = OneTimeWorkRequestBuilder<TodoSyncWorker>()
                .setConstraints(constraints)
                .setInputData(inputData)
                .addTag("todo_sync")
                .build()
            
            WorkManager.getInstance(context).enqueue(syncWorkRequest)
        }
        
        fun cancelAllSyncWork(context: Context) {
            WorkManager.getInstance(context).cancelAllWorkByTag("todo_sync")
        }
    }
    
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            ApiLogger.logApiCall("TODO_SYNC_WORKER", "Starting background sync")
            
            // Simulate some processing time
            delay(3000)
            
            // Check if user is logged in
            val isLoggedIn = securePreferences.getBoolean(SecurePreferences.KEY_IS_LOGGED_IN)
            if (!isLoggedIn) {
                ApiLogger.logApiSuccess("TODO_SYNC_WORKER", "User not logged in, skipping sync")
                return@withContext Result.success()
            }
            
            // Check if we should force sync
            val forceSync = inputData.getBoolean(KEY_FORCE_SYNC, false)
            
            // Get last sync time
            val lastSyncTime = securePreferences.getLong(SecurePreferences.KEY_LAST_SYNC_TIME)
            val currentTime = System.currentTimeMillis()
            
            // If not forced sync and last sync was less than 1 hour ago, skip
            if (!forceSync && (currentTime - lastSyncTime) < Duration.ofHours(1).toMillis()) {
                ApiLogger.logApiSuccess("TODO_SYNC_WORKER", "Last sync was recent, skipping")
                return@withContext Result.success()
            }
            
            // TODO: Implement actual sync logic when needed
            // For now, just update last sync time
            securePreferences.putLong(SecurePreferences.KEY_LAST_SYNC_TIME, currentTime)
            
            ApiLogger.logApiSuccess("TODO_SYNC_WORKER", "Background sync completed successfully")
            Result.success()
            
        } catch (e: Exception) {
            ApiLogger.logApiError("TODO_SYNC_WORKER", e)
            Result.retry()
        }
    }
} 