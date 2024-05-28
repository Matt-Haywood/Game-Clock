package com.mhappening.gameclock.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mhappening.gameclock.R
import com.mhappening.gameclock.model.Timer
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone


@Composable
fun TimerListDialog(
    timerViewModel: TimerViewModel,
    timerList: List<Timer>,
    onTimerClick: (Timer) -> Unit = {},
) {
    CustomDialog(onDismissRequest = { timerViewModel.dismissTimerListPopup() }) {
        Surface(
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Text(
                    text = stringResource(R.string.timers),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 15.dp)

                )
                TimerList(
                    timerList = timerList,
                    onTimerClick = onTimerClick,
                    onTimerPausePlay = { timer -> timerViewModel.toggleTimerPausePlay(timer) },
                    onTimerCancel = { timer -> timerViewModel.cancelTimer(timer) },
                    onTimerMaximise = { timer -> timerViewModel.setSmallTimerRunning(timer) },
                    addTimerOnClick = {
                        timerViewModel.toggleTimerPickerPopup()
                        timerViewModel.dismissTimerListPopup()
                    }
                )
            }
        }
    }
}


@Composable
fun TimerList(
    timerList: List<Timer>,
    onTimerClick: (Timer) -> Unit = {},
    onTimerPausePlay: (Timer) -> Unit = {},
    onTimerCancel: (Timer) -> Unit = {},
    onTimerMaximise: (Timer) -> Unit = {},
    addTimerOnClick: () -> Unit = {},
) {
    LazyColumn(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(10.dp)
    ) {
        timerList.forEach { timer ->
            item {
                TimerListItem(
                    timer = timer,
                    onTimerClick = onTimerClick,
                    onTimerPausePlay = onTimerPausePlay,
                    onTimerCancel = onTimerCancel,
                    onTimerMaximise = onTimerMaximise,
                    )
            }
        }
        item {
            Spacer(modifier = Modifier.padding(10.dp))
            FilledIconButton(onClick = addTimerOnClick) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add Timer",
                        modifier = Modifier
                            .scale(0.5f)
                            .offset(x = 15.dp)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_hourglass_empty_24),
                        contentDescription = "Add Timer",
                        modifier = Modifier
                            .offset(x = (-2).dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TimerListItem(
    timer: Timer = Timer(endTime = Date(1717059119121), durationSeconds = 3600),
    onTimerClick: (Timer) -> Unit = {},
    onTimerPausePlay: (Timer) -> Unit = {},
    onTimerCancel: (Timer) -> Unit = {},
    onTimerMaximise: (Timer) -> Unit = {},
) {
    var remainingDuration by remember { mutableLongStateOf(0L) }

    val timerFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

//    val cardColors = if (timer.isEnabled) {
//        CardDefaults.cardColors(
//        )
//    } else {
//        CardDefaults.cardColors(
//            contentColor = CardDefaults.cardColors().disabledContentColor,
//            containerColor = CardDefaults.cardColors().disabledContainerColor
//        )
//    }

    LaunchedEffect(timer.endTime) {
        val endTime = timer.endTime
        while (true) {
            if (timer.isEnabled) {
                val calendar = Calendar.getInstance()
                val now = calendar.time
                if (now.before(endTime)) {
                    remainingDuration = endTime.time - now.time
                } else {
                    remainingDuration = 0
                    break
                }

            } else remainingDuration = timer.remainingTimeOnPause
            delay(1000)
        }
    }




    Card(modifier = Modifier.padding(top = 10.dp)) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .padding(10.dp)
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_notifications_active_24),
                        contentDescription = "Timer Icon",
                        modifier = Modifier
                            .size(15.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = timerFormat.format(timer.endTime),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start,
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.baseline_hourglass_full_24),
                        contentDescription = "Timer Icon",
                        modifier = Modifier
                            .size(15.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = timerFormat.format(timer.durationSeconds * 1000),
                        style = MaterialTheme.typography.bodySmall
                    )
                }


                IconButton(
                    onClick = { onTimerMaximise(timer) },
                    modifier = Modifier
                        .height(24.dp)
                        .width(36.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_open_in_full_24),
                            contentDescription = "Pop out timer",
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = timerFormat.format(remainingDuration),
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(
                        onClick = { onTimerCancel(timer) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_close_24),
                            contentDescription = "Cancel Timer",
                        )
                    }
                    IconButton(
                        onClick = { onTimerPausePlay(timer) },
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_pause_24),
                            contentDescription = "Pause Timer"
                        )
                        /*if (timer.isEnabled) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_pause_24),
                                contentDescription = "Pause Timer"
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                                contentDescription = "Start Timer"
                            )
                        }*/
                    }
                }
            }
        }
    }

}