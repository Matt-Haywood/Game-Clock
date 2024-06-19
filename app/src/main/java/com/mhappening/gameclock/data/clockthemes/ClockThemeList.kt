package com.mhappening.gameclock.data.clockthemes

import com.mhappening.gameclock.R
import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.model.ClockFormat
import com.mhappening.gameclock.model.ClockThemePreferences
import com.mhappening.gameclock.ui.theme.ClockFont


class ClockThemeList {

    val clockThemeList = listOf(
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
        ),
        ClockThemePreferences(
            appTheme = AppTheme.Vaporwave,
            thumbnail = R.drawable.thumbnail_vaporwave,
            clockFont = ClockFont.TIMES_NEW_ROMAN,
            clockFormat = ClockFormat.TWENTY_FOUR_HOUR
        )
    )

    fun loadThemes(): List<ClockThemePreferences> {
        return clockThemeList
    }

    fun getThemeByAppTheme(appTheme: AppTheme): ClockThemePreferences {
        return clockThemeList.find { it.appTheme == appTheme } ?: clockThemeList[0]
    }
}