package com.mhappening.gameclock.ui.permissions

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.mhappening.gameclock.R
import com.mhappening.gameclock.ui.alarm.AlarmViewModel
import com.mhappening.gameclock.ui.util.PermissionsHelper

@Composable
fun PermissionsRequestDialog(alarmViewModel: AlarmViewModel) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = { alarmViewModel.dismissPermissionsRequestPopup() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            shape = MaterialTheme.shapes.large,
            shadowElevation = 6.dp,
            modifier = Modifier
                .height(intrinsicSize = IntrinsicSize.Min)
                .padding(5.dp)

        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = stringResource(R.string.permissions_request),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(text = stringResource(R.string.permission_request_text_1),
                        style = MaterialTheme.typography.bodyMedium)
                    Text(text = stringResource(R.string.permission_request_text_2),
                        style = MaterialTheme.typography.bodyMedium)
                }

                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { alarmViewModel.dismissPermissionsRequestPopup() },
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(stringResource(R.string.cancel),
                            style = MaterialTheme.typography.bodyMedium)
                    }
                    Button(
                        onClick = {
                            PermissionsHelper().requestPermissionsRedirect(context = context)
                            alarmViewModel.dismissPermissionsRequestPopup()
                        },
                        shape = RectangleShape,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            stringResource(R.string.request_permissions),
                            style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

