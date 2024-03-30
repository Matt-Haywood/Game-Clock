package com.example.gameclock.data.alarms

import android.app.Activity
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getActivity
import android.app.TaskInfo
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.gameclock.MainActivity
import com.example.gameclock.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {


    val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

    // If there is no alarm set, set the notification sound as backup
    val ringtoneUri = alarmUri ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)


    private val broadcastReceiverScope = CoroutineScope(SupervisorJob())

    private var notificationManager: NotificationManagerCompat? = null

/*    override fun onReceive(context: Context?, p1: Intent?) {
        val taskInfo = p1?.getSerializableExtra("task_info") as TaskInfo?
        // tapResultIntent gets executed when user taps the notification
        val tapResultIntent = Intent(context, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            getActivity(context, 0, tapResultIntent, FLAG_UPDATE_CURRENT or FLAG_IMMUTABLE)

        val notification = context?.let {
            NotificationCompat.Builder(it, "AlarmChannel")
                .setContentTitle("Alarm")
                .setContentText("Tap to dismiss the alarm")
                .setSmallIcon(R.drawable.baseline_alarm_24)
                .setContentIntent(pendingIntent)
                .addAction(R.drawable.baseline_alarm_24, "Dismiss", pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .build()
        }
        notificationManager = context?.let { NotificationManagerCompat.from(it) }
        notification?.let { taskInfo?.let { it1 -> notificationManager?.notify(it1.id, it) } }
    }*/


    // Create a new Ringtone

        override fun onReceive(context: Context, intent: Intent) {
            val pendingResult: PendingResult = goAsync()
            broadcastReceiverScope.launch(Dispatchers.Default) {
                try {
                    if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

                        /**
                         * This is where I would reset all alarms on reboot, however this is a considerable amount of work
                         * and there are other more important features to implement first.
                         * TODO Implement resetting all alarms on reboot
                         */
                        /*// reset all alarms
                        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                        val alarms =  // replace this with your method to get all alarms

                        for (alarm in alarms) {
                            val alarmIntent = Intent(context, AlarmReceiver::class.java)
                            val pendingIntent = PendingIntent.getBroadcast(context, alarm.id, alarmIntent,
                                PendingIntent.FLAG_MUTABLE)
                            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarm.time, pendingIntent)
                        }
*/
                    } else {
                        // Get the default alarm ringtone
                        val ringtone = RingtoneManager.getRingtone(context, ringtoneUri)

                        // If the Ringtone is not null, then play the Ringtone
                        ringtone?.play()

                        // Create a notification
                        createNotification(context)
                    }
                } finally {
                    pendingResult.finish()
                }
            }

        }
    // This method is called when the alarm goes off
//        if ((Intent.ACTION_BOOT_COMPLETED) == intent.getAction()) {
//            // reset all alarms
//        } else {
//
//            // Get the default alarm ringtone
//            ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
//
//            // If the Ringtone is not null, then play the Ringtone
//            ringtone?.play()
//
//            // Create a notification
//            createNotification(context)
//        }
//    }

    private fun createNotification(context: Context) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create a notification channel
        val channel = NotificationChannel(
            "AlarmChannel",
            "Alarm Channel",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)

        // Create an intent to start the DismissAlarmActivity when the notification is tapped
        val intent = Intent(context, DismissAlarmActivity::class.java)
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Create a separate PendingIntent for the dismiss action
        val dismissIntent = Intent(context, DismissAlarmActivity::class.java)
        val pendingDismissIntent =
            PendingIntent.getActivity(context, 1, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT)

//TODO: Fix this

        // Create a notification
        val notification = NotificationCompat
            .Builder(context, "AlarmChannel")
            .setContentTitle("Alarm")
            .setContentText("Tap to dismiss the alarm")
            .setSmallIcon(R.drawable.baseline_alarm_24)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.baseline_alarm_24, "Dismiss", pendingDismissIntent)
            .setAutoCancel(true)
            .build()

        // Show the notification
        notificationManager.notify(1, notification)
    }
}

class DismissAlarmActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        AlarmReceiver.ringtone?.stop()

    }
}
