package com.example.gameclock.ui.screens.backgrounds

import android.graphics.Path
import android.graphics.PathMeasure
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gameclock.R
import kotlin.math.atan2
import kotlin.random.Random

@Preview
@Composable
fun BackgroundPinkAF(showAnimations: Boolean = true) {

    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()
    val isLandscape = screenWidth > screenHeight

    val infiniteTransition =
        rememberInfiniteTransition(label = "Floating heart Animation")

    // Defining random variables once on initial load

    val floatingHeartNumber = if (isLandscape) 110 else 100
    val randomListSize = (floatingHeartNumber * 0.7).toInt()
    val pathWidth = if (isLandscape) 100f else 200f
    val noAnimationYList = List(randomListSize) { Random.nextFloat() }
    val xOffsetList =
        List(floatingHeartNumber) { (Random.nextFloat() * (screenWidth + pathWidth)) - (pathWidth) }
    val delayList = List(randomListSize) { Random.nextInt(0, 13000) }
    val durationList = if (isLandscape) {
        List(randomListSize) { Random.nextInt(4000, 7000) }
    } else {
        List(randomListSize) { Random.nextInt(8000, 15000) }
    }
    val sizeList = List(floatingHeartNumber) { Random.nextInt(30, 50) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary,
//                        MaterialTheme.colorScheme.secondary,
                        MaterialTheme.colorScheme.tertiary,

                        )
                )
            )
    ) {
        for (i in 0 until floatingHeartNumber) {
            FloatingHeart(
                showAnimations = showAnimations,
                noAnimationY = noAnimationYList[i % randomListSize],
                xOffset = xOffsetList[i],
                screenHeight = screenHeight,
                infiniteTransition = infiniteTransition,
                delay = delayList[i % randomListSize],
                wiggleVariant = Random.nextBoolean(),
                duration = durationList[i % randomListSize],
                pathWidth = pathWidth,
                heartSize = sizeList[i]
            )
        }
    }
}

@Composable
fun FloatingHeart(
    showAnimations: Boolean,
    noAnimationY: Float,
    xOffset: Float = 70f,
    screenHeight: Float,
    infiniteTransition: InfiniteTransition,
    wiggleVariant: Boolean,
    pathWidth: Float,
    delay: Int,
    duration: Int,
    heartSize: Int
) {
    val halfPathWidth = pathWidth / 2

    // Define the path
    val path = if (wiggleVariant) {
        Path().apply {
            moveTo(halfPathWidth, screenHeight)
            cubicTo(
                halfPathWidth,
                screenHeight,
                pathWidth,
                screenHeight * 5 / 6,
                halfPathWidth,
                screenHeight * 4 / 6
            )
            cubicTo(
                halfPathWidth,
                screenHeight * 4 / 6,
                0f,
                screenHeight * 3 / 6,
                halfPathWidth,
                screenHeight * 2 / 6
            )
            cubicTo(
                halfPathWidth,
                screenHeight * 2 / 6,
                pathWidth,
                screenHeight * 1 / 6,
                halfPathWidth,
                0f
            )
            lineTo(0f, -screenHeight / 6)
        }
    } else {
        Path().apply {
            moveTo(halfPathWidth, screenHeight)
            cubicTo(
                halfPathWidth,
                screenHeight,
                pathWidth,
                screenHeight * 7 / 8,
                halfPathWidth,
                screenHeight * 6 / 8
            )
            cubicTo(
                halfPathWidth,
                screenHeight * 6 / 8,
                0f,
                screenHeight * 5 / 8,
                halfPathWidth,
                screenHeight * 4 / 8
            )
            cubicTo(
                halfPathWidth,
                screenHeight * 4 / 8,
                pathWidth,
                screenHeight * 3 / 8,
                halfPathWidth,
                screenHeight * 2 / 8
            )
            cubicTo(
                halfPathWidth,
                screenHeight * 2 / 8,
                0f,
                screenHeight * 1 / 8,
                halfPathWidth,
                0f
            )
            lineTo(pathWidth, -screenHeight / 8)
        }
    }


// Create a PathMeasure object
    val pathMeasure = PathMeasure(path, false)

// Animate the progress along the path from 0 to 1
    var progress by remember { mutableFloatStateOf(0f) }

    progress = if (showAnimations) {
        infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = ""
        ).value
    } else {
        noAnimationY
    }

// Calculate the position and tangent at the current progress
    val pos = FloatArray(2)
    val tan = FloatArray(2)
    pathMeasure.getPosTan(progress * pathMeasure.length, pos, tan)

// Calculate the rotation angle
    val rotation = (atan2(tan[1].toDouble(), tan[0].toDouble()) * 180 / Math.PI).toFloat() + 90f

    Image(
        painter = painterResource(id = R.drawable.heart1),
        contentDescription = "Floating Heart",
        colorFilter = ColorFilter.tint(Color.White),
        modifier = Modifier
            .size(heartSize.dp)
            .offset(x = pos[0].dp + xOffset.dp, y = pos[1].dp)
            .rotate(rotation)
    )


}

