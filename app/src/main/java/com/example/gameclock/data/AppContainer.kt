package com.example.gameclock.data

import android.content.Context

interface AppContainer {
    val clockThemePreferencesRepository: ClockThemePreferencesRepository
}

/**
 * [AppContainer] implementation that provides instance of [OfflineClockThemePreferencesRepository]
 */
class AppDataContainer(private val context: Context) : AppContainer {
    /**
     * Implementation for [clockThemePreferencesRepository]
     */
    override val clockThemePreferencesRepository: ClockThemePreferencesRepository by lazy {
        OfflineClockThemePreferencesRepository(ClockPreferencesDatabase.getDatabase(context).clockDao())
    }
}