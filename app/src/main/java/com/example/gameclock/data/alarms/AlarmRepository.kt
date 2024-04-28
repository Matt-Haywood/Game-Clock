package com.example.gameclock.data.alarms

import com.example.gameclock.model.Alarm
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

/**
 * Repository for the Alarm entity.
 * This class abstracts the data source and provides methods to perform database operations related to the Alarm entity.
 *
 * @property alarmDao The Data Access Object (DAO) for the Alarm entity.
 */
class AlarmRepository @Inject constructor(
    private val alarmDao: AlarmDao
) {
    /**
     * A Flow of List of alarms from the database.
     * The Flow emits a new list only when the list of alarms in the database changes.
     */
    val alarmsList = alarmDao.getAlarmsList().distinctUntilChanged()

    /**
     * Inserts a new alarm into the database.
     *
     * @param alarm The alarm to be inserted.
     */
    suspend fun insertAlarm(alarm: Alarm) =
        alarmDao.insert(alarm)

    /**
     * Retrieves the last inserted alarm's ID from the database.
     *
     * @return The ID of the last inserted alarm.
     */
    suspend fun getLastId() = alarmDao.getLastId()

    /**
     * Deletes an existing alarm from the database.
     *
     * @param alarm The alarm to be deleted.
     */
    suspend fun deleteAlarm(alarm: Alarm) =
        alarmDao.delete(alarm)

    /**
     * Updates an existing alarm in the database.
     *
     * @param alarm The alarm with updated fields.
     */
    suspend fun updateAlarm(alarm: Alarm) =
        alarmDao.update(alarm)


    /**
     * Retrieves all alarms from the database as a Flow.
     *
     * @return A Flow of List of alarms.
     */
    fun getAlarmsListFlow() = alarmDao.getAlarmsList()


    /**
     * Retrieves an alarm by its ID from the database as a Flow.
     *
     * @param id The ID of the alarm to be retrieved.
     * @return A Flow of the alarm with the given ID.
     */

     fun getAlarmFlowByID(id: Int) = alarmDao.getAlarmFlowByID(id)




}