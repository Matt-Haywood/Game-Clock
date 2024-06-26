package com.mhappening.gameclock.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CaretProperties
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mhappening.gameclock.R
import com.mhappening.gameclock.model.AppTheme
import com.mhappening.gameclock.model.ClockFormat
import com.mhappening.gameclock.ui.AppScreen
import com.mhappening.gameclock.ui.ClockViewModel
import com.mhappening.gameclock.ui.theme.ClockFont
import com.mhappening.gameclock.ui.theme.GameClockTheme


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
        },
        bottomBar = {
            Column {


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.app_version),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_copyright_24),
                        contentDescription = stringResource(
                            R.string.copyright_logo
                        )
                    )
                    Text(
                        text = stringResource(id = R.string.settings_bottom_bar),
                        style = MaterialTheme.typography.bodySmall,

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
            SettingsHeader(headerText = stringResource(R.string.app_wide_settings))

            ToggleRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.full_screen,
                settingEnabled = uiState.isFullScreen,
                onClick = { clockViewModel.toggleFullScreen() })

            SettingsHeader(headerText = stringResource(R.string.theme_settings))
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
            ChoiceRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.clock_format,
                currentChoice = uiState.clockFormat,
                choices = ClockFormat.entries.toList(),
                onChoiceChange = { choice ->
                    if (choice is ClockFormat) {
                        clockViewModel.updateClockFormat(choice)
                    }
                }
            )

            ChoiceRow(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.clock_font,
                currentChoice = uiState.clockFont,
                choices = ClockFont.entries.toList(),
                onChoiceChange = { choice ->
                    if (choice is ClockFont) {
                        clockViewModel.updateClockFont(choice)
                    }
                }
            )

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

//            ToggleRow(
//                settingsTextWeight = settingsTextWeight,
//                rowText = R.string.timer_button,
//                settingEnabled = uiState.showTimerButton,
//                onClick = { clockViewModel.toggleTimerButton() })


            ResetSettingsButton(
                settingsTextWeight = settingsTextWeight,
                rowText = R.string.reset_settings,
                onClick = { clockViewModel.resetThemeToDefaults() }
            )

            SettingsHeader(headerText = stringResource(R.string.other_settings))

            DeepLinkRow(settingsTextWeight = settingsTextWeight)


        }
    }

}


@Composable
fun SettingsHeader(headerText: String = "Test") {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(0.9f),
            color = MaterialTheme.colorScheme.onSurface
        )
        Text(
            text = headerText,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.surface)
        )


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
            modifier = Modifier
                .weight(1f - settingsTextWeight)
                .clickable { onClick(settingEnabled) }
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
        }
    }
}

@Composable
fun ChoiceRow(
    settingsTextWeight: Float,
    rowText: Int,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    currentChoice: Any,
    choices: List<Any>,
    onChoiceChange: (Any) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

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
            Box {
                TextButton(
                    onClick = { expanded = true },
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = when (currentChoice) {
                            is ClockFormat -> currentChoice.formatTitle
                            is ClockFont -> currentChoice.fontName
                            else -> ""
                        },
                        style = if (currentChoice is ClockFont) {
                            currentChoice.textStyle
                        } else textStyle
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    choices.forEach { choice ->
                        DropdownMenuItem(text = {
                            Text(
                                text = when (choice) {
                                    is ClockFormat -> choice.formatTitle
                                    is ClockFont -> choice.fontName
                                    else -> ""
                                },
                                style = if (choice is ClockFont) {
                                    choice.textStyle
                                } else textStyle
                            )
                        }, onClick = {
                            onChoiceChange(choice)
                            expanded = false
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun ResetSettingsButton(
    settingsTextWeight: Float,
    rowText: Int,
    onClick: () -> Unit
) {
    val isSettingsResetDialogVisible = remember { mutableStateOf(false) }
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
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = { isSettingsResetDialogVisible.value = true }) {
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
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp)
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
                    ) {
                        TextButton(
                            onClick = {
                                onClick(); isSettingsResetDialogVisible.value = false
                            },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )
                        ) {
                            Text(text = stringResource(R.string.confirm))
                        }
                        TextButton(
                            onClick ={ isSettingsResetDialogVisible.value = false },
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            )) {
                            Text(text = stringResource(R.string.cancel))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DeepLinkRow(
    settingsTextWeight: Float,
) {
    val context = LocalContext.current
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f - settingsTextWeight)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://matt-haywood.github.io/PrivacyPolicy/")
                        )
                        context.startActivity(intent)
                    }) {
                    Text(text = stringResource(R.string.privacy_policy))
                }
                TextButton(
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://www.app-privacy-policy.com/live.php?token=F4zVmSVTi2HRmzfVdTeKuBhPAmxl4wlA")
                        )
                        context.startActivity(intent)
                    }) {
                    Text(text = stringResource(R.string.eula))
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
    GameClockTheme(AppTheme.Fire) {
        SettingsScreen(
            clockViewModel = viewModel(),
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
    GameClockTheme(AppTheme.Fire) {
        SettingsScreen(
            clockViewModel = viewModel(),
            onBackClick = {}
        )
    }
}