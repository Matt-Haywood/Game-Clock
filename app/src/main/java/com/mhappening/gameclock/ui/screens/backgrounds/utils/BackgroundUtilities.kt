package com.mhappening.gameclock.ui.screens.backgrounds.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

class BackgroundUtilities {

    @Composable
    fun getScreenWidth(): Float {
        return LocalConfiguration.current.screenWidthDp.toFloat()
    }

    @Composable
    fun getScreenHeight(): Float {
        return LocalConfiguration.current.screenHeightDp.toFloat()
    }

    @Composable
    fun isLandscape(): Boolean {
        return LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }


}