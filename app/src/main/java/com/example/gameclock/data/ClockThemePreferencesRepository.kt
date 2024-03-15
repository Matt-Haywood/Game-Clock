package com.example.gameclock.data

import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow

interface ClockThemePreferencesRepository {

    suspend fun isDatabaseEmpty(): Boolean

    fun getAllClockThemePreferences(): Flow<List<ClockThemePreferences?>>
    fun getClockThemePreferences(appTheme: AppTheme): Flow<ClockThemePreferences?>

    suspend fun updateClockThemePreferences(clockThemePreferences: ClockThemePreferences)

    suspend fun writeClockThemePreferences(clockThemePreferences: ClockThemePreferences)
}