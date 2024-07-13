package com.pashteut.todoapp

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.pashteut.todoapp.data.worker.MyWorkerFactory
import com.pashteut.todoapp.data.worker.SyncRepositoryWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * The main Application class for the ToDo application.
 *
 * Initializes the application-wide settings and configurations, including setting up WorkManager with a custom WorkerFactory
 * for background tasks related to fetching and pushing repository data. It also monitors network connectivity changes to trigger
 * data synchronization tasks when the network becomes available.
 *
 * @property workerFactory Injected instance of [MyWorkerFactory] to provide custom workers for WorkManager.
 */

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: MyWorkerFactory

    override fun onCreate() {
        super.onCreate()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            SyncRepositoryWorker.TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            SyncRepositoryWorker.periodicWorkRequest()
        )

        val connectivityManager =
            applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        connectivityManager.registerDefaultNetworkCallback(object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                WorkManager
                    .getInstance(applicationContext)
                    .enqueue(SyncRepositoryWorker.oneTimeWorkRequest())
            }
        })
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}