package com.mhappening.gameclock.data.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import com.mhappening.gameclock.data.workManager.WorkRequestManager
import com.mhappening.gameclock.data.workManager.worker.AlarmWorker
import com.mhappening.gameclock.data.workManager.worker.RESCHEDULE_ALARM_TAG
import com.mhappening.gameclock.data.workManager.worker.RescheduleAlarmWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * A BroadcastReceiver that receives alarm events and performs appropriate actions.
 *
 * @property gameClockAlarmManager The manager for game clock alarms.
 * @property workRequestManager The manager for work requests.
 */
@AndroidEntryPoint
class AlarmReceiver : BroadcastReceiver() {

    @Inject
    lateinit var gameClockAlarmManager: GameClockAlarmManager

    @Inject
    lateinit var workRequestManager: WorkRequestManager

    private val alarmBroadcastReceiverScope = CoroutineScope(SupervisorJob())

    private val TAG = "AlarmBroadcastReceiver"


    override fun onReceive(context: Context, intent: Intent) {
        val pendingResult: PendingResult = goAsync()
        alarmBroadcastReceiverScope.launch(Dispatchers.Default) {
            try {
                intent.let { intent ->
                    when (intent.action) {
                        "android.intent.action.BOOT_COMPLETED" -> {
                            // Enqueue a worker to reschedule alarms after boot completed
                            workRequestManager.enqueueWorker<RescheduleAlarmWorker>(
                                RESCHEDULE_ALARM_TAG,
                            )
                        }

                        ACTION_DISMISS -> {
                            // Cancel the alarm worker and remove the notification when the alarm is dismissed
                            workRequestManager.cancelWorker(ALARM_TAG)
//                            alarmNotificationHelper.removeScheduledAlarmNotification()  // Remove the AlarmCheckerWorker notification
                        }

                        ACTION_SNOOZE -> {
                            // Snooze the alarm and cancel the alarm worker when the snooze action is received
                            gameClockAlarmManager.snooze()
                            workRequestManager.cancelWorker(ALARM_TAG)
                        }

                        else -> {
//                            val shouldStartWorker = alarmIsToday(intent)
                            val inputData = Data.Builder()
                                .putInt(ID, intent.getIntExtra(ID, 0))
                                .putString(TITLE, intent.getStringExtra(TITLE))
                                .putLong(DATE, intent.getLongExtra(DATE, 1L))
                                .build()
                            Log.i(
                                TAG,
                                "onReceive: ${intent.getIntExtra(ID, 0)} ${
                                    intent.getStringExtra(TITLE)
                                } date: ${intent.getLongExtra(DATE, 1L)}"
                            )
//                            if (shouldStartWorker) {
                            workRequestManager.enqueueWorker<AlarmWorker>(
                                ALARM_TAG,
                                inputData,
                            )
//                            }
                        }
                    }
                }
            } finally {
                pendingResult.finish()
                alarmBroadcastReceiverScope.cancel()
            }
        }
    }
}

const val ID = "ID"
const val TITLE = "TITLE"
const val DATE = "DATE"
const val ACTION_DISMISS = "ACTION_DISMISS"
const val ACTION_SNOOZE = "ACTION_SNOOZE"
const val ALARM_TAG = "alarmTag"

