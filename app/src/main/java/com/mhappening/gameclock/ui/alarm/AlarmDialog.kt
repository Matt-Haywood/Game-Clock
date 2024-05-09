package com.mhappening.gameclock.ui.alarm


import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

/**
 * TODO - Animate popup from clock button.
 * TODO - duplicate for timer picker
 */

/**
 * This function is a Composable that displays a dialog for setting an alarm.
 * It includes a TimePicker and a DateSelector, and buttons for confirming or cancelling the alarm.
 *
 * @param alarmViewModel The ViewModel that holds the state of the alarm.
 * @param onCancel The function to call when the "Cancel" button is clicked.
 * @param onConfirm The function to call when the "Set Alarm" button is clicked.
 * @param onConfirmText The text to display on the "Confirm" button. Defaults to "Set Alarm".
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
    val dateListState = alarmUiState.dateListState
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    val surfaceModifier = if (isLandscape) {
        Modifier
            .padding(5.dp)
            .width(650.dp)
            .height(345.dp)

    } else {
        Modifier
            .padding(5.dp)
            .width(373.dp)
            .height(500.dp)
    }

    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            shadowElevation = 6.dp,
            modifier = surfaceModifier
        ) {

            Row {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(5.dp)
                    ) {
                        TimePicker(state = alarmTimePickerState)

                            DateSelector(
                                dateListState = dateListState,
//                                dateList = alarmUiState.dateList,
                                alarmViewModel = alarmViewModel
                            )

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
                        TextButton(
                            onClick = onConfirm,
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


/**
 * This function is a Composable that displays a selector for choosing a date.
 * It displays a list of dates, and the user can scroll through the list to select a date.
 *
 * @param alarmViewModel The ViewModel that holds the state of the alarm.
 * @param dateListState The state of the list of dates.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DateSelector(
    alarmViewModel: AlarmViewModel,
    dateListState: LazyListState,
) {
    val TAG = "DateSelector"
    val alarmUiState by alarmViewModel.alarmUiState.collectAsState()
    var dateList = alarmUiState.dateList

    val calenderNext = Calendar.getInstance()
    val format = SimpleDateFormat("dd/MM", Locale.getDefault())

    var firstVisibleItemIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(dateListState) {
        snapshotFlow { dateListState.firstVisibleItemIndex }
            .collectLatest { index ->
                firstVisibleItemIndex = index
//                Log.i(TAG, "DateSelector: firstVisibleItemIndex: $firstVisibleItemIndex")
            }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(start = 10.dp)
//            .fillMaxHeight()
            .width(80.dp)
    ) {
        Box(contentAlignment = Alignment.TopCenter, modifier = Modifier.height(120.dp)
//            .offset(y = 80.dp)
        ) {
            LaunchedEffect(dateListState) {
                snapshotFlow { dateListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collectLatest { lastIndex ->
//                    Log.i(TAG, "DateSelector: lastIndex: $lastIndex, dateList: ${dateList.size}")
                        if (lastIndex != null) {
                            if (lastIndex >= dateList.size - 1) {
                                val lastDate = dateList.last()
                                Log.i(
                                    TAG,
                                    "lastIndex: $lastIndex, lastDate: $lastDate, dateList: ${dateList.size}"
                                )
                                calenderNext.time = lastDate
                                calenderNext.add(Calendar.DAY_OF_MONTH, 1)
                                alarmViewModel.addToDateList(calenderNext.time)
                                dateList = alarmUiState.dateList
                            }
                        }
                    }
            }

            LazyColumn(
                state = dateListState,
                flingBehavior = rememberSnapFlingBehavior(lazyListState = dateListState),
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(dateList) { index, date ->
                    val textColor = if (index == firstVisibleItemIndex) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                    val surfaceColor = if (index == firstVisibleItemIndex) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                    val text = when (index) {
                        0 -> "Today"
//                    1 -> "Tomorrow"
                        else -> format.format(date)
                    }
                    Surface(
                        color = surfaceColor,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = text,
                                color = textColor,
                                style = MaterialTheme.typography.bodyLarge
//                        modifier = Modifier.padding(5.dp)
                            )
                        }

                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
//                            MaterialTheme.colorScheme.surface,
                                Color.Transparent,
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            ),
                            startY = 0f,
                            endY = 300f // Adjust the endY to control the fade effect
                        )
                    )
            )
        }
    }
}

/**
 * Deprecated - Repeating alarms have been descoped to achieve MVP.
 * the repeat column can be used to display either a column or a lazy column of chips that allow
 * users to select which days of the week they want the alarm to repeat on.
 */
/*@Composable
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
            */
/**
 * Repeating alarms have been descoped to achieve MVP.
 *//*
            *//*ChipWithLabel(
                label = "Repeat",
                selected = alarmUiState.newAlarm.isRecurring,
                onClick = { alarmViewModel.toggleAlarmRecurring() },
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Refresh,
                        contentDescription = "Recurring",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                })*//*
            daysOfWeek.forEachIndexed { index, day ->
                Row {
                    ChipWithLabel(
                        label = day,
                        selected = checkedStates[index],
                        onClick = {
                            checkedStates[index] = !checkedStates[index]
                            alarmViewModel.toggleAlarmDay(day)
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Filled.Done,
                                contentDescription = "$day selected",
                                modifier = Modifier.size(FilterChipDefaults.IconSize)
                            )
                        }
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
        *//*daysOfWeek.forEachIndexed { index, day ->
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
        }*//*
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
}*/


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
