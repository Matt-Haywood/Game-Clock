package com.mhappening.gameclock.ui.screens.backgrounds


import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.ceil

@Preview(showBackground = true)
@Composable
fun HomeBackground() {

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val density = LocalDensity.current
    val screenWidthPixels = with(density) { screenWidth.toPx() }
    val screenHeightPixels = with(density) { screenHeight.toPx() }

    val numberOfSquaresOnLongEdge = 20

    val tallerThanWide = screenWidth < screenHeight
    val maxLength = if (tallerThanWide) screenHeightPixels else screenWidthPixels
    val dotSize = (maxLength / numberOfSquaresOnLongEdge)

    val dotCountHorizontal =
        if (!tallerThanWide) numberOfSquaresOnLongEdge else ceil(screenWidthPixels / dotSize).toInt()
    val dotCountVertical =
        if (tallerThanWide) numberOfSquaresOnLongEdge else ceil(screenHeightPixels / dotSize).toInt()


    val alphaAnimation = remember { Animatable(40f) }
    val yAnimation = remember { Animatable(0f) }
    val xAnimation = remember { Animatable(0f) }



    LaunchedEffect("animationKey") {
        launch {
            alphaAnimation.animateTo(
                255f,
                animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing), repeatMode = RepeatMode.Reverse
                ),
            )
        }
        launch {
            yAnimation.animateTo(
                1 * dotSize, animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
        launch {
            xAnimation.animateTo(
                1 * dotSize, animationSpec = infiniteRepeatable(
                    animation = tween(10000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                )
            )
        }
    }
    val alpha1 = (alphaAnimation.value).toInt()
    val alpha2 = 255 - alpha1


    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .blur(
                radiusX = 1.dp,
                radiusY = 1.dp,
                edgeTreatment = BlurredEdgeTreatment.Unbounded
            ),
        contentDescription = "Home Background Canvas"
    ) {
        for (i in 0 until dotCountHorizontal + 1) {
            for (j in 0 until dotCountVertical + 3) {
                val color = if ((i + j) % 2 == 0) Color(82, 82, 82, alpha1)
                else Color(82, 82, 82, alpha2)
                drawCircle(
                    color = color, center = Offset(
                        i * dotSize + dotSize / 2 + xAnimation.value - dotSize,
                        j * dotSize + dotSize / 2 + yAnimation.value - dotSize
                    ), radius = dotSize / 2

                )
            }
        }
    }
}