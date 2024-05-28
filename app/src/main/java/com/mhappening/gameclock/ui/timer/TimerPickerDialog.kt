package com.mhappening.gameclock.ui.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mhappening.gameclock.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@Composable
fun TimerPickerDialog(
    timerViewModel: TimerViewModel,
    onDismissRequest: () -> Unit = {},
    onTimerSet: () -> Unit = {},
    isSetTimerEnabled: Boolean = false
) {

    val timerUiState by timerViewModel.uiState.collectAsState()
    val localContentDescription = stringResource(R.string.timer_picker_dialog)


    val newTimerDurationSeconds = timerUiState.newTimerDurationSeconds
    val newTimerDurationSecondsInt = timerUiState.newTimerDurationSecondsInt

    val newTimerDurationMinutes = timerUiState.newTimerDurationMinutes
    val newTimerDurationMinutesInt = timerUiState.newTimerDurationMinutesInt

    val newTimerDurationHours = timerUiState.newTimerDurationHours
    val newTimerDurationHoursInt = timerUiState.newTimerDurationHoursInt

    val calendar = Calendar.getInstance()
    val now = calendar.timeInMillis
    val endTime = timerUiState.newTimer.endTime
    val hourMinuteFormat = SimpleDateFormat("HH:mm", Locale.UK)

//    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequester1 = remember { FocusRequester() }
    val focusRequester2 = remember { FocusRequester() }
    val focusRequester3 = remember { FocusRequester() }




    Dialog(
        onDismissRequest = {
            onDismissRequest()
            focusRequester1.freeFocus()
            focusRequester2.freeFocus()
            focusRequester3.freeFocus()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            shadowElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .padding(5.dp)
                .semantics { contentDescription = localContentDescription }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
                    .padding(2.dp)
            ) {

                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_notifications_active_24),
                        contentDescription = stringResource(
                            R.string.timer_end_time
                        ),
                        modifier = Modifier
                            .padding(end = 5.dp)
                            .scale(0.8f)
                    )
                    Text(text = hourMinuteFormat.format(endTime))
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilteredNumberField(
                        value = newTimerDurationHours.value,
                        onValueChange = {
                            newTimerDurationHours.value = it
                            timerViewModel.updateEndTime()
                                        },
                        allowedFirstValue = Regex(ZeroToNine),
                        allowedSecondValue = Regex(ZeroToNine),
                        onNext = {
                            focusRequester1.freeFocus()
                            focusRequester2.requestFocus()
                        },
                        imeAction = ImeAction.Next,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester1),
                        label = stringResource(R.string.hours),
                    )
                    TimeSplitter()
                    FilteredNumberField(
                        value = newTimerDurationMinutes.value,
                        onValueChange = { newTimerDurationMinutes.value = it
                            timerViewModel.updateEndTime()},
                        allowedFirstValue = Regex(ZeroToFive),
                        allowedSecondValue = Regex(ZeroToNine),
                        onNext = {
                            focusRequester2.freeFocus()
                            focusRequester3.requestFocus()
                        },
                        imeAction = ImeAction.Next,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester2),
                        label = stringResource(R.string.minutes),
                    )
                    TimeSplitter()
                    FilteredNumberField(
                        value = newTimerDurationSeconds.value,
                        onValueChange = { newTimerDurationSeconds.value = it
                            timerViewModel.updateEndTime()},
                        allowedFirstValue = Regex(ZeroToFive),
                        allowedSecondValue = Regex(ZeroToNine),
                        onNext = {
                            focusRequester3.freeFocus()
                            if (newTimerDurationSeconds.value.isEmpty()) {
                                newTimerDurationSeconds.value = "00"
                            }
                            if (newTimerDurationMinutes.value.isEmpty()) {
                                newTimerDurationMinutes.value = "00"
                            }
                            if (newTimerDurationHours.value.isEmpty()) {
                                newTimerDurationHours.value = "00"
                            }
                        },
                        imeAction = ImeAction.Done,
                        modifier = Modifier
                            .weight(1f)
                            .focusRequester(focusRequester3),
                        label = stringResource(R.string.seconds),
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
                        onClick = onDismissRequest,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Text(stringResource(id = R.string.cancel))
                    }
                    TextButton(
                        onClick = onTimerSet,
                        colors = ButtonDefaults.textButtonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ),
                        enabled = isSetTimerEnabled
                    ) {
                        Text(stringResource(R.string.go))
                    }
                }


            }
        }
    }
}

@Composable
fun FilteredNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    allowedFirstValue: Regex,
    allowedSecondValue: Regex,
    imeAction: ImeAction,
    modifier: Modifier = Modifier,
    onNext: () -> Unit = {},
    label: String = ""


) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = value,
        onValueChange = { newValue ->
            when {
                newValue.isEmpty() -> {
                    // Handle the case where the user deletes the text field
                    onValueChange(newValue)
                }

                newValue != value -> {
                    // Handle the case where the user overwrites the text field
                    if (newValue.length == 1 && newValue.contains(allowedSecondValue)) {
                        if (newValue.contains(allowedFirstValue)) {
                            onValueChange(newValue)
                        } else {
                            onValueChange("0$newValue")
                            onNext()
                        }
                    } else if (newValue.length == 2 && newValue[1].toString()
                            .contains(allowedSecondValue)
                    ) {
                        onValueChange(newValue)
                        onNext()
                    }
                }
            }
        },
        textStyle = MaterialTheme.typography.displayLarge.copy(textAlign = TextAlign.Center),
        label = {
            Text(
                text = label,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Clip,
            )
        },
        placeholder = {
            Text(
                text = "00",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.displayLarge
            )
        },
        modifier = modifier,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            autoCorrect = false,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onNext = {
            onNext()
            if (value.isEmpty()) {
                onValueChange("00")
            }
            if (value.length == 1) {
                onValueChange("0$value")
            }
        },
            onDone = {
                onNext()
                if (value.isEmpty()) {
                    onValueChange("00")
                }
                if (value.length == 1) {
                    onValueChange("0$value")
                }
                keyboardController?.hide()

            }
        )

    )
}

@Composable
fun TimeSplitter() {
    Text(
        text = ":",
        style = MaterialTheme.typography.displayLarge,
        modifier = Modifier.padding(2.dp)

        )
}