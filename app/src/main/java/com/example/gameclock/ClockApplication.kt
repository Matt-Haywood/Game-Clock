package com.example.gameclock

import android.app.Application
import com.example.gameclock.data.AppContainer
import com.example.gameclock.data.AppDataContainer


class ClockApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}