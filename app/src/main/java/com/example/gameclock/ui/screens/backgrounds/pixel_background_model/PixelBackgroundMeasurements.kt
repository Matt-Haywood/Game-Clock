package com.example.gameclock.ui.screens.backgrounds.pixel_background_model

import kotlin.math.ceil

data class PixelBackgroundMeasurements(
    var width: Int,
    var height: Int
)

val PixelBackgroundMeasurements.tallerThanWide: Boolean
    get() = width < height

val PixelBackgroundMeasurements.pixelSize: Int
    get() {
        val longestLength = if (tallerThanWide) width else height
        return ceil(longestLength.toDouble() / 50).toInt()
    }

val PixelBackgroundMeasurements.widthPixel: Int
    get() = when {
        tallerThanWide -> 50
        else -> ceil(width.toDouble() / pixelSize).toInt()
    }


val PixelBackgroundMeasurements.heightPixel: Int
    get() = when {
        !tallerThanWide -> 50
        else -> ceil(height.toDouble() / pixelSize).toInt()
    }