package com.example.gameclock.ui.alarm


import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.textButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

/**
 * TODO - Animate popup from clock button.
 * TODO - duplicate for timer picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmPickerDialog(
    alarmViewModel: AlarmViewModel,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onConfirmText: String = "Set Alarm",
) {
    val alarmUiState by alarmViewModel.alarmUiState.collectAsState()

    val alarmTimePickerState = alarmUiState.timePickerState
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val surfaceModifier = if (!isLandscape) {
        Modifier
            .width(IntrinsicSize.Min)
            .height(IntrinsicSize.Min)
    } else {
        Modifier
            .padding(horizontal = 100.dp, vertical = 0.dp)
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = surfaceModifier
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {

            Row {
                RepeatColumn(
                    alarmViewModel = alarmViewModel,
                    alarmUiState = alarmUiState,
                    isLandscape = isLandscape
                )
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        modifier = Modifier
                            .padding(10.dp)
//                            .scale(0.9f)
                    ) {
                        TimePicker(state = alarmTimePickerState)

                    }
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        TextButton(
                            onClick = onCancel,
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text("Cancel")
                        }
//                        ChipWithLabel(
//                            label = "Repeat?",
//                            selected = alarmUiState.newAlarm.isRecurring,
//                            onClick = { alarmViewModel.toggleAlarmRecurring() })
                        TextButton(
                            onClick =
//                            alarmViewModel.setNewAlarm(
////                                minute = alarmTimePickerState.minute.toString(),
////                                hour = alarmTimePickerState.hour.toString(),
////                                title = "${String.format("%02d", alarmTimePickerState.hour)}:${String.format("%02d", alarmTimePickerState.minute)}",
//                                )
                            onConfirm,
                            colors = ButtonDefaults.textButtonColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Text(onConfirmText)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RepeatColumn(
    alarmViewModel: AlarmViewModel,
    alarmUiState: AlarmUiState,
    isLandscape: Boolean,
) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val checkedStates = remember { mutableStateListOf(*Array(daysOfWeek.size) { false }) }
    if (!isLandscape) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
//            .padding(start = 2.dp)
                .fillMaxHeight()
                .width(95.dp)
        ) {
            ChipWithLabel(
                label = "Repeat",
                selected = alarmUiState.newAlarm.isRecurring,
                onClick = { alarmViewModel.toggleAlarmRecurring() },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Recurring",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                })
            daysOfWeek.forEachIndexed { index, day ->
                Row {
                    ChipWithLabel(
                        label = day,
                        selected = checkedStates[index],
                        onClick = {
                            checkedStates[index] = !checkedStates[index]
                            alarmViewModel.toggleAlarmDay(day)
                        },
                        icon = {Icon(
                            imageVector = Icons.Filled.Done,
                            contentDescription = "$day selected",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )}
                    )
                }
            }
        }
    } else {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
//            .padding(start = 2.dp)
                .fillMaxHeight()
                .width(95.dp)
        ) {
            this.item {
                ChipWithLabel(
                    label = "Repeat",
                    selected = alarmUiState.newAlarm.isRecurring,
                    onClick = { alarmViewModel.toggleAlarmRecurring() },
                    icon = {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Recurring",
                            modifier = Modifier.size(FilterChipDefaults.IconSize)
                        )
                    })
            }
            this.items(daysOfWeek.size, key = { index -> daysOfWeek[index] }) { index ->
                Row {
                    ChipWithLabel(
                        label = daysOfWeek[index],
                        selected = checkedStates[index],
                        onClick = {
                            checkedStates[index] = !checkedStates[index]
                            alarmViewModel.toggleAlarmDay(daysOfWeek[index])
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "${daysOfWeek[index]} selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
                    )
                }
            }
        }
        /*daysOfWeek.forEachIndexed { index, day ->
            Row {
                ChipWithLabel(
                    label = day,
                    selected = checkedStates[index],
                    onClick = {
                        checkedStates[index] = !checkedStates[index]
                        alarmViewModel.toggleAlarmDay(day)
                    },
                    icon = {Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "$day selected",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )}
                )
            }
        }*/
    }
}


@Composable
fun ChipWithLabel(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable (() -> Unit)
) {
    FilterChip(
        label = { Text(text = label) },
        selected = selected,
        onClick = onClick,
        leadingIcon = if (selected) {
            {
                icon()
            }
        } else {
            null
        },
        modifier = Modifier.scale(scaleX = 0.9f, scaleY = 0.9f)
    )
}


/*
@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true)
fun AlarmPickerDialogPreview(@PreviewParameter(AlarmViewModelPreviewProvider::class) alarmViewModel: AlarmViewModel) {
    val alarmTimePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = (LocalTime.now().minute + 1) % 60,
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AlarmPickerDialog(
            alarmViewModel = alarmViewModel,
            alarmTimePickerState = alarmTimePickerState,
            onCancel = {},
            onConfirm = {}
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
fun AlarmPickerDialogPreviewLS(@PreviewParameter(AlarmViewModelPreviewProvider::class) alarmViewModel: AlarmViewModel) {
    val alarmTimePickerState = rememberTimePickerState(
        initialHour = LocalTime.now().hour,
        initialMinute = (LocalTime.now().minute + 1) % 60,
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        AlarmPickerDialog(
            alarmViewModel = alarmViewModel,
            alarmTimePickerState = alarmTimePickerState,
            onCancel = {},
            onConfirm = {}
        )
    }
}*/
