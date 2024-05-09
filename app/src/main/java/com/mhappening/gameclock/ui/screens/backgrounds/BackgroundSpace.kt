package com.mhappening.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Data class representing a star in the space background.
 *
 * @property currentX The current x-coordinate of the star.
 * @property currentY The current y-coordinate of the star.
 * @property originalX The original x-coordinate of the star.
 * @property originalY The original y-coordinate of the star.
 * @property timeOnScreen The time the star has been on the screen.
 * @property animationOffset The offset for the star's animation.
 */
data class Star(
    var currentX: Float,
    var currentY: Float,
    var originalX: Float,
    var originalY: Float,
    var timeOnScreen: Float = 0f,
    val animationOffset: Float
)

/**
 * A Composable function that displays a space background with stars moving across the screen.
 * The stars move from an animated center point and get larger as they spend more time on screen
 * to create a 3d effect.
 * @param showAnimations A boolean value to determine if the animations should be shown.
 */

@Preview(showBackground = true)
@Composable
fun SpaceBackground(showAnimations: Boolean = true) {
    val density = LocalDensity.current
    val screenWidth = with(density) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeight = with(density) { LocalConfiguration.current.screenHeightDp.dp.toPx() }
    val starCount = 300
    val center = Offset(screenWidth / 2, screenHeight / 2)

    val animatedCenterInfiniteTransition = rememberInfiniteTransition(label = "animatedCenterInfiniteTransition")
    val animatedCenterX by animatedCenterInfiniteTransition.animateFloat(
        initialValue = screenWidth * 0.2f,
        targetValue = screenWidth * 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Animated Center X"
    )

    val animatedCenter = if (showAnimations) Offset(animatedCenterX, screenHeight / 2) else Offset(screenWidth / 2, screenHeight / 2)
    val stars = remember {
        List(starCount) {
            val x = Random.nextFloat() * screenWidth
            val y = Random.nextFloat() * screenHeight
            Star(
                currentX = x,
                currentY = y,
                originalX = x,
                originalY = y,
                animationOffset = Random.nextFloat()
            )
        }
    }
    val starColor = Color.White

    val infiniteTransition = rememberInfiniteTransition(label = "Stars Animation")

    val animation by if (showAnimations) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        )
    } else {
        animateFloatAsState(
            targetValue = 1f,
            animationSpec = tween(2000, easing = LinearEasing), label = ""
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(17, 17, 17, 255))
    ) {
/*        // Draw crosshair at the center of the screen
        val crosshairColor = Color.Red
        val crosshairSize = 50f // Adjust this value to change the size of the crosshair
        val crosshairStrokeWidth = 4f


        // Horizontal line
        drawLine(
            color = crosshairColor,
            start = Offset(animatedCenter.x - crosshairSize / 2, animatedCenter.y),
            end = Offset(animatedCenter.x + crosshairSize / 2, animatedCenter.y),
            strokeWidth = crosshairStrokeWidth
        )

        // Vertical line
        drawLine(
            color = crosshairColor,
            start = Offset(animatedCenter.x, animatedCenter.y - crosshairSize / 2),
            end = Offset(animatedCenter.x, animatedCenter.y + crosshairSize / 2),
            strokeWidth = crosshairStrokeWidth
        )*/

        stars.forEach { star ->
            // Check if the star's position needs to be reset
            if (star.currentX > center.x + screenWidth * 0.7 || star.currentX < center.x - screenWidth * 0.7 || star.currentY > center.y + screenHeight * 0.7 || star.currentY < center.y - screenHeight * 0.7) {
                star.timeOnScreen = 0f
                star.originalX = Random.nextFloat() * screenWidth
                star.originalY = Random.nextFloat() * screenHeight
                star.currentX = star.originalX
                star.currentY = star.originalY
            }

            // Update time on screen
            star.timeOnScreen += animation

            // Calculate alpha value based on time on screen
            val alpha = if (star.timeOnScreen < 2f) star.timeOnScreen/2 else 1f

            // Apply alpha value to star color
            val starColorWithAlpha = starColor.copy(alpha = alpha)

            val direction =
                Offset(star.currentX - animatedCenter.x, star.currentY - animatedCenter.y).normalize()
            val acceleration =
                star.timeOnScreen * 0.01f  // Adjust the multiplier to control the acceleration rate

            val newPosition = Offset(
                star.currentX + direction.x * acceleration,
                star.currentY + direction.y * acceleration
            )

            star.currentX = newPosition.x
            star.currentY = newPosition.y

//            drawCircle(
//                color = starColorWithAlpha,
//                center = Offset(star.currentX, star.currentY),
//                radius = 0.4f * acceleration + 2f
//            )

            val trailCount = 5 // Number of trails for each star, adjust as needed
            val trailDistance = 5f // Distance between each trail, adjust as needed

            for (i in 0 until trailCount) {
                val trailAlpha = alpha * (1f - i.toFloat() / trailCount)
                val trailRadius = 0.5f * acceleration +  trailDistance
                val trailPosition = Offset(
                    star.currentX - i * direction.x *  acceleration,
                    star.currentY - i * direction.y *  acceleration
                )

                drawCircle(
                    color = when (i) {
                        1 -> starColorWithAlpha
                            else -> starColor.copy(alpha = trailAlpha)},
                        center = trailPosition,
                        radius = trailRadius
                            )

            }

        }
    }
}


/**
 * Normalizes the offset.
 * @return The normalized offset.
 */
fun Offset.normalize(): Offset {
    val length = getDistance()
    return if (length != 0f) Offset(x / length, y / length) else this
}

