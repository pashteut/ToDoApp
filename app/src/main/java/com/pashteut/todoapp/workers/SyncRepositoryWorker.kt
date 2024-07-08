package com.pashteut.todoapp.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.pashteut.todoapp.model.TodoItemsRepository
import java.util.concurrent.TimeUnit

/**
 * Worker responsible for pushing local repository changes to the remote server.
 *
 * This worker is triggered to run under specific conditions, such as when the device is connected to a network.
 * It attempts to push any pending changes from the local repository to the remote server. If the operation is
 * successful, it returns a success result. In case of failure, it retries the operation according to the
 * WorkManager's default retry policy.
 *
 * @param appContext The context of the application.
 * @param workerParams Parameters for the worker.
 * @property repository The repository instance where local changes are stored.
 */

class SyncRepositoryWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val repository: TodoItemsRepository,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        try {
            repository.syncChanges()
            return Result.success()
        }
        catch (e: Exception) {
            Log.e(TAG, "doWork: Error fetching repository", e)
            return Result.retry()
        }
    }
    companion object {
        const val TAG = "SyncRepositoryWorker"
        private const val DEFAULT_MIN_INTERVAL = 8L

        fun periodicWorkRequest(): PeriodicWorkRequest {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return PeriodicWorkRequestBuilder<SyncRepositoryWorker>(
                DEFAULT_MIN_INTERVAL,
                TimeUnit.HOURS
            ).setConstraints(constrains)
                .build()
        }

        fun oneTimeWorkRequest(): OneTimeWorkRequest {
            val constrains = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            return OneTimeWorkRequestBuilder<SyncRepositoryWorker>()
                .setConstraints(constrains)
                .build()
        }
    }
}