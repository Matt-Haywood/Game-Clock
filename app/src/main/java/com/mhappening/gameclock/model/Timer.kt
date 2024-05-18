package com.mhappening.gameclock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import kotlin.time.Duration

@Entity(tableName = "timer_table")
data class Timer(
    @PrimaryKey(autoGenerate = true)
    var timerId: Int = 0,
    @ColumnInfo(name = "timer_title")
    var timerTitle: String = "GC Timer",
    @ColumnInfo(name = "duration")
    var durationSeconds: Long = 0L,
    @ColumnInfo(name = "end_time")
    var endTime: Date = Date(),
    @ColumnInfo(name = "is_timer_enabled")
    var isEnabled: Boolean = false
)