package com.mhappening.gameclock.data.alarms

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mhappening.gameclock.model.Alarm
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) for the Alarm entity.
 * This interface defines all the database operations related to the Alarm entity.
 */
@Dao
interface AlarmDao {

    /**
     * Inserts a new alarm into the database.
     * If the alarm already exists, the insert operation is ignored.
     *
     * @param alarm The alarm to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Alarm::class)
    suspend fun insert(alarm: Alarm)

    /**
     * Deletes an existing alarm from the database.
     *
     * @param alarm The alarm to be deleted.
     */
    @Delete(entity = Alarm::class)
    suspend fun delete(alarm: Alarm)

    /**
     * Updates an existing alarm in the database.
     *
     * @param alarm The alarm with updated fields.
     */
    @Update(entity = Alarm::class)
    suspend fun update(alarm: Alarm)

    /**
     * Retrieves the last inserted alarm's ID from the database.
     *
     * @return The ID of the last inserted alarm.
     */
    @Query("SELECT alarm_id FROM alarm_table ORDER BY alarm_id DESC LIMIT 1")
    suspend fun getLastId(): Int?

    /**
     * Retrieves an alarm by its ID from the database.
     *
     * @param id The ID of the alarm to be retrieved.
     * @return The alarm with the given ID.
     */
    @Query("SELECT * FROM alarm_table WHERE alarm_id=:id")
    suspend fun getAlarmById(id: Int): Alarm?

    /**
     * Retrieves all alarms from the database in descending order of their IDs.
     *
     * @return A Flow of List of alarms.
     */
    @Query("SELECT * FROM alarm_table ORDER BY alarm_id DESC")
    fun getAlarmsList(): Flow<List<Alarm>>

    @Query("SELECT * FROM alarm_table WHERE alarm_id=:id")
    fun getAlarmFlowByID(id: Int): Flow<Alarm?>

    @Query("SELECT * FROM alarm_table WHERE alarm_date=:date")
    fun getAlarmByDate(date: Long): Flow<Alarm?>

}