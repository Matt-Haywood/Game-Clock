package com.mhappening.gameclock.data.timers

import com.mhappening.gameclock.model.Timer
import javax.inject.Inject

class TimerRepository @Inject constructor(
    private val timerDao: TimerDao
) {
    val timersList = timerDao.getTimersList()

    suspend fun insertTimer(timer: Timer) =
        timerDao.insert(timer)

    suspend fun deleteTimer(timer: Timer) =
        timerDao.delete(timer)

    suspend fun getLastId() = timerDao.getLastTimerId()

    suspend fun getTimerById(id: Int) = timerDao.getTimerById(id)

    fun getTimerFlowByID(id: Int) = timerDao.getTimerFlowByID(id)

    fun getTimerByDate(endTime: Long) = timerDao.getTimerByDate(endTime)
}