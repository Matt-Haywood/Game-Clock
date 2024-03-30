package com.example.gameclock.ui.screens.backgrounds.pixel_background_model

sealed class WindDirection {
    object Right : WindDirection()
    object Left : WindDirection()
    object None : WindDirection()
}