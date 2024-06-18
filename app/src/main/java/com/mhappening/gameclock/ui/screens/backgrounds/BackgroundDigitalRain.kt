package com.mhappening.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.InfiniteTransition
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mhappening.gameclock.R
import com.mhappening.gameclock.ui.screens.backgrounds.utils.BackgroundUtilities
import com.mhappening.gameclock.ui.theme.Matrix
import kotlinx.coroutines.delay
import kotlin.math.max
import kotlin.random.Random
import kotlin.random.nextInt

val textSeedList = listOf(
    "JDKAICEKTNASK",
    "DFJEPTNMCLSEPSSDDFEURI",
    "DFELKCPSIENTNSKSLLCKEKIXMLKAJDOIOWI",
    "AJDFKDJELALXIXJHVHELSDIESORFRYTPQ",
    "DAJEHRALKJDFHLAKJHDLFIJKCTYDRE",
    "DFASJEIJXJEJAKXKEJTRHYTIOWIEGHIUSE",
    "SPOEISLKANSKDHFGYEIESKOXJWOQIUEJSNCMLEJGSD"
)

private const val permissableCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZbcdfgjklnopqsuvwyz?-&"

@Composable
fun BackgroundDigitalRain(isFullScreen: Boolean = false, showAnimations: Boolean = true) {
    // Get the screen dimensions
    val isLandscape = BackgroundUtilities().isLandscape()
    val screenWidth = BackgroundUtilities().getScreenWidthDp(isFullScreen)
    val screenHeight = BackgroundUtilities().getScreenHeightDp(isFullScreen)

    // Create an infinite transition that oscillates the radius between 0 and maxScreenSize
    val infiniteTransition =
        rememberInfiniteTransition(label = "Digital Rain Animation")

    val localContentDescription = stringResource(R.string.digital_rain_background)

    val cellsX = if (isLandscape) 11 else 7
    val cellsZ = 5

    val spacingX = screenWidth / cellsX
    val cellMid = cellsX / 2

    val delayList = List(cellsX * cellsZ + 1) { Random.nextInt(0..4000) }
    val durationList = List(cellsX * cellsZ + 1) {
        if (isLandscape) Random.nextInt(10000..25000) else Random.nextInt(15000..30000)
    }
    val noAnimationYList = List(cellsX * cellsZ + 1) {
        ((-0.2 * screenHeight).toInt()..<screenHeight.toInt()).random().toFloat()
    }

    val fontSize = if (isLandscape) 25 else 20


    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = Modifier
            .fillMaxSize()
            .semantics { contentDescription = localContentDescription }
            .background(color = Color.Black)
    ) {

        for (x in 0..<cellsX) {
            for (z in 0..<cellsZ) {
                val index = x + (z * cellsX)

                val textSeedIndex = Random.nextInt(textSeedList.indices)

                val offsetX = if (x <= cellMid) {
                    (spacingX * x) - (z * (14 - z)) + 14
                } else {
                    (spacingX * x) + (z * (14 - z))
                }

                val scale = 1f - z * 0.15f
                val textRotationY = 12f - ((12f - (-12f)) * ((offsetX - 0) / (screenWidth - 0)))


                ColumnOfText(
                    showAnimations = showAnimations,
                    infiniteTransition = infiniteTransition,
                    screenHeight = screenHeight,
                    text = textSeedList[textSeedIndex],
                    fontSize = fontSize,
                    duration = durationList[index],
                    delay = delayList[index],
                    noAnimationY = noAnimationYList[index],
                    zIndex = z,
                    modifier = Modifier
                        .offset(x = offsetX.dp, y = 0.dp)
                        .graphicsLayer {
                            transformOrigin = TransformOrigin(0.5f, 0f)
                            scaleX = scale
                            scaleY = scale
                            rotationY = textRotationY

                        }
                )
            }
        }
    }
}


@Composable
fun ColumnOfText(
    showAnimations: Boolean,
    infiniteTransition: InfiniteTransition,
    screenHeight: Float,
    text: String,
    fontSize: Int,
    duration: Int,
    delay: Int,
    noAnimationY: Float,
    zIndex: Int,
    modifier: Modifier
) {

    val textState = remember { mutableStateOf(text) }

    if (showAnimations) {
        LaunchedEffect(key1 = textState) {
            while (true) {
                delay(1000 + delay / 10L)
                textState.value = generateNewText(textState.value)
            }
        }
    }

    // Add a state variable to track the first iteration
    var isFirstIteration by remember { mutableStateOf(true) }

    // Animate the y-coordinate of each column
    var yLocation by remember { mutableIntStateOf(0) }
    yLocation = if (showAnimations) {
        val initialValue = if (isFirstIteration) {
            // If it's the first iteration, start from a random position
            noAnimationY
        } else {
            // If it's not the first iteration, start from the top of the screen
            -fontSize * text.length * 1.15f
        }

        val delayCalculated = if (isFirstIteration) {
            0
        } else {
            delay
        }

        val distanceLeft = 1 - (noAnimationY / screenHeight)

        val durationCalculated = if (isFirstIteration) {
            max((duration * distanceLeft).toInt(), 1)
        } else {
            duration
        }

        val steppedAnimation = infiniteTransition.animateValue(
            initialValue = (initialValue / fontSize).toInt(),
            targetValue = (screenHeight / fontSize).toInt(),
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(durationCalculated, delayCalculated, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Digital Rain Animation",
        )

        // After the first iteration, set isFirstIteration to false
        LaunchedEffect(steppedAnimation) {
            delay((durationCalculated + delayCalculated - 100).toLong())
            isFirstIteration = false
        }

        steppedAnimation.value
    } else {
        noAnimationY.toInt()
    }

    // Create a column with the generated properties
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .height((screenHeight * 2).dp)
            .width(3.dp)
            .offset(x = 0.dp, y = (yLocation * fontSize).dp)
    ) {
        Text(
            text = textState.value,
            style = TextStyle(
                shadow = Shadow(
                    color = Color.Green,
                    offset = Offset(x = 0f, y = -2f),
                    blurRadius = 50f
                ),
                brush = Brush.linearGradient(
                    0.0f to Color.Transparent,
                    0.2f to Color(102, 177, 102, 220),
                    0.8f to Color(101, 216, 101, 255),
                    1f to Color(255, 255, 255, 255),
                    start = Offset(0f, 0f),
                ),
                alpha = 0.98f - (zIndex * 0.12f),
                fontFamily = Matrix,
            ),
            fontSize = fontSize.sp,
            overflow = TextOverflow.Visible,
            textAlign = TextAlign.Center,
            modifier = modifier
        )
    }
}

fun generateNewText(currentText: String): String {
    val newText = StringBuilder(currentText)
    for (i in newText.indices) {
        if (Random.nextFloat() < 0.17) { // 17% chance to change a character
            newText[i] = permissableCharacters
                .random() // Replace the character with a random character from a random string in textSeedList
        }
    }
    return newText.toString()
}


@Preview
@Composable
fun BackgroundGreenNumbersPreviewP() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BackgroundDigitalRain(showAnimations = true)
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
        BackgroundDigitalRain(showAnimations = true)
    }
}