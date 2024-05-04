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
                appTheme = AppTheme.Space,
                thumbnail = R.drawable.thumbnail_space,
                clockFont = ClockFont.POIRET_ONE,
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Fire,
                thumbnail = R.drawable.thumbnail_pixel_fire,
                clockFont = ClockFont.PIXELIFY_SANS,
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Light,
                thumbnail = R.drawable.thumbnail_light,
                clockFont = ClockFont.GERMANIA_ONE,
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Dark,
                thumbnail = R.drawable.thumbnail_dark,
                clockFont = ClockFont.DOPPIO
            ),

            ClockThemePreferences(
                appTheme = AppTheme.CodeFall,
                thumbnail = R.drawable.thumbnail_codefall,
                clockFont = ClockFont.TEKO,
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS
            ),
            ClockThemePreferences(
                appTheme = AppTheme.DvdDark,
                thumbnail = R.drawable.thumbnail_gmc_dark,
                clockFont = ClockFont.TAC_ONE
            ),
            ClockThemePreferences(
                appTheme = AppTheme.DvdLight,
                thumbnail = R.drawable.thumbnail_gmc_light,
                clockFont = ClockFont.MONOTON_REGULAR
            ),
            ClockThemePreferences(
                appTheme = AppTheme.PinkAF,
                thumbnail = R.drawable.thumbnail_pinkaf,
                clockFont = ClockFont.WELLFLEET
            ),
            ClockThemePreferences(
                appTheme = AppTheme.Cat,
                thumbnail = R.drawable.thumbnail_cat,
                clockFont = ClockFont.WELLFLEET
            )
        )
    }
}