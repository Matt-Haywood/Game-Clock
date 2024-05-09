package com.mhappening.gameclock.data.clockthemes

import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow

interface ClockThemePreferencesRepository {

    suspend fun isDatabaseEmpty(): Boolean

    fun getAllClockThemePreferences(): Flow<List<ClockThemePreferences?>>
    fun getClockThemePreferences(appTheme: AppTheme): Flow<ClockThemePreferences?>

    suspend fun updateClockThemePreferences(clockThemePreferences: ClockThemePreferences)

    suspend fun writeClockThemePreferences(clockThemePreferences: ClockThemePreferences)
}