package com.example.gameclock.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CaretProperties
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.gameclock.R
import com.example.gameclock.model.AppTheme
import com.example.gameclock.ui.AppScreen
import com.example.gameclock.ui.ClockViewModel
import com.example.gameclock.ui.theme.GameClockTheme


//TODO: add button to reset to defaults
@Composable
fun SettingsScreen(clockViewModel: ClockViewModel, onBackClick: () -> Unit) {
    BackHandler(onBack = onBackClick)
    val settingsTextWeight = 0.35f
    val uiState by clockViewModel.clockUiState.collectAsState()
    var clockScaleSlider by remember { mutableFloatStateOf(uiState.clockScale) }
    var buttonScaleSlider by remember { mutableFloatStateOf(uiState.buttonsScale) }
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BackButton(onBackClick = onBackClick)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = AppScreen.Settings.title),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SliderRow(
                sliderThumb = painterResource(id = R.drawable.baseline_access_time_filled_24),
                sliderValue = uiState.clockScale,
                onValueChange = { clockScaleSlider = it },
                onValueChangeFinished = { clockViewModel.updateClockScale(clockScaleSlider) },
                rowText = R.string.clock_scale,
                settingsTextWeight = settingsTextWeight
            )

            SliderRow(
                rowText = R.string.buttons_scale,
                sliderThumb = painterResource(id = R.drawable.buttons_24),
                sliderValue = uiState.buttonsScale,
                onValueChange = { buttonScaleSlider = it },
                onValueChangeFinished = { clockViewModel.updateButtonsScale(buttonScaleSlider) },
                settingsTextWeight = settingsTextWeight
            )

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.show_seconds,
                settingEnabled = uiState.showSeconds,
                onClick = { clockViewModel.toggleSeconds() })

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.animations,
                settingEnabled = uiState.showAnimations,
                onClick = { clockViewModel.toggleShowAnimations() })

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.alarm_button,
                settingEnabled = uiState.showAlarmButton,
                onClick = { clockViewModel.toggleAlarmButton() })

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.timer_button,
                settingEnabled = uiState.showTimerButton,
                onClick = { clockViewModel.toggleTimerButton() })

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.full_screen,
                settingEnabled = uiState.isFullScreen,
                onClick = { clockViewModel.toggleFullScreen() })
//TODO: Change clock format from toggle row to selection.
            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.clock_format,
                settingEnabled = !uiState.clockFormatIsTwelveHour,
                onClick = { clockViewModel.toggleClockTwelveHourFormat() },
                toggleOptions = Pair("12hr", "24hr")
            )

            ResetSettingsButton(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.reset_settings,
                onClick = { clockViewModel.resetThemeToDefaults() }
            )

            //TODO: GIVE OPTION OF FONT OVERRIDE
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.clock_font_override),
                    modifier = Modifier.weight(settingsTextWeight)
                )
            }

        }
    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SliderRow(
    rowText: Int,
    settingsTextWeight: Float,
    sliderThumb: Painter,
    sliderValue: Float,
    onValueChange: (Float) -> Unit,
    onValueChangeFinished: () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(rowText),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(settingsTextWeight)
                .padding(end = 8.dp)
        )
        Column(
            modifier = Modifier
                .weight(1f - settingsTextWeight)
                .padding(end = 16.dp)
        ) {
            val sliderContentDescription = stringResource(rowText)
            Slider(
                modifier = Modifier.semantics { contentDescription = sliderContentDescription },
                value = sliderValue,
                onValueChange = onValueChange,
                valueRange = 0.5f..2f,
                onValueChangeFinished = onValueChangeFinished,

                thumb = {
                    Label(
                        label = {
                            PlainTooltip(
                                caretProperties = CaretProperties(45.dp, 25.dp),
                                modifier = Modifier
                                    .wrapContentWidth()
                            ) {
                                Text("%.2f".format(it))
                            }
                        },


                        ) {
                        // Box around icon do stop bug where slider track scales with icon size due to padding.
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(ButtonDefaults.IconSize * 2.5f)
                        ) {
                            Icon(
                                painter = sliderThumb,
                                contentDescription = null,
                                modifier = Modifier.size(ButtonDefaults.IconSize * (sliderValue + 0.5f))

                            )
                        }
                    }
                }
            )
        }
    }
}


@Composable
fun ToggleRow(
    toggleOptions: Pair<String, String> = Pair("Off", "On"),
    settingsTextWeight: Float,
    rowText: Int,
    settingEnabled: Boolean,
    onClick: (Boolean) -> Unit
) {

//    val selectedIndex = if (settingEnabled) 0 else 1
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(rowText),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(settingsTextWeight)
                .padding(end = 8.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f - settingsTextWeight)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.3f)
                ) {
                    AnimatedVisibility(
                        visible = !settingEnabled,
                        enter = slideInHorizontally(initialOffsetX = { it / 2 }) + fadeIn(),
                        exit = slideOutHorizontally(targetOffsetX = { it / 2 }) + fadeOut()
                    ) {
                        Text(
                            text = toggleOptions.first,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.3f)
                ) {
                    Switch(checked = settingEnabled, onCheckedChange = onClick)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(0.3f)
                ) {
                    AnimatedVisibility(
                        visible = settingEnabled,
                        enter = slideInHorizontally() + fadeIn(),
                        exit = slideOutHorizontally() + fadeOut()
                    ) {
                        Text(
                            text = toggleOptions.second,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

//            SingleChoiceSegmentedButtonRow {
//                toggleOptions.forEachIndexed { index, label ->
//                    SegmentedButton(
//                        selected = index == selectedIndex,
//                        onClick = onClick,
//                        shape = SegmentedButtonDefaults.itemShape(
//                            index = index,
//                            count = toggleOptions.size
//                        )
//                    ) {
//                        Text(text = label)
//                    }
//                }
//
//            }

        }
    }
}

@Composable
fun ResetSettingsButton(
    settingsTextWeight: Float,
    rowText: Int,
    onClick: () -> Unit
) {
    var isSettingsResetDialogVisible = remember { mutableStateOf(false) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = stringResource(rowText),
            textAlign = TextAlign.End,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier
                .weight(settingsTextWeight)
                .padding(end = 8.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f - settingsTextWeight)
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = { isSettingsResetDialogVisible.value = true }) {
                    Text(text = stringResource(R.string.reset_settings))
                }
            }
        }
    }
    AnimatedVisibility(visible = isSettingsResetDialogVisible.value) {
        Dialog(
            onDismissRequest = { isSettingsResetDialogVisible.value = false },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = true,
                usePlatformDefaultWidth = true
            )
        ) {
            Surface(
                shape = MaterialTheme.shapes.extraLarge,
                tonalElevation = 6.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surface
                    ),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = stringResource(R.string.reset_settings_confirmation),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.fillMaxWidth()
                    ){
                        TextButton(onClick = {
                            onClick(); isSettingsResetDialogVisible.value = false
                        }) {
                            Text(text = stringResource(R.string.confirm))
                        }
                        TextButton(onClick = { isSettingsResetDialogVisible.value = false }) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true,
)
@Composable
fun SettingsScreenPreviewPT() {
    GameClockTheme(AppTheme.Red) {
        SettingsScreen(
            clockViewModel = viewModel(factory = ClockViewModel.Factory),
            onBackClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Composable
fun SettingsScreenPreviewLS() {
    GameClockTheme(AppTheme.Red) {
        SettingsScreen(
            clockViewModel = viewModel(factory = ClockViewModel.Factory),
            onBackClick = {}
        )
    }
}