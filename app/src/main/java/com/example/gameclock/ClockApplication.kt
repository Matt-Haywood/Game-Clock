package com.example.gameclock

import android.app.Application
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class ClockApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     * Hilt provides the AppContainer instance
     */

}