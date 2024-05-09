package com.mhappening.gameclock.ui.screens.backgrounds

import androidx.compose.runtime.Composable
import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.ui.ClockUiState

@Composable
fun BackgroundChooser(clockUiState: ClockUiState) {

    when (clockUiState.theme) {
        AppTheme.Fire -> {
            PixelFireBackground(
                showAnimations = clockUiState.showAnimations,
                isFullscreen = clockUiState.isFullScreen
            )
        }

        AppTheme.Light -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
        }

        AppTheme.Dark -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
//            window.insetsController?.setSystemBarsAppearance(
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
        }

        AppTheme.CodeFall -> {
            BackgroundDigitalRain(clockUiState = clockUiState)
        }

        AppTheme.Space -> {
            SpaceBackground(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.DvdDark -> {
            DvdBackground(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.DvdLight -> {
            DvdBackground(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.PinkAF -> {
            BackgroundPinkAF(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.Cat -> {
            BackgroundCat(
                showAnimations = clockUiState.showAnimations,
                clockScale = clockUiState.clockScale,
                clockFormat = clockUiState.clockFormat
            )
        }

        else -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)

        }

    }
}