package com.example.gameclock.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.TextUnit

class ScreenUtilities {

    @Composable
    fun getScreenWidthInSp(): TextUnit {
        // Get the screen width in pixels
        val screenWidthInPixels = LocalConfiguration.current.screenWidthDp

        // Convert the screen width to density-independent pixels (dp)
        val screenWidthInDp = with(LocalDensity.current) { screenWidthInPixels.toDp() }

        // Convert the screen width from dp to scale-independent pixels (sp) and return it

        return with(LocalDensity.current) { screenWidthInDp.toSp() }
    }
}