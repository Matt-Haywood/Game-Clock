package com.mhappening.gameclock.ui.alarm


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.mhappening.gameclock.R
import com.mhappening.gameclock.model.Alarm

//TODO: make sure add alarm button doesn't disappear off the bottom of  the list.
@Composable
fun AlarmListDialog(
    alarmViewModel: AlarmViewModel,
    alarmList: List<Alarm>,
    alarmOnClick: (Alarm) -> Unit,
) {
    Dialog(onDismissRequest = { alarmViewModel.dismissAlarmListPopup() }) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(10.dp)
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
                Spacer(modifier = Modifier.padding(10.dp))
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
    val sortedAlarmList = alarmList.sortedBy { it.date }
    LazyColumn() {
        items(sortedAlarmList, { alarm: Alarm -> alarm.alarmId }) { alarm ->
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

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                            contentDescription = stringResource(R.string.swipe_to_delete)
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_delete_forever_24),
                            contentDescription = stringResource(R.string.swipe_to_delete)
                        )
                    }
                },
                modifier = Modifier.padding(top = 10.dp),
                content = {
                    Card(modifier = Modifier.height(48.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_alarm_24),
                                contentDescription = alarm.title,
                                modifier = Modifier.scale(0.5f)
                            )
                            Text(
                                text = alarm.title.removePrefix("Alarm: "),
                                style = MaterialTheme.typography.bodyLarge,
                                textAlign = TextAlign.Justify,
                                modifier = Modifier
                                    .clickable { alarmOnClick(alarm) }
                                    .padding(start = 10.dp, end = 30.dp, top = 5.dp, bottom = 5.dp))
                        }
                    }
                },
            )
        }
    }
}

