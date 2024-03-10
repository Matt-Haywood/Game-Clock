package com.example.gameclock.model

import androidx.annotation.DrawableRes

enum class AppTheme {
    Default,
    Light,
    Dark,
    Red
}

class ClockTheme(
    val appTheme: AppTheme,
    @DrawableRes val thumbnail: Int,
    val themeTitle: String

    )