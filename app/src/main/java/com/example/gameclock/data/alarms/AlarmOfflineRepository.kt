package com.example.gameclock.data.alarms

import com.example.gameclock.model.Alarm
import kotlinx.coroutines.flow.first
import javax.inject.Inject


class AlarmOfflineRepository @Inject constructor(
    private val alarmDao: AlarmDao
): AlarmRepository {

    override suspend fun isDatabaseEmpty(): Boolean =
        alarmDao.getAlarmsList().first().isEmpty()

    override suspend fun insertAlarm(alarm: Alarm) =
        alarmDao.insert(alarm)

    override suspend fun deleteAlarm(alarm: Alarm) =
        alarmDao.delete(alarm)

    override suspend fun updateAlarm(alarm: Alarm) =
        alarmDao.update(alarm)

    override fun getAlarmsList() = alarmDao.getAlarmsList()

}