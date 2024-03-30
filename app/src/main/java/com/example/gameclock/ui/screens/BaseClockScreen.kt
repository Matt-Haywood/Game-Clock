package com.example.gameclock.ui.screens

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gameclock.R
import com.example.gameclock.data.alarms.SetAlarm
import com.example.gameclock.model.Alarm
import com.example.gameclock.model.AppTheme
import com.example.gameclock.ui.ClockUiState
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.alarm.AlarmPickerDialog
import com.example.gameclock.ui.alarm.AlarmViewModel
import com.example.gameclock.ui.screens.backgrounds.BackgroundBreathingEllipse
import com.example.gameclock.ui.screens.backgrounds.BackgroundGreenNumbers
import com.example.gameclock.ui.screens.backgrounds.BackgroundPixelFire
import com.example.gameclock.ui.theme.GameClockTheme
import com.example.gameclock.ui.theme.Roboto
import com.example.gameclock.ui.theme.SplineSansMono
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.util.concurrent.TimeUnit

//TODO make a background for one theme.

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseClockScreen(
    clockViewModel: ClockViewModel,
    alarmViewModel: AlarmViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isLandscape: Boolean = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
) {
    BackHandler(onBack = onBackClick)
    val clockUiState by clockViewModel.clockUiState.collectAsState()
    val alarmUiState by alarmViewModel.alarmUiState.collectAsState()
    val alarmList = alarmUiState.alarmsList
    val alarmTimePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = (LocalTime.now().minute + 1) % 60,
        is24Hour = !clockUiState.clockFormatIsTwelveHour
    )
    val context = LocalContext.current

    when (clockUiState.theme) {
        AppTheme.Default -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
        }

        AppTheme.Red -> {
            BackgroundPixelFire()
        }

        AppTheme.Light -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
        }

        AppTheme.Dark -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
        }

        AppTheme.CodeFall -> {
            BackgroundGreenNumbers(clockUiState = clockUiState)
        }

        else -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)

        }

    }
//    TextRatioTest()
//    BackgroundBreathingEllipse(clockUiState = uiState)
//    BackgroundGreenNumbers(clockUiState = uiState)

    if (isLandscape) {
        LandscapeBaseClock(
            clockViewModel = clockViewModel,
            alarmViewModel = alarmViewModel,
            clockUiState = clockUiState,
            timePickerState = alarmTimePickerState,
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick,
            alarmList = alarmList,
            alarmTimePickerOnConfirm = {
                SetAlarm(
                    context = context,
                    hour = alarmTimePickerState.hour,
                    minute = alarmTimePickerState.minute
                )
                clockViewModel.dismissAlarmPickerPopup()
            },
            timerTimePickerOnConfirm = { clockViewModel.dismissTimerPickerPopup() },
        )
    } else {
        PortraitBaseClock(
            clockViewModel = clockViewModel,
            alarmViewModel = alarmViewModel,
            clockUiState = clockUiState,
            timePickerState = alarmTimePickerState,
            onBackClick = onBackClick,
            onSettingsClick = onSettingsClick,
            alarmList = alarmList,
            alarmTimePickerOnConfirm = {
                SetAlarm(
                    context = context,
                    hour = alarmTimePickerState.hour,
                    minute = alarmTimePickerState.minute
                )
                clockViewModel.dismissAlarmPickerPopup()
            },
            timerTimePickerOnConfirm = { clockViewModel.dismissTimerPickerPopup() },
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LandscapeBaseClock(
    clockViewModel: ClockViewModel,
    alarmViewModel: AlarmViewModel,
    clockUiState: ClockUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    alarmList: List<Alarm>,
    timePickerState: TimePickerState,
    alarmTimePickerOnConfirm: () -> Unit,
    timerTimePickerOnConfirm: () -> Unit,
) {
    val showSeconds = clockUiState.showSeconds
    val clockFormatIsTwelveHour = clockUiState.clockFormatIsTwelveHour
    val clockScale = clockUiState.clockScale
    val buttonScale = clockUiState.buttonsScale
    val showAlarmButton = clockUiState.showAlarmButton
    val showTimerButton = clockUiState.showTimerButton
    val showAlarmPopup = clockUiState.showAlarmPickerPopup
    val showTimerPopup = clockUiState.showTimerPickerPopup

    Text(text = "L")
    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = showAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { clockViewModel.dismissAlarmPickerPopup() },
                onConfirm = alarmTimePickerOnConfirm,
                alarmTimePickerState = timePickerState
            )
        }
        AnimatedVisibility(visible = showTimerPopup) {

        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()

        ) {
            ClockText(
                showSeconds = showSeconds,
                clockFormatIsTwelveHour = clockFormatIsTwelveHour,
                clockSize = clockScale,
                isLandscape = true
            )
            LazyColumn {

                items(alarmList) { alarm ->
                    Text(text = alarm.title)
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        start = (30 / buttonScale).dp,
                        top = (30 / buttonScale).dp,
                        bottom = (30 / buttonScale).dp
                    )
            ) {
                BackButton(onBackClick = onBackClick, buttonScale = buttonScale)
                SettingsButton(onSettingsClick = onSettingsClick, buttonScale = buttonScale)
            }

            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End,
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(
                        end = (30 / buttonScale).dp,
                        top = (30 / buttonScale).dp,
                        bottom = (30 / buttonScale).dp
                    )
            ) {
                AnimatedVisibility(visible = showTimerButton) {
                    AlarmButton(
                        alarmButtonOnClick = { clockViewModel.toggleAlarmPickerPopup() },
                        buttonScale = buttonScale
                    )
                }
                AnimatedVisibility(visible = showAlarmButton) {
                    TimerButton(
                        timerButtonOnClick = { clockViewModel.toggleTimerPickerPopup() },
                        buttonScale = buttonScale
                    )
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PortraitBaseClock(
    clockViewModel: ClockViewModel,
    alarmViewModel: AlarmViewModel,
    clockUiState: ClockUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    alarmList: List<Alarm>,
    timePickerState: TimePickerState,
    alarmTimePickerOnConfirm: () -> Unit,
    timerTimePickerOnConfirm: () -> Unit,
) {
    val showSeconds = clockUiState.showSeconds
    val clockFormatIsTwelveHour = clockUiState.clockFormatIsTwelveHour
    val clockScale = clockUiState.clockScale
    val buttonScale = clockUiState.buttonsScale
    val showAlarmButton = clockUiState.showAlarmButton
    val showTimerButton = clockUiState.showTimerButton
    val showAlarmPopup = clockUiState.showAlarmPickerPopup
    val showTimerPopup = clockUiState.showTimerPickerPopup

    Text(text = "P")
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AnimatedVisibility(visible = showAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { clockViewModel.dismissAlarmPickerPopup() },
                onConfirm = alarmTimePickerOnConfirm,
                alarmTimePickerState = timePickerState
            )
        }
        AnimatedVisibility(visible = showTimerPopup) {

        }
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp * buttonScale.toInt())
        ) {
            BackButton(onBackClick = onBackClick)
            SettingsButton(onSettingsClick = onSettingsClick)
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .weight(0.8f)
//            ) {
            ClockText(
                showSeconds = showSeconds,
                clockFormatIsTwelveHour = clockFormatIsTwelveHour,
                clockSize = clockScale,
                isLandscape = false
            )

            LazyColumn {

                items(alarmList) { alarm ->
                    Text(text = alarm.title)
                }
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
                    AlarmButton(
                        alarmButtonOnClick = { clockViewModel.toggleAlarmPickerPopup() },
                        buttonScale = buttonScale
                    )
                }
                AnimatedVisibility(visible = showAlarmButton) {
                    TimerButton(
                        timerButtonOnClick = { clockViewModel.toggleTimerPickerPopup() },
                        buttonScale = buttonScale
                    )
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
            alarmViewModel = viewModel(factory = AlarmViewModel.Factory),
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
            alarmViewModel = viewModel(factory = AlarmViewModel.Factory),
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
    val TAG = "ClockText"
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
            timeSuffix
        } else ""
    }"
    // Estimate the maximum text size based on the screen width and the number of characters in the text
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val estimatedMaxTextSize = screenWidth * clockSize * (if (isLandscape) {
        0.65
    } else 0.7) / text.length

//    Log.i(TAG, "ClockText: $estimatedMaxTextSize")
    Text(
        text = text,

//  would have used below, however no seconds allowed for that and it can change format based on region.
//            DateUtils.formatDateTime(LocalContext.current, currentTimeMillis.value,
//                DateUtils.FORMAT_SHOW_TIME
//            )
        letterSpacing = 2.sp,
        modifier = Modifier
            .padding(8.dp, 8.dp),
        color = MaterialTheme.colorScheme.onBackground,
        fontSize = estimatedMaxTextSize.sp,
        style = MaterialTheme.typography.headlineLarge,
//            .plus(
//                TextStyle(
//                    shadow = Shadow(
//                        color = Color.Cyan,
////                        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
//                        blurRadius = 100f
//                    )
//                )
//            ),
        maxLines = if (isLandscape) 1 else 2,
        lineHeight = estimatedMaxTextSize.sp,
        textAlign = TextAlign.Center,

//        shadow = Shadow(
//            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
//            blurRadius = 5f
//        ),
    )

}

@Composable
fun BackButton(
    onBackClick: () -> Unit,
    buttonScale: Float = 1f
) {
    IconButton(
        onClick = onBackClick,
        modifier = Modifier.size(60.dp * buttonScale)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(R.string.back_button),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(42.dp * buttonScale)
        )
    }
}

@Composable
fun SettingsButton(
    onSettingsClick: () -> Unit,
    buttonScale: Float = 1f
) {
    IconButton(
        onClick = onSettingsClick,
        modifier = Modifier.size(60.dp * buttonScale)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = stringResource(R.string.settings_button),
            tint = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.size(42.dp * buttonScale)
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
fun TimerButton(timerButtonOnClick: () -> Unit, buttonScale: Float) {
    IconButton(
        onClick = timerButtonOnClick,
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


@Composable
fun TextRatioTest() {
    val singleDigit = "1"
    val doubleDigit = "12"
    val tripleDigit = "123"
    val time = "12:34"
    val timeWithSeconds = "12:34:56"
    val timeWithAmPm = "12:34 PM"
    val timeWithSecondsAmPm = "12:34:56 PM"

    val textList = listOf(
        singleDigit,
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "0",
        doubleDigit,
        tripleDigit,
        time,
        timeWithSeconds,
        timeWithAmPm,
        timeWithSecondsAmPm
    )

    val fontFamilyList = mapOf<FontFamily, String>(

        Pair(Roboto, "Roboto"),

//        Pair(Wellfleet, "Wellfleet"),
        Pair(SplineSansMono, "Spline San"),
//        Pair(RobotoFlex, "Roboto Flex"),
//        Pair(Anek, "Anek"),
    )

    Row {
        fontFamilyList.forEach() { (fontFamily, string) ->
            Column(
                modifier = Modifier,
            ) {
                Row {
                    Text(
                        text = string,
                        fontFamily = fontFamily,
                        fontSize = 10.sp,
                        modifier = Modifier
                            .height(10.dp)
                            .width(40.dp)
//                        lineHeight = 10.sp,

                    )
                }
                textList.forEach() {
                    Row {
                        Text(
                            text = it,
                            fontFamily = fontFamily,
                            fontSize = 10.sp,
//                            lineHeight = 10.sp,

                        )
                    }
                }
                Row {
                    Text(
                        text = string,
                        fontFamily = fontFamily,
                        fontSize = 20.sp,
//                        lineHeight = 20.sp,

                    )
                }
                textList.forEach() {
                    Row {
                        Text(
                            text = it,
                            fontFamily = fontFamily,
                            fontSize = 20.sp,
//                            lineHeight = 20.sp,

                        )
                    }
                }
            }
        }
    }


}