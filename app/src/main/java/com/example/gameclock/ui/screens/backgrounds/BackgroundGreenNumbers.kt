package com.example.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontVariation.width
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gameclock.ui.ClockUiState
import com.example.gameclock.ui.theme.md_theme_red_light_shadow
import kotlin.random.Random
import kotlin.random.nextInt


@Composable
fun BackgroundGreenNumbers(clockUiState: ClockUiState) {
    // Get the screen dimensions
    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    // Calculate the max of the screen width and height
    val maxScreenSize: Float = maxOf(screenWidth, screenHeight)

    // Create the text to display
    val binaryText =
        "01100101011010100101010100010101101011011011100010101001011010010101001011010101011011010010011101"

    // Create an infinite transition that oscillates the radius between 0 and maxScreenSize
    val infiniteTransition =
        rememberInfiniteTransition(label = "Digital Rain Animation")


    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush =
                Brush.verticalGradient(
                    0.0f to MaterialTheme.colorScheme.background,
                    1f to Color.Black,
                )
            )
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween) {
            var columnSpaceCounter = 0
            while (columnSpaceCounter < screenWidth) {
                val delay = Random.nextInt(0..5000)
                val duration = Random.nextInt(5000..20000)
                val spacing = Random.nextInt(-10..15)
                val fontSize = Random.nextInt(10..18)
                columnSpaceCounter += (spacing * 2) + fontSize - 2
                val noAnimationY = (0..maxScreenSize.toInt()).random().toFloat()
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(spacing.dp)
                )
                ColumnOfText(
                    clockUiState = clockUiState,
                    infiniteTransition = infiniteTransition,
                    screenHeight = screenHeight,
                    maxScreenSize = maxScreenSize,
                    binaryText = binaryText,
                    fontSize = fontSize,
                    duration = duration,
                    delay = delay,
                    noAnimationY = noAnimationY
                )
                Spacer(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(spacing.dp))
            }
        }
    }
}

@Composable
fun ColumnOfText(
    clockUiState: ClockUiState,
    infiniteTransition: InfiniteTransition,
    screenHeight: Float,
    maxScreenSize: Float,
    binaryText: String,
    fontSize: Int,
    duration: Int,
    delay: Int,
    noAnimationY: Float
) {
    // Animate the y-coordinate of each column
    var yLocation by remember { mutableFloatStateOf(0f) }
    yLocation = if (clockUiState.showAnimations) {
        infiniteTransition.animateFloat(
            initialValue = -maxScreenSize * 3 ,
            targetValue = screenHeight,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "Digital Rain Animation"
        ).value
    } else {
        (noAnimationY)
    }

    // Create a column with the generated properties
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height((maxScreenSize * 2).dp)
            .width(5.dp)
            .absoluteOffset(y = yLocation.dp)

    ) {
        Text(
            text = binaryText,
            style = TextStyle(
                shadow = Shadow(color = md_theme_red_light_shadow, offset = Offset(x = 100f, y = -200f), blurRadius = fontSize/9f),
                brush = Brush.linearGradient(
                    0.0f to Color.Transparent,
                    0.2f to Color.Green,
                    0.8f to Color.Green,
                    1f to Color.White,
                    start = Offset(0f, 0f),

                ),

            ),
            fontSize = fontSize.sp,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
        )
    }
}

@Preview
@Composable
fun BackgroundGreenNumbersPreviewP() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BackgroundGreenNumbers(ClockUiState(showAnimations = false))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun BackgroundGreenNumbersPreviewL() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BackgroundGreenNumbers(ClockUiState(showAnimations = false))
    }
}