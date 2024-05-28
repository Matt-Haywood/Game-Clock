package com.mhappening.gameclock.ui.timer

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.mhappening.gameclock.R
import com.mhappening.gameclock.model.Timer
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.roundToInt

@Preview
@Composable
fun TimerRunningDraggableSurface(
    timer: Timer = Timer(endTime = Date(1716745489814L), isEnabled = true),
    onTimerPausePlay: () -> Unit = {},
    onTimerCancel: () -> Unit = {},
    onTimerMinimise: () -> Unit = {},
) {

    var remainingDuration by remember { mutableLongStateOf(0L) }

    val timerFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC") }

    LaunchedEffect(timer.endTime) {
        val endTime = timer.endTime
        while (timer.isEnabled) {
            val calendar = Calendar.getInstance()
            val now = calendar.time
            if (now.before(endTime)) {
                remainingDuration = endTime.time - now.time
            } else {
                remainingDuration = 0
                break
            }
            delay(1000)  // Update every second
        }
    }

    val density = LocalDensity.current
    val defaultOffsetX =
        with(density) { (LocalConfiguration.current.screenWidthDp.dp.toPx() / 2 - 130.dp.toPx()) }
    val defaultOffsetY =
        with(density) { (LocalConfiguration.current.screenHeightDp * 0.1f).dp.toPx() }


    var offsetX by remember { mutableStateOf(defaultOffsetX) }
    var offsetY by remember { mutableStateOf(defaultOffsetY) }

    Surface(
        onClick = { /*TODO*/ },
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        modifier = Modifier
            .size(260.dp, 80.dp)
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) } // Apply the offset
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    if (change.positionChange() != Offset.Zero) change.consume()
                    offsetX += dragAmount.x
                    offsetY += dragAmount.y
                }
            }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(5.dp)
        ) {
            IconButton(onClick = onTimerMinimise) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_fullscreen_24),
                    contentDescription = "Cancel Timer"
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(5.dp)
            ) {
                Text(text = timerFormat.format(remainingDuration))
            }
            IconButton(onClick = onTimerPausePlay) {
                if (timer.isEnabled) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_pause_24),
                        contentDescription = "Pause Timer"
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                        contentDescription = "Start Timer"
                    )
                }

            }
            IconButton(onClick = onTimerCancel) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_close_24),
                    contentDescription = "Cancel Timer"
                )
            }
        }
    }
}