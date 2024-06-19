package com.mhappening.gameclock.ui.screens.backgrounds

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateValueAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.tooling.preview.Preview
import com.mhappening.gameclock.ui.screens.backgrounds.utils.BackgroundUtilities
import kotlin.math.cos
import kotlin.math.min


@Composable
fun BackgroundVaporwave(
    showAnimations: Boolean = true,
    isFullscreen: Boolean = false
) {

    val screenWidth = BackgroundUtilities().getScreenWidthDp(isFullscreen)
    val screenHeight = BackgroundUtilities().getScreenHeightDp(isFullscreen)
    val isLandscape = BackgroundUtilities().isLandscape()

    val transition = rememberInfiniteTransition()
    val animateValue by animateValueAsState(
        targetValue = if (showAnimations) {
            transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Restart
                ), label = "Grid Animation"
            ).value
        } else 0f,
        typeConverter = Float.VectorConverter,
        label = "Grid Animation"
    )

    val orbAnimation by animateValueAsState(
        targetValue = if (showAnimations) {
            transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(durationMillis = 60000, easing = sineEasing()),
                    repeatMode = RepeatMode.Reverse
                ), label = "Orb Animation"
            ).value
        } else 0.5f,
        typeConverter = Float.VectorConverter,
        label = "Orb Animation"
    )
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush =
                Brush.verticalGradient(
                    colorStops = arrayOf(
                        0f to Color.Magenta,
                        0.5f to Color.Black
                    ),
                    startY = 0f
                )
            )
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                // Apply a rotation transformation
                rotationX = if (isLandscape) 50f else 50f // Rotate around the X-axis
                // Set the origin of the transformation to the center of the screen
                transformOrigin = TransformOrigin(0.5f, 0.5f)
            }

    ) {
        val center = Offset(size.width / 2, size.height / 2)
        val horizon = size.height / 2
        val numberOfLinesHorizontal = if (isLandscape) 20 else 25
        val lineSpacingHorizontal = (size.height / 2) / numberOfLinesHorizontal
        val numberOfLinesVertical = if (isLandscape) 20 else 20
        val lineSpacingVertical = (size.width ) / numberOfLinesVertical

        withTransform({
            val depth = if (isLandscape) {
                0.5f + cos(2 * Math.PI.toFloat()) * 0.5f
            } else {
                0.5f + cos(2 * Math.PI.toFloat()) * 0.5f
            }

            translate(
                left = center.x * (1 - depth).toFloat(),
                top = horizon * (1 - depth).toFloat()
            )
            scale(scaleX = depth, scaleY = depth, pivot = center)
        }) {
            for (i in 0 until numberOfLinesHorizontal) {
                val start =
                    Offset(
                        0f,
                        i * lineSpacingHorizontal + animateValue * lineSpacingHorizontal + size.height / 2
                    )
                val end = Offset(
                    size.width,
                    i * lineSpacingHorizontal + animateValue * lineSpacingHorizontal + size.height / 2
                )
                drawGlowingLine(Color.Magenta, start, end, strokeWidth = 5f, isXDirection = false)
            }
            for (i in 0 until numberOfLinesVertical) {
                val start =
                    Offset(i * lineSpacingVertical, size.height)
                val end = Offset(
                    i * lineSpacingVertical,
                    horizon
                )
                drawGlowingLine(Color.Magenta, start, end, strokeWidth = 5f)
            }

            drawRect(
                brush =
                Brush.linearGradient(
                    colorStops = arrayOf(
                        0f to Color.White,
//                0.98f to Color(0xFFF7AC81),
                        1f to Color.Black
                    ),
                    start = Offset(size.width / 2, 0f),
                    end = Offset(size.width / 2, size.height)
                ),
                alpha = 0.5f,
                blendMode = BlendMode.Color
            )

        }
    }
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        val rectWidth = size.width
        val rectHeight = size.height * 0.6f
        val rectStart = Offset(0f, 0f)
        val rectEnd = Offset(rectStart.x + rectWidth, rectStart.y + rectHeight)
        drawRect(
            brush =
            Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Black,
                    0.1f to Color(0xFFFE00BF),
                    0.4f to Color(0xFFFE0082),
                    0.8f to Color(0xFFFE9A61),
//                0.98f to Color(0xFFF7AC81),
                    1f to Color.White
                ),
                start = Offset(rectStart.x + rectWidth / 2, rectStart.y),
                end = Offset(rectStart.x + rectWidth / 2, rectEnd.y)
            ),
            topLeft = rectStart,
            size = Size(rectWidth, rectHeight)
        )
        drawRect(
            brush =
            Brush.linearGradient(
                colorStops = arrayOf(
                    0f to Color.Gray,
//                0.2f to Color.Black,
                    0.4f to Color.Magenta,
//                0.4f to Color(0xFFFE0082),
//                0.8f to Color(0xFFFE9A61),
//                0.98f to Color(0xFFF7AC81),
                    1f to Color.White
                ),
                start = Offset(rectStart.x + rectWidth / 2, rectStart.y),
                end = Offset(rectStart.x + rectWidth / 2, rectEnd.y)
            ),
            topLeft = rectStart,
            size = Size(rectWidth, rectHeight),
            alpha = 0.7f,
            blendMode = BlendMode.Modulate
        )
        drawGlowingLine(
            Color.White,
            Offset(0f, rectEnd.y),
            Offset(size.width, rectEnd.y),
            strokeWidth = 5f,
            isXDirection = false
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        val circleCenter = Offset(
            size.width / 2,
            size.height * 0.42f + orbAnimation * if (isLandscape) 60f else 150f
        )
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0f to Color(0xFFFFCA72),
                    0.9f to Color.Transparent
                ),
                center = circleCenter
            ),
            center = circleCenter,
            radius = 0.50f * min(size.width, size.height),
            blendMode = BlendMode.Plus
        )
        drawCircle(
            brush = Brush.verticalGradient(
                colorStops = arrayOf(
                    0f to Color(0xFFFFCA72),
                    1f to Color(0xFFFF002E)
                ),
                startY = 0f
            ),
            center = circleCenter,
            radius = 0.3f * min(size.width, size.height),
        )


    }
}

fun DrawScope.drawGlowingLine(
    color: Color,
    start: Offset,
    end: Offset,
    strokeWidth: Float = 5f,
    alpha: Float = 1f,
    isXDirection: Boolean = true,
    blurRadius: Float = 50f
) {
    val mainLinePaint = Paint().apply {
        this.color = Color.White
        this.strokeWidth = strokeWidth
        this.isAntiAlias = true
        this.style = PaintingStyle.Stroke
        this.strokeCap = StrokeCap.Round
        this.strokeJoin = StrokeJoin.Round
        this.strokeWidth = strokeWidth
        this.blendMode = BlendMode.Plus
        this.alpha = alpha


//        this.shader = android.graphics.RadialGradient(
//            end.x - start.x,
//            start.y,
//            blurRadius,
//            color.toArgb(),
//            Color.Transparent.toArgb(),
//            android.graphics.Shader.TileMode.CLAMP
//        )
    }

    val glowPaint = Paint().apply {
        this.color = color
        this.strokeWidth = 100f
        this.style = PaintingStyle.Stroke
        this.strokeCap = StrokeCap.Round
        this.strokeJoin = StrokeJoin.Round
        this.blendMode = BlendMode.Plus
        this.alpha = alpha
        this.shader = LinearGradientShader(
            from = if (isXDirection) Offset(start.x - 50, start.y) else Offset(
                start.x,
                start.y - 50
            ),
            to = if (isXDirection) Offset(start.x + 50, start.y) else Offset(start.x, start.y + 50),
            colors = listOf(
                Color.Transparent,
                color.copy(alpha = 0.3f),
                color,
                color.copy(alpha = 0.3f),
                Color.Transparent
            ),
            colorStops = listOf(0f, 0.4f, 0.5f, 0.6f, 1f)
        )
        this.isAntiAlias = true
    }

    drawIntoCanvas { canvas ->
        canvas.drawLine(start, end, mainLinePaint)
        canvas.drawLine(start, end, glowPaint)
    }
}

@Preview
@Composable
fun BackgroundVaporwavePreviewP() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BackgroundVaporwave(showAnimations = true)
    }
}

@Preview(
    showBackground = true,
    showSystemUi = false,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun BackgroundVaporwavePreviewL() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        BackgroundVaporwave(showAnimations = true)
    }
}