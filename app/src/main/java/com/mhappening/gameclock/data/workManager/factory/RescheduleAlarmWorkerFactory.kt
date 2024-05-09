package com.mhappening.gameclock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.mhappening.gameclock.data.alarms.AlarmRepository
import com.mhappening.gameclock.data.alarms.GameClockAlarmManager
import com.mhappening.gameclock.data.workManager.WorkRequestManager
import com.mhappening.gameclock.data.workManager.worker.RescheduleAlarmWorker
import javax.inject.Inject

class RescheduleAlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val scheduleAlarmManager: GameClockAlarmManager,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return RescheduleAlarmWorker(alarmRepository, scheduleAlarmManager, workRequestManager, appContext, params)
    }
}
