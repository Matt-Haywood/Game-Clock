package com.mhappening.gameclock.data.alarms

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import com.mhappening.gameclock.data.workManager.WorkRequestManager
import com.mhappening.gameclock.model.Alarm
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Random
import javax.inject.Inject
import javax.inject.Singleton


/**
 * A manager for game clock alarms.
 * This class provides methods to set, snooze, and cancel alarms.
 *
 * @property applicationContext The application context.
 * @property workRequestManager The manager for work requests.
 */
@Singleton
class GameClockAlarmManager @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val workRequestManager: WorkRequestManager,
) {
    /**
     * Sets an alarm.
     *
     * @param alarm The alarm to be set.
     */
    @SuppressLint("MissingPermission")
    fun setAlarm(alarm: Alarm) {

        val TAG = "SetAlarm"
        Log.i(TAG, "setAlarm: setting alarm  ${alarm}.")


        val alarmManager = applicationContext.getSystemService(AlarmManager::class.java)

        val alarmIntent = Intent(applicationContext, AlarmReceiver::class.java).apply {
            putExtra(ID, alarm.id)
            putExtra(TITLE, alarm.title)
            putExtra(DATE, alarm.date.time)
        }
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alarm.id,
            alarmIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
        )

        val calendar = Calendar.getInstance()
        calendar.time = alarm.date

        // Notification and alarm permissions have already been granted
        // Set the alarm
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmPendingIntent,
        )
        Log.i(TAG, "Alarm set for $alarm")

    }


    /**
     * Snoozes the current alarm for 10 minutes.
     */
    fun snooze() {
        val calendar = java.util.Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.add(java.util.Calendar.MINUTE, 10)

        val alarm = Alarm(
            id = Random().nextInt(Integer.MAX_VALUE),
            title = "Snooze",
            isEnabled = true,
            date = calendar.time,
        )

        setAlarm(alarm)
    }

    /**
     * Cancels a given alarm.
     *
     * @param alarm The alarm to be cancelled.
     */
    fun cancel(alarm: Alarm) {
        val alarmManager =
            applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alamIntent = Intent(applicationContext, AlarmReceiver::class.java)
        val alarmPendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            alarm.id,
            alamIntent,
            PendingIntent.FLAG_IMMUTABLE,
        )
        alarmManager.cancel(alarmPendingIntent)
    }
}