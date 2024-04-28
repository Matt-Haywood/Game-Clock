package com.example.gameclock.data.workManager.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.data.alarms.GameClockAlarmManager
import com.example.gameclock.data.workManager.WorkRequestManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.firstOrNull

@HiltWorker
class RescheduleAlarmWorker @AssistedInject constructor(
    @Assisted private val alarmRepository: AlarmRepository,
    @Assisted private val gameClockAlarmManager: GameClockAlarmManager,
    @Assisted private val workRequestManager: WorkRequestManager,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {

    override suspend fun doWork(): Result {
        return try {
            val scheduledAlarms = alarmRepository.alarmsList
                .firstOrNull { it.isNotEmpty() }
            scheduledAlarms?.forEach {
                gameClockAlarmManager.setAlarm(it)
                Log.i(RESCHEDULE_ALARM_TAG, "alarm rescheduled: $it")
            }
            workRequestManager.cancelWorker(RESCHEDULE_ALARM_TAG)

            Result.success()
        } catch (throwable: Throwable) {
            Log.e(RESCHEDULE_ALARM_TAG, "doWork: error rescheduling alarm ", )
            Result.failure()
        }
    }
}

const val RESCHEDULE_ALARM_TAG = "rescheduleAlarmTag"
