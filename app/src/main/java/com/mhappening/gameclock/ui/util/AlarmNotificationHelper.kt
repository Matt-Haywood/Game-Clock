package com.mhappening.gameclock.ui.util

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Icon
import com.mhappening.gameclock.MainActivity
import com.mhappening.gameclock.R
import com.mhappening.gameclock.data.alarms.ACTION_DISMISS
import com.mhappening.gameclock.data.alarms.ACTION_SNOOZE
import com.mhappening.gameclock.data.alarms.AlarmReceiver
import com.mhappening.gameclock.data.util.setIntentAction
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AlarmNotificationHelper @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
    private val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    private val alarmBroadcastReceiver = AlarmReceiver::class.java

    private val openAppIntent = Intent(
        applicationContext,
        MainActivity::class.java,
    ).apply {
        flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
    }

    private val openAlarmPendingIntent = PendingIntent.getActivity(
        applicationContext,
        13,
        openAppIntent,
        PendingIntent.FLAG_IMMUTABLE,
    )

    private val dismissIntentAction = alarmBroadcastReceiver.setIntentAction(
        actionName = ACTION_DISMISS,
        requestCode = 14,
        context = applicationContext,
    )

    private val snoozeIntentAction = alarmBroadcastReceiver.setIntentAction(
        actionName = ACTION_SNOOZE,
        requestCode = 15,
        context = applicationContext,
    )

    init {
        createAlarmNotificationChannels()
    }

    fun getAlarmBaseNotification(title: String) =
        Notification.Builder(applicationContext, ALARM_WORKER_CHANNEL_ID)
            .setContentTitle("Game Clock Alarm")
            .setContentText(title)
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setShowWhen(false)
            .setFullScreenIntent(openAlarmPendingIntent, true)
            .addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(applicationContext, R.drawable.baseline_close_24),
                    "Dismiss",
                    dismissIntentAction
                ).build()
            )
            .addAction(
                Notification.Action.Builder(
                    Icon.createWithResource(applicationContext, R.drawable.baseline_snooze_24),
                    "Snooze 10 mins",
                    snoozeIntentAction
                ).build()
            )
            .setDeleteIntent(dismissIntentAction)
            .setOngoing(true)


    private fun createAlarmNotificationChannels() {
        val alarmWorkerChannel = NotificationChannel(
            ALARM_WORKER_CHANNEL_ID,
            applicationContext.getString(R.string.alarm_worker_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = applicationContext.getString(R.string.alarm_worker_channel_description)
            setSound(null, null)
        }

        notificationManager.createNotificationChannel(alarmWorkerChannel)
    }

    fun removeAlarmWorkerNotification() {
        notificationManager.cancel(ALARM_WORKER_NOTIFICATION_ID)
    }


}

private const val ALARM_WORKER_CHANNEL_ID = "alarm_worker_channel"
const val ALARM_WORKER_NOTIFICATION_ID = 12
