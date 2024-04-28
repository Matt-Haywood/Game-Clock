package com.example.gameclock.model

import androidx.annotation.DrawableRes
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.gameclock.ui.theme.ClockFont


@Entity(tableName = "clock_preferences_table")
class ClockThemePreferences(
    @PrimaryKey val appTheme: AppTheme,
    @DrawableRes val thumbnail: Int,
    val showAnimations: Boolean = true,
    val clockFormat: ClockFormat = ClockFormat.TWENTY_FOUR_HOUR,
    val clockScale: Float = 1.8f,
    val buttonsScale: Float = 1.2f,
    val showAlarmButton: Boolean = true,
    val showTimerButton: Boolean = true,
    val clockFont: ClockFont
)

