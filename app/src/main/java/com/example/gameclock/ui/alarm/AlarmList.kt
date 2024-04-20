package com.example.gameclock.ui.alarm


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.gameclock.R
import com.example.gameclock.model.Alarm


@Composable
fun AlarmListDialog(
    alarmViewModel: AlarmViewModel,
    alarmList: List<Alarm>,
    alarmOnClick: (Alarm) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()
    
    Dialog(onDismissRequest = { alarmViewModel.dismissAlarmListPopup() }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier
//                .width(IntrinsicSize.Min)
//                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
//                Modifier.fillMaxSize()
            ) {
                Text(
                    text = stringResource(R.string.alarms),
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 15.dp)

                )
                AlarmList(
                    alarmViewModel = alarmViewModel,
                    alarmList = alarmList,
                    alarmOnClick = alarmOnClick
                )

                FilledIconButton(onClick = { alarmViewModel.openSetAlarmPopup() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_alarm_24),
                        contentDescription = "Add Alarm",

                        )
                }
            }
        }
    }




}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmList(
    alarmViewModel: AlarmViewModel,
    alarmList: List<Alarm>,
    alarmOnClick: (Alarm) -> Unit
) {
    LazyColumn {
        items(alarmList, { alarm: Alarm -> alarm.id }) { alarm ->
            var isDismissed = false
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart || it == SwipeToDismissBoxValue.StartToEnd) {
                        alarmViewModel.deleteAlarm(alarm)
                        isDismissed = true
                    }
                    true
                }
            )

            LaunchedEffect(isDismissed) {
                dismissState.reset()
                isDismissed = false
            }

            val daysAlarmSet = alarmDaysParse(alarm)

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {},
                content = {
                    Card {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_alarm_24),
                                contentDescription = alarm.title,
                                modifier = Modifier.scale(0.5f)
                            )
                            Text(
                                text = alarm.title,
                                modifier = Modifier
                                    .clickable { alarmOnClick(alarm) }
                                    .padding(start = 10.dp, end = 30.dp))
                            for (i in 0 until stringResource(R.string.DaysOfWeek).length) {

                                Column(horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,) {

                                    Icon(
                                        painter = painterResource(id = R.drawable.baseline_circle_24),
                                        contentDescription = "Alarm set ${daysAlarmSet[i]}",
                                        modifier = Modifier.scale(0.3f),
                                        tint = if (daysAlarmSet[i] == '1') MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
                                    )

                                    Text(
                                        text = stringResource(R.string.DaysOfWeek)[i].toString(),
                                        color = if (daysAlarmSet[i] == '1') MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.apply {
                                            copy(
                                                alpha = 0.5f
                                            )
                                        },
                                        modifier = Modifier.padding(start = 5.dp)
                                    )
                                }
                            }
                    }

                    }
                },
            )
        }
    }
}

fun alarmDaysParse(alarm: Alarm): String {
    var days = ""

    days += if (alarm.daySetMon) "1" else "0"
    days += if (alarm.daySetTue) "1" else "0"
    days += if (alarm.daySetWed) "1" else "0"
    days += if (alarm.daySetThu) "1" else "0"
    days += if (alarm.daySetFri) "1" else "0"
    days += if (alarm.daySetSat) "1" else "0"
    days += if (alarm.daySetSun) "1" else "0"

    return days
}