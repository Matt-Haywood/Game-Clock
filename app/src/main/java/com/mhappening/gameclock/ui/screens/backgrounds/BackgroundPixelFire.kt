package com.mhappening.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.util.lerp
import com.mhappening.gameclock.R
import com.mhappening.gameclock.ui.screens.backgrounds.pixel_background_model.fireColors
import com.mhappening.gameclock.ui.screens.backgrounds.utils.BackgroundUtilities
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.random.Random

/**
 * This is a Composable function that creates a pixel fire animation.
 * This background is based upon the Pixel Fire background created by @ditn and available at https://github.com/ditn/Doom-Compose
 * however, this version has been heavily modified by myself from the ground up to be more performant and efficient.
 * It may be a similar effect but the way I got there is completely different.
 * @param showAnimations A boolean value to control whether animations should be shown or not. Default is true.
 */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PixelFireBackground(showAnimations: Boolean = true, isFullscreen: Boolean = false) {
    // Get the screen width and height
    val screenWidth = BackgroundUtilities().getScreenWidthPx(isFullscreen)
    val screenHeight = BackgroundUtilities().getScreenHeightPx(isFullscreen)

    val isLandscape = BackgroundUtilities().isLandscape()

    // Set the pixel size of the fire
    val pixelSize = Size(
        width = if (isLandscape) floor(screenWidth / 60f) else floor(screenWidth / 35f),
        height = if (isLandscape) floor(screenWidth / 60f) else floor(screenWidth / 35f))

    // Calculate the number of pixels in width and height
    val pixelsNumberWidth = ceil(screenWidth / pixelSize.width).toInt() + if (isFullscreen && isLandscape) 6 else 0
    val pixelsNumberHeight = ceil(screenHeight / pixelSize.height).toInt() + if (isFullscreen && !isLandscape) 6 else 0

    // Set the fire speed, 0.01f is a good value
    val fireSpeed = 0.008f

    // Create an infinite animation for the fire
    val infiniteAnimation = rememberInfiniteTransition(label = "Fire animation")
    val firePixelOffsetY by if (showAnimations) {
        infiniteAnimation.animateValue(
            initialValue = pixelsNumberHeight,
            targetValue = 0,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween((pixelsNumberHeight / fireSpeed).toInt(), easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "Y Axis pixel fire animation"
        )
    } else {
        remember { mutableIntStateOf(0) }
    }

    // Create a random array of points to use as a point from the top of screen at which each pixel will finish its interpolation to black
    val randomPoint = Array(pixelsNumberWidth * pixelsNumberHeight) { Random.nextFloat() * 0.26f * pixelsNumberHeight }

    val localContentDescription = stringResource(R.string.pixel_fire_background)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    fireColors,
                    start = Offset(0f, screenHeight * 0.1f),
                    end = Offset(0f, screenHeight)
                )
            )
            .semantics { contentDescription = localContentDescription }
    ) {
        for (i in 0 until pixelsNumberWidth) {
            for (y in 0 until pixelsNumberHeight) {
                val firePixelY = if ((firePixelOffsetY + y - pixelsNumberHeight) < 0f) {
                    (firePixelOffsetY + y)
                } else firePixelOffsetY + y - pixelsNumberHeight
                val colorIndex =
                    if (firePixelY < randomPoint[i + y * pixelsNumberWidth]) {
                        0
                    } else {
                        val index = lerp(
                            start = 0,
                            stop = fireColors.size,
                            fraction = (firePixelY - randomPoint[i + y * pixelsNumberWidth]) / pixelsNumberHeight.toFloat()
                        )
                        // Ensure the index is always less than the size of the array
                        if (index >= fireColors.size) fireColors.size - 1 else index
                    }

                val yOffset = firePixelY * pixelSize.height

                // Get the color for this rectangle
                val pixelColor = fireColors[colorIndex]
                drawRect(
                    color = pixelColor,
                    topLeft = Offset(
                        x = (i * pixelSize.width),
                        y = yOffset
                    ),

                    size = pixelSize
                )
            }
        }
    }
}