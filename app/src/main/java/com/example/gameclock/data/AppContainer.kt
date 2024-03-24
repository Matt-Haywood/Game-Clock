package com.example.gameclock.data

import android.content.Context
import com.example.gameclock.data.alarms.AlarmOfflineRepository
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.data.clockthemes.ClockThemePreferencesRepository
import com.example.gameclock.data.clockthemes.OfflineClockThemePreferencesRepository

interface AppContainer {
    val clockThemePreferencesRepository: ClockThemePreferencesRepository
    val alarmRepository: AlarmRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineClockThemePreferencesRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [clockThemePreferencesRepository]
     */
    override val clockThemePreferencesRepository: ClockThemePreferencesRepository by lazy {
        OfflineClockThemePreferencesRepository(ClockDatabase.getDatabase(context).clockDao())
    }

    /**
     * Implementation for [alarmRepository]
     */
    override val alarmRepository: AlarmRepository by lazy {
        AlarmOfflineRepository(ClockDatabase.getDatabase(context).alarmDao())
    }
}