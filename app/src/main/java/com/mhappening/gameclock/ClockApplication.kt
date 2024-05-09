package com.mhappening.gameclock

import android.app.Application
import androidx.work.Configuration
import com.mhappening.gameclock.data.workManager.factory.WrapperWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class ClockApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: WrapperWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()
}

