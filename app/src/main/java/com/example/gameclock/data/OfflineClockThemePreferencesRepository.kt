package com.example.gameclock.data

import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class OfflineClockThemePreferencesRepository(private val clockDao: ClockDao) :
    ClockThemePreferencesRepository {

    override suspend fun isDatabaseEmpty(): Boolean =
        clockDao.getAllThemePreferences().first().isEmpty()

    override fun getAllClockThemePreferences(): Flow<List<ClockThemePreferences?>> =
        clockDao.getAllThemePreferences()

    override fun getClockThemePreferences(appTheme: AppTheme): Flow<ClockThemePreferences?> =
        clockDao.getPreferencesForTheme(appTheme = appTheme)

    override suspend fun updateClockThemePreferences(clockThemePreferences: ClockThemePreferences) =
        clockDao.update(clockThemePreferences)

    override suspend fun writeClockThemePreferences(clockThemePreferences: ClockThemePreferences) =
        clockDao.writeClockTheme(clockThemePreferences)
}