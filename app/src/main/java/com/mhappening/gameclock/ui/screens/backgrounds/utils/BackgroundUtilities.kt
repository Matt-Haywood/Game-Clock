package com.mhappening.gameclock.ui.screens.backgrounds.utils

import android.content.Context
import android.view.WindowManager
import android.view.WindowMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp


class BackgroundUtilities {

    @Composable
    fun getScreenWidthDp(isFullscreen: Boolean): Float {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        return if (isFullscreen) {
            getScreenWidthPx(isFullscreen = true) / context.resources.displayMetrics.density
//            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
//            val widthPixels = windowMetrics.bounds.width()
//            widthPixels / context.resources.displayMetrics.density
        } else {
            configuration.screenWidthDp.toFloat()
        }

    }

    @Composable
    fun getScreenHeightDp(isFullscreen: Boolean): Float {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        return if (isFullscreen) {
            getScreenHeightPx(isFullscreen = true) / context.resources.displayMetrics.density
//            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
//            val heightPixels = windowMetrics.bounds.height()
//            heightPixels / context.resources.displayMetrics.density
        } else {
            return configuration.screenHeightDp.toFloat()
        }
    }

    @Composable
    fun getScreenWidthPx(isFullscreen: Boolean): Float {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        return if (isFullscreen) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            windowMetrics.bounds.width().toFloat()
        } else {
            val density = LocalDensity.current
            with(density) { configuration.screenWidthDp.dp.toPx() }
        }
    }

    @Composable
    fun getScreenHeightPx(isFullscreen: Boolean): Float {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        return if (isFullscreen) {
            val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val windowMetrics: WindowMetrics = windowManager.currentWindowMetrics
            windowMetrics.bounds.height().toFloat()
        } else {
            val density = LocalDensity.current
            with(density) { configuration.screenHeightDp.dp.toPx() }
        }
    }

    @Composable
    fun isLandscape(): Boolean {
        return LocalConfiguration.current.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE
    }


}