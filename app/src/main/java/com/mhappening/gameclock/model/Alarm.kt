package com.mhappening.gameclock.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date


@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "alarm_id")
    var alarmId: Int = 0,
    @ColumnInfo(name = "alarm_title")
    var title: String = "",
    @ColumnInfo(name = "is_alarm_enabled")
    var isEnabled: Boolean = false,
    @ColumnInfo(name = "alarm_date")
    var date: Date = Date()
)

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
