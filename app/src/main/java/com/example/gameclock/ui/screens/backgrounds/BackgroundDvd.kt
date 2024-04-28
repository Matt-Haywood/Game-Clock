package com.example.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gameclock.R

/**
 * A Composable function that displays a DVD logo bouncing around the screen.
 * The color of the logo changes each time it hits the edge of the screen.
 *
 * @param showAnimations A boolean value that determines whether animations should be shown.
 */
@Preview(showBackground = true)
@Composable
fun DvdBackground(showAnimations: Boolean = true) {
    // Screen dimensions
    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    // DVD logo dimensions
    val dvdWidth = 150f
    val dvdHeight = 75f

    // DVD logo speed
    val dvdSpeed = 0.1f

    // DVD logo position
    val dvdX = 0f
    val dvdY = 0f

    // DVD logo animation
    val dvdAnimation = rememberInfiniteTransition(label = "Dvd Animation")

    val dvdBoundsOffsetX by dvdAnimation.animateFloat(
        initialValue = dvdX,
        targetValue = screenWidth - dvdWidth,
        animationSpec = infiniteRepeatable(
            animation = tween((screenWidth / dvdSpeed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "X Axis dvd Animation"
    )

    val dvdBoundsOffsetY by dvdAnimation.animateFloat(
        initialValue = dvdY,
        targetValue = screenHeight - dvdHeight,
        animationSpec = infiniteRepeatable(
            animation = tween((screenHeight / dvdSpeed).toInt(), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Y Axis dvd Animation"
    )

    // Spectrum of colors
    val colors = listOf(
        Color.Red,
        Color(255, 87, 34, 255),
        Color.Yellow,
        Color.Green,
        Color.Blue,
        Color(75, 0, 130, 255),
        Color(155, 38, 182, 255),
    )
    var colorIndex by remember { mutableIntStateOf(0) }

    // Update colorIndex when DVD logo hits the edge
    LaunchedEffect(dvdBoundsOffsetX, dvdBoundsOffsetY) {
        colorIndex = when {
            dvdBoundsOffsetX <= 0.5f -> (colorIndex + 1) % colors.size
            dvdBoundsOffsetX >= screenWidth - dvdWidth - 0.5f -> (colorIndex + 1) % colors.size
            dvdBoundsOffsetY <= 0.5f -> (colorIndex + 1) % colors.size
            dvdBoundsOffsetY >= screenHeight - dvdHeight - 0.5f -> (colorIndex + 1) % colors.size
            else -> colorIndex
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.gmc_logo),
            contentDescription = "DVD Logo",
            modifier = Modifier
                .offset(x = dvdBoundsOffsetX.dp, y = dvdBoundsOffsetY.dp)
                .size(dvdWidth.dp, dvdHeight.dp),
            contentScale = ContentScale.FillBounds,
            colorFilter = ColorFilter.tint(colors[colorIndex])
        )
    }
}