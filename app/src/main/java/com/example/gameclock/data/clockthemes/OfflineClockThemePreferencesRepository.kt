package com.example.gameclock.data.clockthemes

import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OfflineClockThemePreferencesRepository @Inject constructor(private val clockDao: ClockDao) :
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