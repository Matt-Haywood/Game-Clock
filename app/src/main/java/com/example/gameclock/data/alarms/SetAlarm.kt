package com.example.gameclock.data.alarms

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


private const val MY_PERMISSIONS_REQUEST_SET_ALARM = 1
private const val MY_PERMISSIONS_REQUEST_ACCESS_NOTIFICATION_POLICY = 1
private const val MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM = 1


fun SetAlarm(context: Context, hour: Int, minute: Int) {
    val TAG = "SetAlarm"
    val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    val alarmIntent = Intent(context, AlarmReceiver::class.java). let { intent ->
        PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
    }
//    val pendingAlarmIntent =
//        PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_IMMUTABLE)

    val calendar: Calendar = Calendar.getInstance()

    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)


    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.SET_ALARM
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Permission is not granted
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                context as Activity,
                Manifest.permission.SET_ALARM
            )
        ) {
            // Show an explanation to the user
            RequestPermissionsWithDetailToast(context = context, permissionName = "setting alarms")
            Log.e(TAG, "SetAlarm: Permission not granted for setting alarms")
            // After the user sees the explanation, try again to request the permission.

        } else {
            // No explanation needed; request the permission
            Log.e(TAG, "SetAlarm: Permission not granted for setting alarms sending to settings")
            RequestPermissionsRedirect(context = context)
            ActivityCompat.requestPermissions(
                context,
                arrayOf(Manifest.permission.SET_ALARM),
                MY_PERMISSIONS_REQUEST_SET_ALARM
            )
        }
    } else {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Notification policy access is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                )
            ) {
                // Show an explanation to the user
                // After the user sees the explanation, try again to request the permission.
                Log.e(TAG, "SetAlarm: user has not given notification permission")
                RequestPermissionsWithDetailToast(
                    context = context,
                    permissionName = "notifications"
                )
            } else {
                // No explanation needed; request the permission
                Log.e(
                    TAG,
                    "SetAlarm: user has not given notification permission sending to settings"
                )
                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                    MY_PERMISSIONS_REQUEST_ACCESS_NOTIFICATION_POLICY
                )
                RequestPermissionsRedirect(context = context)

            }
        } else {
            if (!alarmManager.canScheduleExactAlarms()) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        context as Activity,
                        Manifest.permission.SCHEDULE_EXACT_ALARM
                    )
                ) {
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                        MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM
                    )
                    RequestPermissionsWithDetailToast(
                        context = context,
                        permissionName = "exact alarms"
                    )
                } else {
                    RequestPermissionsRedirect(context = context)
                    ActivityCompat.requestPermissions(
                        context,
                        arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                        MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM
                    )
                }
            } else {
                // Notification and alarm permissions have already been granted
                // Set the alarm
                alarmManager.setAlarmClock(
//                setAndAllowWhileIdle(
                    AlarmManager.AlarmClockInfo(
                        calendar.timeInMillis,
                        alarmIntent
                    ),
                    alarmIntent
                )
                Log.i(TAG, "Alarm set for $hour:$minute")
            }
        }
    }
}


fun RequestPermissionsWithDetailToast(context: Context, permissionName: String) {
    val TAG = "RequestPermissions"
    Toast.makeText(
        context,
        "Please grant permissions for $permissionName in the app settings",
        Toast.LENGTH_LONG
    )
        .show()
    Log.i(TAG, "Alarm not set because $permissionName not granted")

}


fun RequestPermissionsRedirect(context: Context) {
    val intentChangeAndroidSetting =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    val uri: Uri = Uri.fromParts("package", context.packageName, null)
    intentChangeAndroidSetting.data = uri
    context.startActivity(intentChangeAndroidSetting)
    return

}