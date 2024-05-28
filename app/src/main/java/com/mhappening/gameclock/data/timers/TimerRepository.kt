package com.mhappening.gameclock.data.timers

import com.mhappening.gameclock.model.Timer
import java.util.Date
import javax.inject.Inject

class TimerRepository @Inject constructor(
    private val timerDao: TimerDao
) {
    val timersList = timerDao.getTimersList()

    suspend fun insertTimer(timer: Timer) =
        timerDao.insert(timer)

    suspend fun deleteTimer(timer: Timer) =
        timerDao.delete(timer)

    suspend fun updateTimer(timer: Timer) =
        timerDao.update(timer)

    suspend fun getLastId() = timerDao.getLastTimerId()

    suspend fun updateTimerEndTimeAndStatus(timerId: Int, endTime: Date, isEnabled: Boolean) {
        val timer: Timer? = getTimerById(timerId)
        if (timer != null) {
            val updatedTimer = timer.copy(endTime = endTime, isEnabled = isEnabled)
            updateTimer(updatedTimer)
        }
    }

    suspend fun getTimerById(id: Int): Timer? = timerDao.getTimerById(id)

    fun getTimerFlowByID(id: Int) = timerDao.getTimerFlowByID(id)

    fun getTimerByDate(endTime: Long) = timerDao.getTimerByDate(endTime)
}