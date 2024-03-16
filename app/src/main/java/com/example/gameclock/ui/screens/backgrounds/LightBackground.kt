package com.example.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import com.example.gameclock.ui.ClockUiState


@Composable
fun LightBackground(clockUiState: ClockUiState) {
    // Get the screen dimensions
    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    // Calculate the max of the screen width and height
    val maxScreenSize: Float = maxOf(screenWidth, screenHeight) * 1.5f

    // Create an infinite transition that oscillates the radius between 0 and maxScreenSize
    val infiniteTransition =
        rememberInfiniteTransition(label = "lightBackground Breathing Animation")
    var radius by remember { mutableFloatStateOf(1f) }
    radius = if (clockUiState.showAnimations) {
        infiniteTransition.animateFloat(
            initialValue = maxScreenSize * 0.75f,
            targetValue = maxScreenSize,
            animationSpec = infiniteRepeatable(
                animation = tween(10000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ), label = "lightBackground Breathing Animation"
        ).value
    } else {
        maxScreenSize
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    0.0f to MaterialTheme.colorScheme.background,
                    1f to Color.Black,
                    radius = radius,
                    tileMode = TileMode.Clamp
                )
            )
    )
}

@Preview
@Composable
fun LightBackgroundPreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LightBackground(ClockUiState())
    }
}