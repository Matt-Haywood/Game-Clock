package com.mhappening.gameclock.data.workManager.worker

import android.content.Context
import android.content.pm.ServiceInfo
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.mhappening.gameclock.data.alarms.AlarmRepository
import com.mhappening.gameclock.data.alarms.DATE
import com.mhappening.gameclock.data.alarms.TITLE
import com.mhappening.gameclock.data.workManager.WorkRequestManager
import com.mhappening.gameclock.ui.util.ALARM_WORKER_NOTIFICATION_ID
import com.mhappening.gameclock.ui.util.AlarmNotificationHelper
import com.mhappening.gameclock.ui.util.MediaPlayerHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext
import java.util.Date
import kotlin.coroutines.cancellation.CancellationException

/**
 * A worker class that checks for scheduled alarms and displays or removes notifications accordingly.
 *
 * @property alarmRepository The repository to fetch alarm data from.
 * @property alarmNotificationHelper The helper class to manage alarm notifications.
 * @property workRequestManager The manager to handle work requests.
 * @property ctx The context in which the worker is running.
 * @property params The parameters for the worker.
 */
@HiltWorker
class AlarmWorker @AssistedInject constructor(
    @Assisted private val alarmRepository: AlarmRepository,
    @Assisted private val alarmNotificationHelper: AlarmNotificationHelper,
    @Assisted private val mediaPlayerHelper: MediaPlayerHelper,
    @Assisted private val workRequestManager: WorkRequestManager,
    @Assisted ctx: Context,
    @Assisted params: WorkerParameters,
) : CoroutineWorker(ctx, params) {
    private val TAG = "Alarm Worker"

    /**
     * Returns the ForegroundInfo object for the worker.
     * The ForegroundInfo object contains the notification details for the worker.
     *
     * @return ForegroundInfo object for the worker.
     */
    override suspend fun getForegroundInfo(): ForegroundInfo {
        val title = "${inputData.getString(TITLE)}"
        Log.i(TAG, "getForegroundInfo: Alarm notification: $title")
        return ForegroundInfo(
            ALARM_WORKER_NOTIFICATION_ID,
            alarmNotificationHelper.getAlarmBaseNotification(title).build(),
            ServiceInfo.FOREGROUND_SERVICE_TYPE_MEDIA_PLAYBACK or ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
        )
    }

    /**
     * The main work method for the worker.
     * This method is responsible for executing the worker's task.
     *
     * @return Result object indicating the success or failure of the worker's task.
     */
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                val date = inputData.keyValueMap[DATE] as? Long
                if (date == null) {
                    Log.e(TAG, "doWork: Date is null")
                    return@withContext Result.failure()
                }
                val alarmId = inputData.getInt("ID", 0)
                Log.i(TAG, "doWork: Alarm called $alarmId, at ${Date(date)}")

                mediaPlayerHelper.prepare()

                setForeground(getForegroundInfo())

                mediaPlayerHelper.start()

                // Fetch the alarm by unique ID and disable it if it exists
                alarmRepository.getAlarmFlowByID(alarmId)
                    .collectLatest {
                        it?.let {
                            it.isEnabled = false
                            alarmRepository.updateAlarm(it)
                        }
                    }

                Result.success()
            } catch (e: CancellationException) {
                alarmNotificationHelper.removeAlarmWorkerNotification()
                mediaPlayerHelper.release()
                Log.i(TAG, "doWork: cancellation exception")
                Result.failure()
            } catch (e: NoSuchMethodException) {
                alarmNotificationHelper.removeAlarmWorkerNotification()
                mediaPlayerHelper.release()
                Log.e(TAG, "doWork: failed NoSuchMethodException")
                Result.failure()
            }
        }
    }
}

