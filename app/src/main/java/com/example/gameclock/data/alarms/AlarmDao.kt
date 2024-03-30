package com.example.gameclock.data.alarms

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.gameclock.model.Alarm
import kotlinx.coroutines.flow.Flow


@Dao
interface AlarmDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Alarm::class)
    suspend fun insert(alarm: Alarm)

    @Delete(entity = Alarm::class)
    suspend fun delete(alarm: Alarm)

    @Update(entity = Alarm::class)
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarm_table ORDER BY id DESC")
    fun getAlarmsList(): Flow<List<Alarm>>

}