package com.example.gameclock.data.clockthemes

import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockFormat
import com.example.gameclock.model.ClockThemePreferences
import com.example.gameclock.ui.theme.ClockFont


class ClockThemeList {

    fun loadThemes(): List<ClockThemePreferences> {
        return listOf(
            ClockThemePreferences(
                appTheme = AppTheme.Light,
                thumbnail = R.drawable.thumbnail_light,
                clockFont = ClockFont.ROBOTO,
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Dark,
                thumbnail = R.drawable.thumbnail_dark,
                clockFont = ClockFont.DOPPIO
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Red,
                thumbnail = R.drawable.thumbnail_red,
                clockFont = ClockFont.PIXELIFY_SANS,
            ),
            ClockThemePreferences(
                appTheme = AppTheme.CodeFall,
                thumbnail = R.drawable.thumbnail_red,
                clockFont = ClockFont.TEKO,
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Space,
                thumbnail = R.drawable.thumbnail_red,
                clockFont = ClockFont.POIRET_ONE,
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR
            ),
            ClockThemePreferences(
                appTheme = AppTheme.DvdDark,
                thumbnail = R.drawable.thumbnail_red,
                clockFont = ClockFont.DOPPIO
            ),
            ClockThemePreferences(
                appTheme = AppTheme.DvdLight,
                thumbnail = R.drawable.thumbnail_light,
                clockFont = ClockFont.DOPPIO
            ),
        )
    }
}