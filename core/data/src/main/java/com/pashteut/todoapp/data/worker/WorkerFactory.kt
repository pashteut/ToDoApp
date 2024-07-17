package com.pashteut.todoapp.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.pashteut.todoapp.data.TodoItemsRepository
import javax.inject.Inject

/**
 * Factory for creating instances of ListenableWorker.
 *
 * This factory is responsible for creating specific worker instances based on the class name provided. It supports
 * creating workers for syncing repository data. The factory is injected with a TodoItemsRepository
 * instance to be passed into worker for data operations.
 *
 * @property repository The TodoItemsRepository instance that will be used by workers for data operations.
 */

class MyWorkerFactory @Inject constructor(
    private val repository: TodoItemsRepository
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {

            SyncRepositoryWorker::class.java.name ->
                SyncRepositoryWorker(appContext, workerParameters, repository)

            else -> null
        }
    }
}