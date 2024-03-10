package com.example.gameclock.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.theme.GameClockTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

//TODO Update system back button logic to change theme back to default.

@Composable
fun BaseClockScreen(
    clockViewModel: ClockViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isLandscape: Boolean = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
) {
    val uiState by clockViewModel.uiState.collectAsState()
    val showAlarmPopup = remember { mutableStateOf(false) }
    val showTimerPopup = remember { mutableStateOf(false) }

    if (isLandscape) {
        LandscapeBaseClock(
            showSeconds = uiState.showSeconds,
            clockScale = uiState.clockScale,
            showAlarmButton = uiState.showAlarmButton,
            showAlarmPopup = showAlarmPopup,
            showTimerButton = uiState.showTimerButton,
            showTimerPopup = showTimerPopup,
            clockFormatIsTwelveHour = uiState.clockFormatIsTwelveHour,
            buttonScale = uiState.buttonsScale,
            alarmButtonOnClick = { showAlarmPopup.value = !showAlarmPopup.value },
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick
        )
    } else {
        PortraitBaseClock(
            showSeconds = uiState.showSeconds,
            clockScale = uiState.clockScale,
            showAlarmButton = uiState.showAlarmButton,
            showAlarmPopup = showAlarmPopup,
            showTimerButton = uiState.showTimerButton,
            showTimerPopup = showTimerPopup,
            clockFormatIsTwelveHour = uiState.clockFormatIsTwelveHour,
            buttonScale = uiState.buttonsScale,
            alarmButtonOnClick = { showAlarmPopup.value = !showAlarmPopup.value },
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick
        )
    }
}


@Composable
fun LandscapeBaseClock(
    showSeconds: Boolean,
    clockFormatIsTwelveHour: Boolean,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    clockScale: Float,
    buttonScale: Float,
    showAlarmPopup: MutableState<Boolean>,
    showAlarmButton: Boolean,
    showTimerPopup: MutableState<Boolean>,
    showTimerButton: Boolean,
    alarmButtonOnClick: () -> Unit
) {
    Text(text = "L")
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showAlarmPopup.value) {
            PopupWindowDialog()
        }
        AnimatedVisibility(visible = showTimerPopup.value) {
            PopupWindowDialog()
        }
        Column(horizontalAlignment = Alignment.Start, modifier = Modifier.fillMaxHeight()) {
            Row(verticalAlignment = Alignment.Top, modifier = Modifier.weight(0.5f)) {
                BackButton(onBackClick = onBackClick)
            }
            Row(verticalAlignment = Alignment.Bottom, modifier = Modifier.weight(0.5f)) {
                SettingsButton(onSettingsClick = onSettingsClick)
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.8f)
            ) {
                ClockText(
                    showSeconds = showSeconds, clockFormatIsTwelveHour = clockFormatIsTwelveHour,
                    clockSize = 40.sp * clockScale
                )


//                AndroidView(
//                    // on below line we are initializing our text clock.
//                    factory = { context ->
//                        TextClock(context).apply {
//                            // on below line we are setting 12 hour format.
//                            if (clockFormatIsTwelveHour) {
//                                format12Hour?.let { this.format12Hour = clockNumberFormat }
//                            } else {
//                                format24Hour?.let { this.format24Hour = clockNumberFormat }
//                            }
//                            // on below line we are setting time zone.
//                            timeZone?.let { this.timeZone = it }
//                            // on below line we are setting text size.
//                            textSize.let { this.textSize = 100f * clockScale }
//                        }
//                    },
//                    // on below line we are adding padding.
//                    modifier = Modifier.padding(5.dp),
//                )
            }

        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(30.dp)
            ) {
                AnimatedVisibility(visible = showTimerButton) {
                    AlarmButton(alarmButtonOnClick = alarmButtonOnClick, buttonScale = buttonScale)
                }
                AnimatedVisibility(visible = showAlarmButton) {
                    TimerButton(buttonScale = buttonScale)
                }

            }
        }


    }

}


@Composable
fun PortraitBaseClock(
    showSeconds: Boolean,
    clockFormatIsTwelveHour: Boolean,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    clockScale: Float,
    buttonScale: Float,
    showAlarmPopup: MutableState<Boolean>,
    showAlarmButton: Boolean,
    showTimerPopup: MutableState<Boolean>,
    showTimerButton: Boolean,
    alarmButtonOnClick: () -> Unit
) {
//    var clockNumberFormat = if (showSeconds) {
//        "hh:mm:ss"
//    } else {
//        "hh:mm"
//    }
//    if (clockFormatIsTwelveHour) {
//        clockNumberFormat = clockNumberFormat.plus(" a")
//    }

    Text(text = "P")
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(10.dp)) {
        AnimatedVisibility(visible = showAlarmPopup.value) {
            PopupWindowDialog()
        }
        AnimatedVisibility(visible = showTimerPopup.value) {
            PopupWindowDialog()
        }
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxHeight()) {
            Column(horizontalAlignment = Alignment.Start, modifier = Modifier.weight(0.5f)) {
                BackButton(onBackClick = onBackClick)
            }
            Column(horizontalAlignment = Alignment.End, modifier = Modifier.weight(0.5f)) {
                SettingsButton(onSettingsClick = onSettingsClick)
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(0.8f)
            ) {
                ClockText(
                    showSeconds = showSeconds,
                    clockFormatIsTwelveHour = clockFormatIsTwelveHour,
                    clockSize = 40.sp * clockScale
                )
//                AndroidView(
//                    // on below line we are initializing our text clock.
//                    factory = { context ->
//                        TextClock(context).apply {
//                            // on below line we are setting 12 hour format.
//                            if (clockFormatIsTwelveHour) {
//                                format12Hour?.let { this.format12Hour = clockNumberFormat }
//                            } else {
//                                format24Hour?.let { this.format24Hour = clockNumberFormat }
//                            }
//                            // on below line we are setting time zone.
//                            timeZone?.let { this.timeZone = it }
//                            // on below line we are setting text size.
//                            textSize.let { this.textSize = 60f * clockScale }
//
//                        }
//                    },
//                    // on below line we are adding padding.
//                    modifier = Modifier.padding(5.dp),
//                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End,

            modifier = Modifier.fillMaxSize()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(30.dp)
            ) {
                AnimatedVisibility(visible = showTimerButton) {
                    AlarmButton(alarmButtonOnClick = alarmButtonOnClick, buttonScale = buttonScale)
                }
                AnimatedVisibility(visible = showAlarmButton) {
                    TimerButton(buttonScale = buttonScale)
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BaseClockPreviewPT() {
    GameClockTheme(AppTheme.Red) {
        BaseClockScreen(
            ClockViewModel(),
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}

@Preview(showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape")
@Composable
fun BaseClockPreviewLS() {
    GameClockTheme(AppTheme.Red) {
        BaseClockScreen(
            ClockViewModel(),
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}


@Composable
fun ClockText(
    showSeconds: Boolean,
    clockFormatIsTwelveHour: Boolean,
    clockSize: TextUnit

) {
    val currentTimeMillis = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = currentTimeMillis) {
        while (true) {
            delay(250)
            currentTimeMillis.longValue = System.currentTimeMillis()
        }
    }
    val clockFormat = if (clockFormatIsTwelveHour) 12 else 24

    val hoursUnformatted = TimeUnit.MILLISECONDS.toHours(currentTimeMillis.longValue) % clockFormat
    val hours = if (hoursUnformatted < 10) "0$hoursUnformatted" else "$hoursUnformatted"
    val timeSuffix = if (TimeUnit.MILLISECONDS.toHours(currentTimeMillis.longValue) % 24 <= 12
    ) "am" else "pm"

    val minutesUnformatted = TimeUnit.MILLISECONDS.toMinutes(currentTimeMillis.longValue) % 60
    val minutes = if (minutesUnformatted < 10) "0$minutesUnformatted" else "$minutesUnformatted"


    val secondsUnformatted = TimeUnit.MILLISECONDS.toSeconds(currentTimeMillis.longValue) % 60
    val seconds = if (secondsUnformatted < 10) "0$secondsUnformatted" else "$secondsUnformatted"


    Text(
        text = "$hours:$minutes${
            if (showSeconds) {
                ":$seconds"
            } else ""
        }${
            if (clockFormatIsTwelveHour) {
                " $timeSuffix"
            } else ""
        }",

//  would have used below, however no seconds allowed for that and it can change format based on region.
//            DateUtils.formatDateTime(LocalContext.current, currentTimeMillis.value,
//                DateUtils.FORMAT_SHOW_TIME
//            )
        letterSpacing = 2.sp,
        modifier = Modifier
            .padding(8.dp, 8.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = clockSize,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        lineHeight = clockSize,
        textAlign = TextAlign.Center
    )

}

@Composable
fun BackButton(onBackClick: () -> Unit) {
    IconButton(
        onClick = onBackClick,

        ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun SettingsButton(
    onSettingsClick: () -> Unit,
) {
    IconButton(onClick = onSettingsClick) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.settings_button),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun AlarmButton(alarmButtonOnClick: () -> Unit, buttonScale: Float) {
    IconButton(
        onClick = alarmButtonOnClick,
        modifier = Modifier.size(80.dp * buttonScale)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_alarm_24),
            contentDescription = stringResource(R.string.alarms_button),
            modifier = Modifier.size(50.dp * buttonScale),
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun TimerButton(buttonScale: Float) {
    IconButton(
        onClick = { /*TODO*/ },
        modifier = Modifier.size(80.dp * buttonScale)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_hourglass_empty_24),
            contentDescription = stringResource(R.string.timers_button),
            modifier = Modifier.size(50.dp * buttonScale),
            tint = MaterialTheme.colorScheme.onBackground
        )

    }
}


/**
 * TODO - Set up alarm time picker with quick buttons to add set times.
 * TODO - Animate popup from clock button.
 * TODO - duplicate for timer picker
 */
@Composable
fun PopupWindowDialog() {
    // on below line we are creating variable for button title
    // and open dialog.
    // on below line we are specifying height and width
    val popupWidth = 300.dp
    val popupHeight = 100.dp

    // on below line we are creating a box to display box.
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {


        // on below line we are adding pop up
        Popup(
            // on below line we are adding
            // alignment and properties.
            alignment = Alignment.Center,
            properties = PopupProperties()
        ) {

            // on the below line we are creating a box.
            Box(
                // adding modifier to it.
                Modifier
                    .size(popupWidth, popupHeight)
                    .padding(top = 5.dp)
                    // on below line we are adding background color
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        RoundedCornerShape(10.dp)
                    )
                    // on below line we are adding border.
                    .border(1.dp, color = Color.Black, RoundedCornerShape(10.dp))
            ) {

                // on below line we are adding column
                Column(
                    // on below line we are adding modifier to it.
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 20.dp),
                    // on below line we are adding horizontal and vertical
                    // arrangement to it.
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // on below line we are adding text for our pop up
                    Text(
                        // on below line we are specifying text
                        text = "Welcome to Geeks for Geeks",
                        // on below line we are specifying color.

                        // on below line we are adding padding to it
                        modifier = Modifier.padding(vertical = 5.dp),
                        // on below line we are adding font size.
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

