package com.example.gameclock.data.clockthemes

import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme
import com.example.gameclock.ui.theme.ClockFont


class ClockThemeList {

    fun loadThemes(): List<ClockTheme> {
        return listOf(
//            ClockTheme(AppTheme.Default, R.drawable.thumbnail_light, AppTheme.Default.themeName, ClockFont.ROBOTO),
            ClockTheme(
                appTheme = AppTheme.Light,
                thumbnail = R.drawable.thumbnail_light,
                themeTitle = AppTheme.Light.themeName,
                clockFont = ClockFont.ROBOTO,
            ),
            ClockTheme(
                appTheme = AppTheme.Dark,
                thumbnail = R.drawable.thumbnail_dark,
                themeTitle = AppTheme.Dark.themeName,
                clockFont = ClockFont.DOPPIO
            ),
            ClockTheme(
                appTheme = AppTheme.Red,
                thumbnail = R.drawable.thumbnail_red,
                themeTitle = AppTheme.Red.themeName,
                clockFont = ClockFont.PIXELIFY_SANS,
            ),
            ClockTheme(
                appTheme = AppTheme.CodeFall,
                thumbnail = R.drawable.thumbnail_red,
                themeTitle = AppTheme.CodeFall.themeName,
                clockFont = ClockFont.TEKO
            ),
            ClockTheme(
                appTheme = AppTheme.Space,
                thumbnail = R.drawable.thumbnail_red,
                themeTitle = AppTheme.Space.themeName,
                clockFont = ClockFont.ANEK
            ),
            ClockTheme(
                appTheme = AppTheme.DvdDark,
                thumbnail = R.drawable.thumbnail_red,
                themeTitle = AppTheme.DvdDark.themeName,
                clockFont = ClockFont.DOPPIO
            ),
            ClockTheme(
                appTheme = AppTheme.DvdLight,
                thumbnail = R.drawable.thumbnail_light,
                themeTitle = AppTheme.DvdLight.themeName,
                clockFont = ClockFont.DOPPIO
            ),
        )
    }
}