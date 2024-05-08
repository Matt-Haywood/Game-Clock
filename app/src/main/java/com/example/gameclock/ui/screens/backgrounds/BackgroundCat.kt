package com.example.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.Easing
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.gameclock.R
import com.example.gameclock.model.ClockFormat
import com.example.gameclock.ui.screens.backgrounds.utils.BackgroundUtilities
import kotlin.math.ceil
import kotlin.math.sin
import kotlin.math.PI

@Preview
@Composable
fun BackgroundCat(showAnimations: Boolean = true, clockScale: Float=2f, clockFormat: ClockFormat = ClockFormat.TWENTY_FOUR_HOUR) {
    val isLandscape = BackgroundUtilities().isLandscape()
    val screenWidth = BackgroundUtilities().getScreenWidth()
    val screenHeight = BackgroundUtilities().getScreenHeight()


    val catTopYOffset = when (clockFormat) {
        ClockFormat.TWELVE_HOUR, ClockFormat.TWELVE_HOUR_WITH_SECONDS -> {
            if (isLandscape) -42*clockScale + 25 else -30*clockScale + 40
        }
        ClockFormat.TWENTY_FOUR_HOUR, ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) -42*clockScale + 15 else -25*clockScale + 40
        }
        ClockFormat.VERTICAL_TWELVE_HOUR -> {
            if (isLandscape) 0f else -115*clockScale + 20
        }
        ClockFormat.VERTICAL_TWENTY_FOUR_HOUR -> {
            if (isLandscape) 0f else -115*clockScale + 60
        }
        ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS -> {
            if (isLandscape) 0f else -150*clockScale + 40
        }
        ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) 0f else -130*clockScale + 40
        }
    }


    val catBottomYOffset = when (clockFormat) {
        ClockFormat.TWELVE_HOUR, ClockFormat.TWELVE_HOUR_WITH_SECONDS -> {
            if (isLandscape) 42*clockScale - 45 else 40*clockScale - 80
        }
        ClockFormat.TWENTY_FOUR_HOUR, ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) 42*clockScale - 45 else 25*clockScale - 80
        }
        ClockFormat.VERTICAL_TWELVE_HOUR -> {
            if (isLandscape) -90f else 115*clockScale - 60
        }
        ClockFormat.VERTICAL_TWENTY_FOUR_HOUR -> {
            if (isLandscape) -90f else 115*clockScale - 90
        }
        ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS -> {
            if (isLandscape) -90f else 143*clockScale - 80
        }
        ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) -90f else 130*clockScale - 80
        }
    }


    val catXOffset = when (clockFormat) {
        ClockFormat.TWELVE_HOUR, ClockFormat.TWELVE_HOUR_WITH_SECONDS, ClockFormat.TWENTY_FOUR_HOUR, ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            0f
        }
        ClockFormat.VERTICAL_TWELVE_HOUR, ClockFormat.VERTICAL_TWENTY_FOUR_HOUR, ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS, ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) -(screenWidth/3) - (clockScale * 10) else 0f
        }
    }

    val scaleModifier = when (clockFormat) {
        ClockFormat.TWELVE_HOUR, ClockFormat.TWELVE_HOUR_WITH_SECONDS, ClockFormat.TWENTY_FOUR_HOUR, ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            Modifier
        }
        ClockFormat.VERTICAL_TWELVE_HOUR, ClockFormat.VERTICAL_TWENTY_FOUR_HOUR, ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS, ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS -> {
            if (isLandscape) Modifier.scale(clockScale/3 +0.3f) else Modifier
        }
    }

    val catAnimation = rememberInfiniteTransition(label = "Cat Animation")

    var eyeOffset by remember{ mutableFloatStateOf(0f) }
    eyeOffset = if (showAnimations) {
    catAnimation.animateFloat(
        initialValue =-15f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "Cat Eye Animation"
    ).value
    } else 0f

    var tailRotation by remember{ mutableFloatStateOf(0f) }
    tailRotation = if (showAnimations) {
        catAnimation.animateFloat(
            initialValue = 30f,
            targetValue = -30f,
            animationSpec = infiniteRepeatable(
                animation = tween(2000, easing = sineEasing()),
                repeatMode = RepeatMode.Reverse
            ), label = "Cat tail Animation"
        ).value
    } else 0f

    Box(
        modifier = Modifier.fillMaxSize().background(color = Color.White)
    ) {
        val tileWidth = 80f
        val tileHeight = tileWidth*3/5
        val numberOfTilesX = (ceil(screenWidth / tileWidth) + 1).toInt()
        val numberOfTilesY = (ceil(screenHeight / tileHeight) + 1).toInt()

        for (i in 0 until numberOfTilesX) {
            for (j in 0 until numberOfTilesY) {
                val xOffset = if (j % 2 ==0) -tileWidth/2 else 0f
                Image(
                    painter = painterResource(id = R.drawable.tile),
                    contentDescription = "CatTile",
                    modifier = Modifier
                        .offset(x = (xOffset + i * tileWidth - tileWidth/2).dp, y = (j * tileHeight - tileHeight/2).dp)
                        .size(width = tileWidth.dp, height = tileHeight.dp),

                )
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = scaleModifier.fillMaxSize()
        ) {
            Image(
                painter = painterResource(id = R.drawable.cat_tail),
                contentDescription = "CatTail",
                modifier = Modifier.offset(y = catBottomYOffset.dp + 280.dp, x = catXOffset.dp)
                    .graphicsLayer(
                        rotationZ = tailRotation,
                        transformOrigin = TransformOrigin(0.5f, 0f)
                    ),
                contentScale = ContentScale.Inside
            )
            Image(
                painter = painterResource(id = R.drawable.cat_eye_mask),
                contentDescription = "catEyeMask",
                modifier = Modifier.offset(y = catTopYOffset.dp, x = catXOffset.dp),
                contentScale = ContentScale.Inside
            )
            Image(
                painter = painterResource(id = R.drawable.cat_eyes),
                contentDescription = "CatEyes",
                modifier = Modifier.offset(y = catTopYOffset.dp, x = eyeOffset.dp + catXOffset.dp),
                contentScale = ContentScale.Inside
            )
            Image(
                painter = painterResource(id = R.drawable.cat_top),
                contentDescription = "CatTop",
                modifier = Modifier.offset(y = catTopYOffset.dp, x = catXOffset.dp),
                contentScale = ContentScale.Inside
            )

            Image(
                painter = painterResource(id = R.drawable.cat_bottom),
                contentDescription = "CatBottom",
                modifier = Modifier.offset(y = catBottomYOffset.dp, x = catXOffset.dp),
                contentScale = ContentScale.Inside
            )
        }
    }
}

fun sineEasing() : Easing = Easing { fraction ->
    ((sin(2 * PI * fraction - PI / 2) + 1) / 2).toFloat()
}
