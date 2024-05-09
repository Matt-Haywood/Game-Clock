package com.mhappening.gameclock.data.workManager.factory

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.mhappening.gameclock.data.alarms.AlarmRepository
import com.mhappening.gameclock.data.workManager.WorkRequestManager
import com.mhappening.gameclock.data.workManager.worker.AlarmWorker
import com.mhappening.gameclock.ui.util.AlarmNotificationHelper
import com.mhappening.gameclock.ui.util.MediaPlayerHelper
import javax.inject.Inject

class AlarmWorkerFactory @Inject constructor(
    private val alarmRepository: AlarmRepository,
    private val alarmNotificationHelper: AlarmNotificationHelper,
    private val mediaPlayerHelper: MediaPlayerHelper,
    private val workRequestManager: WorkRequestManager,
) : ChildWorkerFactory {

    override fun create(appContext: Context, params: WorkerParameters): ListenableWorker {
        return AlarmWorker(alarmRepository, alarmNotificationHelper, mediaPlayerHelper, workRequestManager, appContext, params)
    }
}
