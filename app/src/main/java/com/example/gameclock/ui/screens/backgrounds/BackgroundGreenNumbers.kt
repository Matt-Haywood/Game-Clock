package com.example.gameclock.ui.screens.backgrounds

import android.graphics.RenderEffect
import android.graphics.RuntimeShader
import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gameclock.ui.ClockUiState
import com.example.gameclock.ui.screens.backgrounds.codefall_model.MatrixShader
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random
import kotlin.random.nextInt


@Composable
fun BackgroundGreenNumbers(clockUiState: ClockUiState) {
    // Get the screen dimensions
    val screenWidth = LocalConfiguration.current.screenWidthDp.toFloat()
    val screenHeight = LocalConfiguration.current.screenHeightDp.toFloat()

    // Calculate the max of the screen width and height
    val maxScreenSize: Float = maxOf(screenWidth, screenHeight)

    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                launch {
                    value = if (clockUiState.showAnimations) {
                        it / 1000f
                    } else {
                        delay(1000)
                        1000f
                    }
                }

            }
        }
    }
    val matrixShader = RuntimeShader(MatrixShader)

    // Create the text to display
    val binaryText =
        "0110010101101010010101010001010110101101101110001010100101101001010100101101010101101101001001110101"

    val charText = "kfeokxhjslektgalszxzqqhwmkxsdfiajelkthakskflkerthalksdflktlaksdhthsasdlkdtkejriejskdjurtioqnsigfpakd"
    // Create an infinite transition that oscillates the radius between 0 and maxScreenSize
    val infiniteTransition =
        rememberInfiniteTransition(label = "Digital Rain Animation")

    Canvas(modifier = Modifier
        .fillMaxSize()
        .onSizeChanged { size ->
            matrixShader.setFloatUniform(
                "resolution", size.width.toFloat(), size.height.toFloat()
            )
        }
        .graphicsLayer {
            matrixShader.setFloatUniform("time", time)
            renderEffect = RenderEffect
                .createRuntimeShaderEffect(
                    matrixShader,
                    "contents"
                )
                .asComposeRenderEffect()
        }
    ) {
        drawRect(Color.Red)
    }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxSize()
//            .background(
//                brush =
//                Brush.verticalGradient(
//                    0.0f to MaterialTheme.colorScheme.background,
//                    1f to Color.Black,
//                )
//            )
    ) {
        Row(
//            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            var columnSpaceCounter = 0
            while (columnSpaceCounter < screenWidth) {
                val delay = Random.nextInt(0..5000)
                val duration = Random.nextInt(8000..20000)
                val spacing = Random.nextInt(-5..20) // -10 to 15 works well.
                val fontSize = Random.nextInt(10..18)
                columnSpaceCounter += (spacing * 2) + 5
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
                        .width(spacing.dp)
                )
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
/*    val time by produceState(0f) {
        while (true) {
            withInfiniteAnimationFrameMillis {
                value = it / 1000f
            }
        }
    }*/
    // Animate the y-coordinate of each column
    var yLocation by remember { mutableFloatStateOf(0f) }
    yLocation = if (clockUiState.showAnimations) {
        infiniteTransition.animateFloat(
            initialValue = -maxScreenSize * 3,
            targetValue = screenHeight,
            animationSpec = infiniteRepeatable(
                animation = tween(duration, delay, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ), label = "Digital Rain Animation"
        ).value
    } else {
        (noAnimationY)
    }

//    val glitchShader = RuntimeShader(GlitchShader)


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
//                shadow = Shadow(
//                    color = MaterialTheme.colorScheme.surfaceDim,
//                    offset = Offset(x = 100f, y = -200f),
//                    blurRadius = fontSize / 9f
//                ),
                brush = Brush.linearGradient(
                    0.0f to Color.Transparent,
                    0.2f to Color.Green,
                    0.8f to Color.Green,
                    1f to Color.White,
                    start = Offset(0f, 0f),
                    ),

                ),
            color = Color.Green,
            fontSize = fontSize.sp,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
            modifier = Modifier
//                .onSizeChanged { size ->
//                    glitchShader.setFloatUniform(
//                        "resolution", size.width.toFloat(), size.height.toFloat())
//                }
//                .graphicsLayer {
//                    glitchShader.setFloatUniform("time", time)
//                    renderEffect = android.graphics.RenderEffect
//                        .createRuntimeShaderEffect(
//                            glitchShader,
//                            "contents"
//                        )
//                        .asComposeRenderEffect()
//                }
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