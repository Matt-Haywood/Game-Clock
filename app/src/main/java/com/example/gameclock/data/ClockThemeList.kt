package com.example.gameclock.data

import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockTheme


class ClockThemeList {

    fun loadThemes(): List<ClockTheme> {
        return listOf(
            ClockTheme(AppTheme.Light, R.drawable.thumbnail_light, "Light theme"),
            ClockTheme(AppTheme.Dark, R.drawable.thumbnail_dark, "Dark theme"),
            ClockTheme(AppTheme.Red, R.drawable.thumbnail_red, "Red theme")
        )
    }
}