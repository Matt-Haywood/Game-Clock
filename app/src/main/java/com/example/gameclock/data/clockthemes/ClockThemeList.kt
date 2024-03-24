package com.example.gameclock.data.clockthemes

import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme


class ClockThemeList {

    fun loadThemes(): List<ClockTheme> {
        return listOf(
//            ClockTheme(AppTheme.Default, R.drawable.thumbnail_light, AppTheme.Default.themeName),
            ClockTheme(AppTheme.Light, R.drawable.thumbnail_light, AppTheme.Light.themeName),
            ClockTheme(AppTheme.Dark, R.drawable.thumbnail_dark, AppTheme.Dark.themeName),
            ClockTheme(AppTheme.Red, R.drawable.thumbnail_red, AppTheme.Red.themeName),
            ClockTheme(AppTheme.CodeFall, R.drawable.thumbnail_red, AppTheme.CodeFall.themeName),
        )
    }
}