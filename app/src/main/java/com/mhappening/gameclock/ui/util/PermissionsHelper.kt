package com.mhappening.gameclock.ui.util

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState


class PermissionsHelper {
    val TAG = "PermissionsHelper"

    @Composable
    @OptIn(ExperimentalPermissionsApi::class)
    fun checkPermissions(): Boolean {
        val alarmPermissionState = rememberPermissionState(Manifest.permission.SET_ALARM)
        val notificationPolicyPermissionState =
            rememberPermissionState(Manifest.permission.ACCESS_NOTIFICATION_POLICY)
        val notificationPermissionState =
            rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        val useExactAlarmPermissionState =
            rememberPermissionState(Manifest.permission.USE_EXACT_ALARM)

        val hasAlarmPermission = alarmPermissionState.status.isGranted

        val hasNotificationPolicyPermission = notificationPolicyPermissionState.status.isGranted

        val hasNotificationPermission = notificationPermissionState.status.isGranted

        val hasScheduleExactAlarmPermission = useExactAlarmPermissionState.status.isGranted

        Log.i(
            TAG,
            "checkPermissions: hasAlarmPermission: $hasAlarmPermission, hasNotificationPolicyPermission: $hasNotificationPolicyPermission, hasScheduleExactAlarmPermission: $hasScheduleExactAlarmPermission, hasNotificationPermission: $hasNotificationPermission"
        )

        return hasAlarmPermission && hasNotificationPolicyPermission && hasScheduleExactAlarmPermission && hasNotificationPermission
    }

    fun checkNotificationPermissions(context: Context): Boolean {
        val hasNotificationPolicyPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_NOTIFICATION_POLICY
        ) == PackageManager.PERMISSION_GRANTED

        val hasPostNotificationPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        return hasPostNotificationPermission && hasNotificationPolicyPermission
    }


    /**
     * Redirects the user to the application settings to grant permissions.
     *
     * @param context The context in which the redirection is to be performed.
     */
    fun requestPermissionsRedirect(context: Context) {
        val intentChangeAndroidSetting =
            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", context.packageName, null)
        intentChangeAndroidSetting.data = uri
        context.startActivity(intentChangeAndroidSetting)
        return

    }
}