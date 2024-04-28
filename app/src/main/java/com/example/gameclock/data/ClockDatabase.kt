package com.example.gameclock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.gameclock.data.alarms.AlarmDao
import com.example.gameclock.data.clockthemes.ClockDao
import com.example.gameclock.model.Alarm
import com.example.gameclock.model.ClockThemePreferences
import com.example.gameclock.model.Converters


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [ClockThemePreferences::class, Alarm::class], version = 12, exportSchema = false)
@TypeConverters(Converters::class)
abstract class ClockDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDao

    abstract fun alarmDao(): AlarmDao

}

