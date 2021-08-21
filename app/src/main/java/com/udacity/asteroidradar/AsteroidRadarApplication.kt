package com.udacity.asteroidradar

import android.app.Application
import android.os.Build
import androidx.work.*
import com.udacity.asteroidradar.workers.DeleteDataWorker
import com.udacity.asteroidradar.workers.RefreshDataWorker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * @author Kulbaka Nataly
 * @date 21.08.2021
 */
class AsteroidRadarApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        delayedInit()
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setRequiresDeviceIdle(true)
                }
            }.build()

        val repeatingRefreshRequest = PeriodicWorkRequestBuilder<RefreshDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            RefreshDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRefreshRequest
        )

        val repeatingDeleteRequest = PeriodicWorkRequestBuilder<DeleteDataWorker>(
            1,
            TimeUnit.DAYS
        ).setConstraints(constraints)
            .build()
        WorkManager.getInstance().enqueueUniquePeriodicWork(
            DeleteDataWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingDeleteRequest
        )
    }
}