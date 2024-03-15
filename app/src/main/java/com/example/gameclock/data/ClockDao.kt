package com.example.gameclock.data

import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme
import com.example.gameclock.model.ClockThemePreferences
import kotlinx.coroutines.flow.Flow

@Dao
interface ClockDao {

    @Query("SELECT * from clock_preferences_database")
    fun getAllThemePreferences(): Flow<List<ClockThemePreferences>>

    @Query("SELECT * from clock_preferences_database WHERE appTheme = :appTheme")
    fun getPreferencesForTheme(appTheme: AppTheme): Flow<ClockThemePreferences>

    @Update
    suspend fun update(clockThemePreferences: ClockThemePreferences)

    @Insert
    suspend fun writeClockTheme(clockThemePreferences: ClockThemePreferences)
}