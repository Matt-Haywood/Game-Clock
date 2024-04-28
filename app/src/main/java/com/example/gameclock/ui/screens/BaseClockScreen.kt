package com.example.gameclock.ui.screens

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.gameclock.R
import com.example.gameclock.model.Alarm
import com.example.gameclock.model.AppTheme
import com.example.gameclock.model.ClockFormat
import com.example.gameclock.ui.ClockUiState
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.alarm.AlarmListDialog
import com.example.gameclock.ui.alarm.AlarmPickerDialog
import com.example.gameclock.ui.alarm.AlarmUiState
import com.example.gameclock.ui.alarm.AlarmViewModel
import com.example.gameclock.ui.screens.backgrounds.BackgroundBreathingEllipse
import com.example.gameclock.ui.screens.backgrounds.BackgroundDigitalRain
import com.example.gameclock.ui.screens.backgrounds.DvdBackground
import com.example.gameclock.ui.screens.backgrounds.PixelFireBackground
import com.example.gameclock.ui.screens.backgrounds.SpaceBackground
import com.example.gameclock.ui.theme.ClockFont
import com.example.gameclock.ui.theme.GameClockTheme
import kotlinx.coroutines.delay
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun BaseClockScreen(
    clockViewModel: ClockViewModel,
    alarmViewModel: AlarmViewModel,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    isLandscape: Boolean = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE,
) {
    val TAG = "BaseClockScreen"
    BackHandler(onBack = onBackClick)
    val clockUiState by clockViewModel.clockUiState.collectAsState()
    val alarmUiState by alarmViewModel.alarmUiState.collectAsState()
    alarmViewModel.getAlarmsList()
    val alarmList = alarmUiState.alarmsList

    when (clockUiState.theme) {
        AppTheme.Red -> {
            PixelFireBackground(showAnimations = clockUiState.showAnimations, isFullscreen = clockUiState.isFullScreen)
        }

        AppTheme.Light -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
        }

        AppTheme.Dark -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)
//            window.insetsController?.setSystemBarsAppearance(
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
        }

        AppTheme.CodeFall -> {
            BackgroundDigitalRain(clockUiState = clockUiState)
        }

        AppTheme.Space -> {
            SpaceBackground(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.DvdDark -> {
            DvdBackground(showAnimations = clockUiState.showAnimations)
        }

        AppTheme.DvdLight -> {
            DvdBackground(showAnimations = clockUiState.showAnimations)
        }

        else -> {
            BackgroundBreathingEllipse(clockUiState = clockUiState)

        }

    }
//    TextRatioTest()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable {  // Detect taps and show the buttons again
                clockViewModel.showButtons()
                Log.i(
                    TAG,
                    "BaseClockScreen: Clicked on the screen. Buttons are visible again."
                )
            }
    ) {
        if (isLandscape) {
            LandscapeBaseClock(
                clockViewModel = clockViewModel,
                clockUiState = clockUiState,
                alarmViewModel = alarmViewModel,
                alarmUiState = alarmUiState,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                alarmList = alarmList,
            )
        } else {
            PortraitBaseClock(
                clockViewModel = clockViewModel,
                clockUiState = clockUiState,
                alarmViewModel = alarmViewModel,
                alarmUiState = alarmUiState,
                onBackClick = onBackClick,
                onSettingsClick = onSettingsClick,
                alarmList = alarmList,
            )
        }
    }
}


@Composable
fun LandscapeBaseClock(
    clockViewModel: ClockViewModel,
    clockUiState: ClockUiState,
    alarmViewModel: AlarmViewModel,
    alarmUiState: AlarmUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    alarmList: List<Alarm>,
) {
    val clockFormat = clockUiState.clockFormat
    val clockScale = clockUiState.clockScale
    val buttonScale = clockUiState.buttonsScale
    val showAlarmButton = clockUiState.showAlarmButton
    val showTimerButton = clockUiState.showTimerButton
    val showSetAlarmPopup = alarmUiState.showSetAlarmPopup
    val showUpdateAlarmPopup = alarmUiState.showAlarmUpdatePopup
    val showTimerPopup = clockUiState.showTimerPickerPopup
    val buttonsVisible = clockUiState.buttonsVisible


    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = alarmUiState.showAlarmListPopup) {
            AlarmListDialog(
                alarmViewModel = alarmViewModel,
                alarmList = alarmList,
                alarmOnClick = { alarm -> alarmViewModel.openAlarmUpdatePopup(alarm) })
        }
        AnimatedVisibility(visible = showSetAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { alarmViewModel.dismissAlarmPickerPopup() },
                onConfirm = {
                    alarmViewModel.setNewAlarm()
                    alarmViewModel.dismissAlarmPickerPopup()
                },
            )
        }

        AnimatedVisibility(visible = showUpdateAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { alarmViewModel.dismissAlarmUpdatePopup() },
                onConfirm = {
                    alarmViewModel.updateAlarm()
                    alarmViewModel.dismissAlarmUpdatePopup()
                },
                onConfirmText = stringResource(R.string.updateAlarm)
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
                clockFormat = clockFormat,
                clockSize = clockScale,
                isLandscape = true,
                clockFont = clockUiState.clockFont
            )
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
                AnimatedVisibility(
                    visible = buttonsVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    HomeButton(onBackClick = onBackClick, buttonScale = buttonScale)
                }
                AnimatedVisibility(
                    visible = buttonsVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    SettingsButton(onSettingsClick = onSettingsClick, buttonScale = buttonScale)
                }
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
                AnimatedVisibility(
                    visible = showAlarmButton && buttonsVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    AlarmButton(
                        alarmButtonOnClick = {
                            if (alarmList.isNotEmpty()) {
                                alarmViewModel.openAlarmListPopup()
                            } else {
                                alarmViewModel.openSetAlarmPopup()
                            }
                        },
                        buttonScale = buttonScale
                    )
                }
/*                AnimatedVisibility(visible = showTimerButton && buttonsVisible) {
                    TimerButton(
                        timerButtonOnClick = { clockViewModel.toggleTimerPickerPopup() },
                        buttonScale = buttonScale
                    )
                }*/
            }
        }
    }
}


@Composable
fun PortraitBaseClock(
    clockViewModel: ClockViewModel,
    clockUiState: ClockUiState,
    alarmViewModel: AlarmViewModel,
    alarmUiState: AlarmUiState,
    onBackClick: () -> Unit,
    onSettingsClick: () -> Unit,
    alarmList: List<Alarm>,
) {
    val clockFormat = clockUiState.clockFormat
    val clockScale = clockUiState.clockScale
    val buttonScale = clockUiState.buttonsScale
    val showAlarmButton = clockUiState.showAlarmButton
    val showTimerButton = clockUiState.showTimerButton
    val showSetAlarmPopup = alarmUiState.showSetAlarmPopup
    val showUpdateAlarmPopup = alarmUiState.showAlarmUpdatePopup
    val showTimerPopup = clockUiState.showTimerPickerPopup
    val buttonsVisible = clockUiState.buttonsVisible


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        AnimatedVisibility(visible = alarmUiState.showAlarmListPopup) {
            AlarmListDialog(
                alarmViewModel = alarmViewModel,
                alarmList = alarmList,
                alarmOnClick = { alarm -> alarmViewModel.openAlarmUpdatePopup(alarm) })
        }
        AnimatedVisibility(visible = showSetAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { alarmViewModel.dismissAlarmPickerPopup() },
                onConfirm = {
                    alarmViewModel.setNewAlarm()
                    alarmViewModel.dismissAlarmPickerPopup()
                },
            )
        }

        AnimatedVisibility(visible = showUpdateAlarmPopup) {
            AlarmPickerDialog(
                alarmViewModel = alarmViewModel,
                onCancel = { alarmViewModel.dismissAlarmUpdatePopup() },
                onConfirm = {
                    alarmViewModel.updateAlarm()
                    alarmViewModel.dismissAlarmUpdatePopup()
                },
                onConfirmText = stringResource(R.string.updateAlarm)
            )
        }
        //TODO: update alarm dialog.
        //TODO: timer dialog.
        AnimatedVisibility(visible = showTimerPopup) {

        }

        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp * buttonScale.toInt())
        ) {
            AnimatedVisibility(
                visible = buttonsVisible,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                HomeButton(onBackClick = onBackClick)
            }
            AnimatedVisibility(
                visible = buttonsVisible,
                enter = scaleIn(),
                exit = scaleOut()
            ) {
                SettingsButton(onSettingsClick = onSettingsClick)
            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = clockFormat,
                clockSize = clockScale,
                isLandscape = false,
                clockFont = clockUiState.clockFont
            )

//            AlarmList(
//                alarmViewModel = alarmViewModel,
//                alarmList = alarmList,
//                alarmOnClick = { alarm -> alarmViewModel.openAlarmUpdatePopup(alarm) })
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
                AnimatedVisibility(
                    visible = showTimerButton && buttonsVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    AlarmButton(
                        alarmButtonOnClick = {
                            if (alarmList.isNotEmpty()) {
                                alarmViewModel.openAlarmListPopup()
                            } else {
                                alarmViewModel.openSetAlarmPopup()
                            }
//                            alarmViewModel.openSetAlarmPopup()
                        },
                        buttonScale = buttonScale
                    )
                }
                /*AnimatedVisibility(
                    visible = showAlarmButton && buttonsVisible,
                    enter = scaleIn(),
                    exit = scaleOut()
                ) {
                    TimerButton(
                        timerButtonOnClick = { clockViewModel.toggleTimerPickerPopup() },
                        buttonScale = buttonScale
                    )

                }*/
            }
        }
    }
}


@Composable
fun ClockText(
    clockFormat: ClockFormat,
    clockSize: Float,
    clockFont: ClockFont,
    isLandscape: Boolean
) {
//    val TAG = "ClockText"
    val currentTime = remember {
        mutableStateOf(ZonedDateTime.now(ZoneId.systemDefault()))
    }

    val formatHasSeconds = clockFormat.formatValue.contains("s", ignoreCase = true)

    // Refresh rate is 250ms if the clock format contains seconds, otherwise 1s
    val refreshRate: Long =
        if (formatHasSeconds) 250 else 1000

    LaunchedEffect(key1 = currentTime) {
        while (true) {
            delay(refreshRate)
            currentTime.value = ZonedDateTime.now(ZoneId.systemDefault())
        }
    }

    val formatter = DateTimeFormatter.ofPattern(clockFormat.formatValue)
    val formatIsVertical = clockFormat.formatTitle.contains("Vertical", ignoreCase = true)
    val clockSuffixFormatter = DateTimeFormatter.ofPattern(clockFormat.timeSuffix)
    val clockText = currentTime.value.format(formatter)
    val clockSuffix = currentTime.value.format(clockSuffixFormatter)

    val screenWidth = LocalConfiguration.current.screenWidthDp

    @Suppress("KotlinConstantConditions")
    val estimatedMaxTextSize = when {
        formatIsVertical && formatHasSeconds -> {
            screenWidth * (clockSize / 2) * (if (isLandscape) 0.12 else 0.4) * clockFont.fontScale
        }

        formatIsVertical && !formatHasSeconds -> {
            screenWidth * (clockSize / 2) * (if (isLandscape) 0.18 else 0.5) * clockFont.fontScale
        }

        !formatIsVertical && clockSuffix.isBlank() -> {
            screenWidth * (clockSize / clockText.length) * (if (isLandscape) 0.7 else 0.7) * clockFont.fontScale
        }

        else -> {
            screenWidth * (clockSize / clockText.length) * (if (isLandscape) 0.55 else 0.7) * clockFont.fontScale
        }
    }

    val totalClockHeight = when {
        formatIsVertical && formatHasSeconds -> estimatedMaxTextSize * clockFont.textBoxHeight * 3 * clockFont.fontTotalHeightPercentage
        formatIsVertical && !formatHasSeconds -> estimatedMaxTextSize * clockFont.textBoxHeight * 2 * clockFont.fontTotalHeightPercentage
        else -> estimatedMaxTextSize * clockFont.textBoxHeight * 1 * clockFont.fontTotalHeightPercentage
    }

    ConstraintLayout(
    ) {
        val (text, suffix) = createRefs()

        //split the clock text into individual lines
        val lines = clockText.split("\n")

        Box(modifier = Modifier
            .constrainAs(text) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
//            .height(totalClockHeight.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.requiredHeight(totalClockHeight.dp)
            ) {

                for (i in lines.indices) {
                    val line = lines[i]
                    val lineIndex = i + 1
                    val yOffset = if (lines.size == 1) {
                        0.dp
                    } else {
                        -(estimatedMaxTextSize * clockFont.fontYOffsetPercentage * lineIndex).dp
                    }
                    val requiredHeightModifier = if (lines.size == 1) {
                        Modifier
                    } else {
                        Modifier.requiredHeight((estimatedMaxTextSize * clockFont.textBoxHeight).dp)
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
//                        modifier = Modifier.height(estimatedMaxTextSize.dp)
                        modifier = requiredHeightModifier
                            .offset(y = yOffset)
                    ) {
                        for (element in line) {

                            val charWidth = if (element == ':') {
                                (estimatedMaxTextSize * clockFont.textBoxWidthHeightRatio * 0.6).dp
                            } else {
                                (estimatedMaxTextSize * clockFont.textBoxWidthHeightRatio).dp
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = requiredHeightModifier
                                    .requiredWidth(charWidth)
                            ) {
                                Text(
                                    text = element.toString(),
                                    color = MaterialTheme.colorScheme.onBackground,
                                    fontSize = estimatedMaxTextSize.sp,
                                    textAlign = TextAlign.Center,
                                    style = clockFont.textStyle,
//                                    modifier = Modifier.offset(y = yOffset)
                                )
                            }
                        }
                    }
                }
                if (clockSuffix.isNotEmpty() && !isLandscape) {
                    Row {
                        Text(
                            text = clockSuffix,
                            fontSize = (estimatedMaxTextSize * 0.4).sp,
                            style = clockFont.textStyle,
                            color = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.offset(y = -(estimatedMaxTextSize * clockFont.fontYOffsetPercentage * lines.size).dp)
                        )
                    }
                }
            }
        }
        if (clockSuffix.isNotEmpty() && isLandscape) {
            Text(
                text = clockSuffix,
                fontSize = (estimatedMaxTextSize * 0.4).sp,
                style = clockFont.textStyle,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.constrainAs(suffix) {
                    top.linkTo(text.top)
                    bottom.linkTo(text.bottom)
                    start.linkTo(text.end)
                }
            )
        }
    }
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
fun HomeButton(
    onBackClick: () -> Unit,
    buttonScale: Float = 1f
) {
    IconButton(
        onClick = onBackClick,
        modifier = Modifier.size(60.dp * buttonScale)
    ) {
        Icon(
            imageVector = Icons.Filled.Home,
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




const val previewFontScale: Float = 2f
val previewFont: ClockFont = ClockFont.TAC_ONE


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTTwelveHrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWELVE_HOUR,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTTwelveHrScPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWELVE_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PT24HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PT24HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTV12HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTV12HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTV24HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun PTV24HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = false,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSTwelveHrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWELVE_HOUR,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSTwelveHrScPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWELVE_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LS24HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LS24HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.TWENTY_FOUR_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSV12HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSV12HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWELVE_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSV24HrPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun LSV24HrSPreview() {
    GameClockTheme(AppTheme.Light) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxSize()
        ) {
            ClockText(
                clockFormat = ClockFormat.VERTICAL_TWENTY_FOUR_HOUR_WITH_SECONDS,
                clockSize = previewFontScale,
                isLandscape = true,
                clockFont = previewFont
            )
        }
    }
}



