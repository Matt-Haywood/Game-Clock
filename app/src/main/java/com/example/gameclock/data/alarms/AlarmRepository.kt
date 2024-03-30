package com.example.gameclock.data.alarms

import com.example.gameclock.model.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {

    suspend fun isDatabaseEmpty(): Boolean

    suspend fun insertAlarm(alarm: Alarm)

    suspend fun deleteAlarm(alarm: Alarm)

    suspend fun updateAlarm(alarm: Alarm)

    fun getAlarmsList(): Flow<List<Alarm>>

}