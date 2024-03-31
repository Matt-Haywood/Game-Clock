package com.example.gameclock.ui.alarm

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.gameclock.model.Alarm

/**
 * TODO - Set up alarm time picker with quick buttons to add set times.
 * TODO - Animate popup from clock button.
 * TODO - duplicate for timer picker
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmPickerDialog(
    alarmViewModel: AlarmViewModel,
    title: String = "Select Time",
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    alarmTimePickerState: TimePickerState,
) {
    val context = LocalContext.current
    Dialog(
        onDismissRequest = onCancel,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                ),
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier,
//                            .fillMaxWidth()
//                            .padding(bottom = 5.dp),
                        text = title,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
                TimePicker(state = alarmTimePickerState)
//                content()
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom,
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    TextButton(onClick = onCancel) {
                        Text("Cancel")
                    }
                    TextButton(onClick = {
                        alarmViewModel.setAlarm(
                            context = context,
                            alarm = Alarm(
                                minute = alarmTimePickerState.minute.toString(),
                                hour = alarmTimePickerState.hour.toString(),
                                title = "${alarmTimePickerState.hour}:${alarmTimePickerState.minute}",
                                isEnabled = true
                            )
                        )
                        onConfirm()
                    }) {
                        Text("Set Alarm")
                    }
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//@Preview
//fun AlarmPickerDialogPreview() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//    ) {
//        AlarmPickerDialog(
//            alarmViewModel = ViewModel(),
//            alarmTimePickerState = TimePickerState,
//            onCancel = {},
//            onConfirm = {}
//        )
//    }
//}