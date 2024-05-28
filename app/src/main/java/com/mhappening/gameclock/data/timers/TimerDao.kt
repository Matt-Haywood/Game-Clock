package com.mhappening.gameclock.data.timers

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mhappening.gameclock.model.Timer
import kotlinx.coroutines.flow.Flow

@Dao
interface TimerDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE, entity = Timer::class)
    suspend fun insert(timer: Timer)

    @Delete(entity = Timer::class)
    suspend fun delete(timer: Timer)

    @Update(entity = Timer::class)
    suspend fun update(timer: Timer)

    @Query("SELECT timerId FROM timer_table ORDER BY timerId DESC LIMIT 1")
    suspend fun getLastTimerId(): Int?

    @Query("SELECT * FROM timer_table WHERE timerId=:id")
    suspend fun getTimerById(id: Int): Timer?

    @Query("SELECT * FROM timer_table ORDER BY timerId DESC")
    fun getTimersList(): Flow<List<Timer>>

    @Query("SELECT * FROM timer_table WHERE timerId=:id")
    fun getTimerFlowByID(id: Int): Flow<Timer?>

    @Query("SELECT * FROM timer_table WHERE end_time=:endTime")
    fun getTimerByDate(endTime: Long): Flow<Timer?>
}