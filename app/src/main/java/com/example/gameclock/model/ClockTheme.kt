package com.example.gameclock.model

import androidx.annotation.DrawableRes
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "clock_preferences_database")
class ClockThemePreferences (
    @PrimaryKey
    val appTheme: AppTheme,
    val showSeconds: Boolean = true,
    val showAnimations: Boolean = true,
    val clockFormatIsTwelveHour: Boolean = true,
    val clockScale: Float = 1f,
    val buttonsScale: Float = 1f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,

    //TODO should be in user preferences as it applies to the whole app, not just per clock theme
    val isFullScreen: Boolean = false,
)

class ClockTheme (
    val appTheme: AppTheme,
    @DrawableRes val thumbnail: Int,
    val themeTitle: String,
)

