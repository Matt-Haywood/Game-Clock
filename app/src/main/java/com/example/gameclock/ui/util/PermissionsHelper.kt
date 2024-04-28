package com.example.gameclock.ui.util

import android.Manifest
import android.app.Activity
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

private const val MY_PERMISSIONS_REQUEST_SET_ALARM = 1
private const val MY_PERMISSIONS_REQUEST_ACCESS_NOTIFICATION_POLICY = 1
private const val MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM = 1

class PermissionsHelper(
    private val activityContext: Context,
    private val alarmManager: AlarmManager
) {
    val TAG = "PermissionsHelper"

    fun checkPermissions(): Boolean {
        val hasAlarmPermission = ContextCompat.checkSelfPermission(
            activityContext,
            Manifest.permission.SET_ALARM
        ) == PackageManager.PERMISSION_GRANTED

        val hasNotificationPolicyPermission = ContextCompat.checkSelfPermission(
            activityContext,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED

        val hasScheduleExactAlarmPermission = alarmManager.canScheduleExactAlarms()

        return hasAlarmPermission && hasNotificationPolicyPermission && hasScheduleExactAlarmPermission
    }

    fun requestPermissions() {
        val hasAlarmPermission = ContextCompat.checkSelfPermission(
            activityContext,
            Manifest.permission.SET_ALARM
        ) == PackageManager.PERMISSION_GRANTED

        val hasNotificationPolicyPermission = ContextCompat.checkSelfPermission(
            activityContext,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED

        val hasScheduleExactAlarmPermission = ContextCompat.checkSelfPermission(
            activityContext,
            Manifest.permission.SCHEDULE_EXACT_ALARM
        ) == PackageManager.PERMISSION_GRANTED
        if (!hasAlarmPermission) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activityContext as Activity,
                    Manifest.permission.SET_ALARM
                )
            ) {
                // Show an explanation to the user
                requestPermissionsWithDetailToast(
                    context = activityContext,
                    permissionName = "setting alarms"
                )
                Log.e(TAG, "SetAlarm: Permission not granted for setting alarms")
                // After the user sees the explanation, try again to request the permission.

            } else {
                // No explanation needed; request the permission
                Log.e(
                    TAG,
                    "SetAlarm: Permission not granted for setting alarms sending to settings"
                )
                requestPermissionsRedirect(context = activityContext)
                ActivityCompat.requestPermissions(
                    activityContext,
                    arrayOf(Manifest.permission.SET_ALARM),
                    MY_PERMISSIONS_REQUEST_SET_ALARM
                )
            }
        } else if (!hasNotificationPolicyPermission) {
            // Notification policy access is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activityContext as Activity,
                    Manifest.permission.ACCESS_NOTIFICATION_POLICY
                )
            ) {
                // Show an explanation to the user
                // After the user sees the explanation, try again to request the permission.
                Log.e(TAG, "SetAlarm: user has not given notification permission")
                requestPermissionsWithDetailToast(
                    context = activityContext,
                    permissionName = "notifications"
                )
            } else {
                // No explanation needed; request the permission
                Log.e(
                    TAG,
                    "SetAlarm: user has not given notification permission sending to settings"
                )
                ActivityCompat.requestPermissions(
                    activityContext,
                    arrayOf(Manifest.permission.ACCESS_NOTIFICATION_POLICY),
                    MY_PERMISSIONS_REQUEST_ACCESS_NOTIFICATION_POLICY
                )
                requestPermissionsRedirect(context = activityContext)

            }
        } else if (!hasScheduleExactAlarmPermission) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(
//
//                    Manifest.permission.SCHEDULE_EXACT_ALARM
//                )
//            ) {
                ActivityCompat.requestPermissions(
                    activityContext as Activity,
                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                    MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM
                )
                requestPermissionsWithDetailToast(
                    context = activityContext,
                    permissionName = "exact alarms"
                )
            } else {
                requestPermissionsRedirect(context = activityContext)
                ActivityCompat.requestPermissions(
                    activityContext as Activity,
                    arrayOf(Manifest.permission.SCHEDULE_EXACT_ALARM),
                    MY_PERMISSIONS_REQUEST_SCHEDULE_EXACT_ALARM
                )
            }
        }


    /**
     * Requests permissions with a detailed toast message.
     *
     * @param context The context in which the toast is to be shown.
     * @param permissionName The name of the permission for which the toast is to be shown.
     */
    private fun requestPermissionsWithDetailToast(context: Context, permissionName: String) {
        val TAG = "RequestPermissions"
        Toast.makeText(
            context,
            "Please grant permissions for $permissionName in the app settings",
            Toast.LENGTH_LONG
        )
            .show()
        Log.i(TAG, "Alarm not set because $permissionName not granted")

    }

    /**
     * Redirects the user to the application settings to grant permissions.
     *
     * @param context The context in which the redirection is to be performed.
     */
    private fun requestPermissionsRedirect(context: Context) {
        val intentChangeAndroidSetting =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intentChangeAndroidSetting.data = uri
        context.startActivity(intentChangeAndroidSetting)
        return

    }
}