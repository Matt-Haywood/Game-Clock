package com.example.gameclock.data.clockthemes

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface ClockDao {

    @Query("SELECT * from clock_preferences_table")
    fun getAllThemePreferences(): Flow<List<ClockThemePreferences>>

    @Query("SELECT * from clock_preferences_table WHERE appTheme = :appTheme")
    fun getPreferencesForTheme(appTheme: AppTheme): Flow<ClockThemePreferences>

    @Update(entity = ClockThemePreferences::class)
    suspend fun update(clockThemePreferences: ClockThemePreferences)

    @Insert(entity = ClockThemePreferences::class)
    suspend fun writeClockTheme(clockThemePreferences: ClockThemePreferences)
}