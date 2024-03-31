package com.example.gameclock.ui.screens.backgrounds

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.PixelBackgroundMeasurements
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.PixelBackgroundState
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.PixelBackgroundViewModel
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.WindDirection
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.fireColors
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.heightPixel
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.pixelSize
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.tallerThanWide
import com.example.gameclock.ui.screens.backgrounds.pixel_background_model.widthPixel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.random.Random


@Composable
fun BackgroundPixelFire(
    pixelBackgroundViewModel: PixelBackgroundViewModel = viewModel(factory = PixelBackgroundViewModel.Factory)
) {
    val pixelBackgroundState = pixelBackgroundViewModel.state.collectAsState().value
    val configuration = LocalConfiguration.current
    val pixelBackgroundMeasurements = PixelBackgroundMeasurements(
        configuration.screenWidthDp,
        configuration.screenHeightDp
    )

SetupArtView(
    pixelBackgroundMeasurements = pixelBackgroundMeasurements,
    pixelBackgroundViewModel = pixelBackgroundViewModel)
    PixelArtDrawer(pixelBackgroundMeasurements, pixelBackgroundState)
}

@Composable
fun PixelArtDrawer(
    pixelBackgroundMeasurements: PixelBackgroundMeasurements,
    pixelBackgroundState: PixelBackgroundState,
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (pixelBackgroundState.pixels.isNotEmpty()) {
            RenderPixelArt(
                pixelBackgroundState.pixels,
                pixelBackgroundMeasurements.heightPixel,
                pixelBackgroundMeasurements.widthPixel,
                pixelBackgroundMeasurements.pixelSize
            )
        }
    }
}

@Composable
fun RenderPixelArt(
    artPixels: List<Int>,
    heightPixelsNumber: Int,
    widthPixelsNumber: Int,
    pixelSize: Int
) {
    Row(modifier = Modifier.fillMaxSize()) {
        for (column in 0 until widthPixelsNumber) {
            Column(modifier = Modifier
                .width(pixelSize.dp)
                .fillMaxHeight()) {
                for (row in 0 until heightPixelsNumber - 1) {
                    val currentPixelIndex = column + (widthPixelsNumber * row)
                    val currentPixel = artPixels[currentPixelIndex]
                    Row(
                        modifier = Modifier
                            .size(pixelSize.dp)
                            .background(fireColors[currentPixel])
                    ) {

                    }
                }
            }

        }
    }
}

@Composable
fun SetupArtView(
    pixelBackgroundMeasurements: PixelBackgroundMeasurements,
    pixelBackgroundViewModel: PixelBackgroundViewModel
) {
    val arraySize = pixelBackgroundMeasurements.widthPixel * pixelBackgroundMeasurements.heightPixel

    val pixelArray = IntArray(arraySize) { 0 }
        .apply { createArtSource(this, pixelBackgroundMeasurements) }
    val scope  = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                calculateArtPropagation(pixelArray, pixelBackgroundMeasurements)
                pixelBackgroundViewModel.updatePixels(pixelArray.toList())
                delay(16) // delay to match approximately 60 FPS
            }
        }
    }
}

fun createArtSource(pixelArray: IntArray, pixelBackgroundMeasurements: PixelBackgroundMeasurements) {
    val overFlowPixelIndex = pixelBackgroundMeasurements.widthPixel * pixelBackgroundMeasurements.heightPixel

    for (column in 0 until pixelBackgroundMeasurements.widthPixel) {
        val pixelIndex = (overFlowPixelIndex - pixelBackgroundMeasurements.widthPixel) + column
        pixelArray[pixelIndex] = fireColors.size - 1
    }
}

fun calculateArtPropagation(
    pixelArray: IntArray,
    pixelBackgroundMeasurements: PixelBackgroundMeasurements
) {
    for (column in 0 until pixelBackgroundMeasurements.widthPixel) {
        for (row in 1 until pixelBackgroundMeasurements.heightPixel) {
            val currentPixelIndex = column + (pixelBackgroundMeasurements.widthPixel * row)
            updateArtPerPixel(
                currentPixelIndex,
                pixelArray,
                pixelBackgroundMeasurements
            )
        }
    }
}

private fun updateArtPerPixel(
    currentPixelIndex: Int,
    pixelArray: IntArray,
    measurements: PixelBackgroundMeasurements
) {
    val bellowPixelIndex = currentPixelIndex + measurements.widthPixel
    if (bellowPixelIndex >= measurements.widthPixel * measurements.heightPixel) return

    val offset = if (measurements.tallerThanWide) 2 else 3
    val decay = floor(Random.nextDouble() * offset).toInt()
    val bellowPixelArtIntensity = pixelArray[bellowPixelIndex]
    val newArtIntensity = when {
        bellowPixelArtIntensity - decay >= 0 -> bellowPixelArtIntensity - decay
        else -> 0
    }

    pixelArray[currentPixelIndex] = newArtIntensity
}

@Composable
fun SetupFireView(
    canvas: PixelBackgroundMeasurements,
    pixelBackgroundViewModel: PixelBackgroundViewModel,
    windDirection: WindDirection = WindDirection.Left
) {
    val arraySize = canvas.widthPixel * canvas.heightPixel

    val pixelArray = IntArray(arraySize) { 0 }
        .apply { createFireSource(this, canvas) }

    LaunchedEffect(Unit) {
        while (true) {
            calculateFirePropagation(pixelArray, canvas, windDirection)
            pixelBackgroundViewModel.updatePixels(pixelArray.toList())
            delay(16) // delay to match approximately 60 FPS
        }
    }
}

fun createFireSource(firePixels: IntArray, canvas: PixelBackgroundMeasurements) {
    val overFlowFireIndex = canvas.widthPixel * canvas.heightPixel

    for (column in 0 until canvas.widthPixel) {
        val pixelIndex = (overFlowFireIndex - canvas.widthPixel) + column
        firePixels[pixelIndex] = fireColors.size - 1
    }
}

fun calculateFirePropagation(
    firePixels: IntArray,
    pixelBackgroundMeasurements: PixelBackgroundMeasurements,
    windDirection: WindDirection
) {
    for (column in 0 until pixelBackgroundMeasurements.widthPixel) {
        for (row in 1 until pixelBackgroundMeasurements.heightPixel) {
            val currentPixelIndex = column + (pixelBackgroundMeasurements.widthPixel * row)
            updateFireIntensityPerPixel(
                currentPixelIndex,
                firePixels,
                pixelBackgroundMeasurements,
                windDirection
            )
        }
    }
}

private fun updateFireIntensityPerPixel(
    currentPixelIndex: Int,
    firePixels: IntArray,
    measurements: PixelBackgroundMeasurements,
    windDirection: WindDirection
) {
    val bellowPixelIndex = currentPixelIndex + measurements.widthPixel
    if (bellowPixelIndex >= measurements.widthPixel * measurements.heightPixel) return

    val offset = if (measurements.tallerThanWide) 2 else 3
    val decay = floor(Random.nextDouble() * offset).toInt()
    val bellowPixelFireIntensity = firePixels[bellowPixelIndex]
    val newFireIntensity = when {
        bellowPixelFireIntensity - decay >= 0 -> bellowPixelFireIntensity - decay
        else -> 0
    }

    val newPosition = when (windDirection) {
        WindDirection.Right -> if (currentPixelIndex - decay >= 0) currentPixelIndex - decay else currentPixelIndex
        WindDirection.Left -> if (currentPixelIndex + decay >= 0) currentPixelIndex + decay else currentPixelIndex
        WindDirection.None -> currentPixelIndex
    }

    firePixels[newPosition] = newFireIntensity
}