package com.example.gameclock.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerLayoutType
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.ui.ClockUiState
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.screens.backgrounds.LightBackground
import com.example.gameclock.ui.theme.GameClockTheme
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

//TODO make a background for one theme.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseClockScreen(
    clockViewModel: ClockViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isLandscape: Boolean = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
) {
    BackHandler(onBack = onBackClick)
    val uiState by clockViewModel.uiState.collectAsState()
    val timePickerState = rememberTimePickerState(is24Hour = !uiState.clockFormatIsTwelveHour)
LightBackground(clockUiState = uiState)

    if (isLandscape) {
        LandscapeBaseClock(
            uiState = uiState,
            showAlarmPopup = uiState.showAlarmPickerPopup,
            timePickerState = timePickerState,
            showTimerPopup = uiState.showTimerPickerPopup,
            alarmButtonOnClick = {clockViewModel.toggleAlarmPickerPopup()},
            timerButtonOnClick = {clockViewModel.toggleTimerPickerPopup()},
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick,
            alarmTimePickerOnConfirm = { clockViewModel.dismissAlarmPickerPopup() },
            alarmTimePickerOnCancel = { clockViewModel.dismissAlarmPickerPopup()},
            timerTimePickerOnConfirm = { clockViewModel.dismissTimerPickerPopup() },
            timerTimePickerOnCancel = { clockViewModel.dismissTimerPickerPopup() }
        )
    } else {
        PortraitBaseClock(
            uiState = uiState,
            showAlarmPopup = uiState.showAlarmPickerPopup,
            timePickerState = timePickerState,
            showTimerPopup = uiState.showTimerPickerPopup,
            alarmButtonOnClick = {clockViewModel.toggleAlarmPickerPopup()},
            timerButtonOnClick = {clockViewModel.toggleTimerPickerPopup()},
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick,
            alarmTimePickerOnConfirm = { clockViewModel.dismissAlarmPickerPopup() },
            alarmTimePickerOnCancel = { clockViewModel.dismissAlarmPickerPopup()},
            timerTimePickerOnConfirm = { clockViewModel.dismissTimerPickerPopup() },
            timerTimePickerOnCancel = { clockViewModel.dismissTimerPickerPopup() }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeBaseClock(
    uiState: ClockUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    showAlarmPopup: Boolean,
    timePickerState: TimePickerState,
    showTimerPopup: Boolean,
    alarmButtonOnClick: () -> Unit,
    timerButtonOnClick: () -> Unit,
    alarmTimePickerOnConfirm: () -> Unit,
    alarmTimePickerOnCancel: () -> Unit,
    timerTimePickerOnConfirm: () -> Unit,
    timerTimePickerOnCancel: () -> Unit,
) {
    val showSeconds = uiState.showSeconds
    val clockFormatIsTwelveHour = uiState.clockFormatIsTwelveHour
    val clockScale = uiState.clockScale
    val buttonScale = uiState.buttonsScale
    val showAlarmButton = uiState.showAlarmButton
    val showTimerButton = uiState.showTimerButton

    Text(text = "L")
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showAlarmPopup) {
            TimePickerDialog(
                onCancel = alarmTimePickerOnCancel,
                onConfirm = alarmTimePickerOnConfirm
            ) {
                TimePicker(state = timePickerState, layoutType = TimePickerLayoutType.Horizontal)
            }
        }
        AnimatedVisibility(visible = showTimerPopup) {

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
                    clockSize = clockScale,
                    isLandscape = true
                )
            }
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitBaseClock(
    uiState: ClockUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    showAlarmPopup: Boolean,
    timePickerState: TimePickerState,
    showTimerPopup: Boolean,
    alarmButtonOnClick: () -> Unit,
    timerButtonOnClick: () -> Unit,
    alarmTimePickerOnConfirm: () -> Unit,
    alarmTimePickerOnCancel: () -> Unit,
    timerTimePickerOnConfirm: () -> Unit,
    timerTimePickerOnCancel: () -> Unit,
) {
    val showSeconds = uiState.showSeconds
    val clockFormatIsTwelveHour = uiState.clockFormatIsTwelveHour
    val clockScale = uiState.clockScale
    val buttonScale = uiState.buttonsScale
    val showAlarmButton = uiState.showAlarmButton
    val showTimerButton = uiState.showTimerButton

    Text(text = "P")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AnimatedVisibility(visible = showAlarmPopup) {
            TimePickerDialog(
                onCancel = alarmTimePickerOnCancel,
                onConfirm = alarmTimePickerOnConfirm
            ) {
                TimePicker(state = timePickerState)
            }
        }
        AnimatedVisibility(visible = showTimerPopup) {

        }
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            BackButton(onBackClick = onBackClick)
            SettingsButton(onSettingsClick = onSettingsClick)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.8f)
            ) {
                ClockText(
                    showSeconds = showSeconds,
                    clockFormatIsTwelveHour = clockFormatIsTwelveHour,
                    clockSize = clockScale,
                    isLandscape = false
                )
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
            clockViewModel = viewModel(factory = ClockViewModel.Factory),
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun BaseClockPreviewLS() {
    GameClockTheme(AppTheme.Red) {
        BaseClockScreen(
            clockViewModel = viewModel(factory = ClockViewModel.Factory),
            onBackClick = {},
            onSettingsClick = {}
        )
    }
}


@Composable
fun ClockText(
    showSeconds: Boolean,
    clockFormatIsTwelveHour: Boolean,
    clockSize: Float,
    isLandscape: Boolean

) {
    val currentTimeMillis = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    val refreshRate: Long = if (showSeconds) 250 else 1000

    LaunchedEffect(key1 = currentTimeMillis) {
        while (true) {
            delay(refreshRate)
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

    val text = "$hours:$minutes${
        if (showSeconds) {
            ":$seconds"
        } else ""
    }${
        if (clockFormatIsTwelveHour) {
            " $timeSuffix"
        } else ""
    }"
    // Estimate the maximum text size based on the screen width and the number of characters in the text
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val estimatedMaxTextSize = screenWidth * clockSize * (if (isLandscape) {
        0.65
    } else 0.8) / text.length

    Text(
        text = text,

//  would have used below, however no seconds allowed for that and it can change format based on region.
//            DateUtils.formatDateTime(LocalContext.current, currentTimeMillis.value,
//                DateUtils.FORMAT_SHOW_TIME
//            )
        letterSpacing = 2.sp,
        modifier = Modifier
            .padding(8.dp, 8.dp)
            .fillMaxWidth(),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = estimatedMaxTextSize.sp,
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 2,
        lineHeight = estimatedMaxTextSize.sp,
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

@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    toggle: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Box(
                modifier = Modifier.padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        modifier = Modifier,
//                            .fillMaxWidth()
//                            .padding(bottom = 5.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                content()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier

                        .fillMaxSize()
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = onConfirm) {
                        Text("Set Alarm")
                    }
                }
            }
        }
    }
}

