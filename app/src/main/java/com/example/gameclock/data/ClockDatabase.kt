package com.example.gameclock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gameclock.data.alarms.AlarmDao
import com.example.gameclock.data.clockthemes.ClockDao
import com.example.gameclock.model.Alarm
import com.example.gameclock.model.ClockThemePreferences


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [ClockThemePreferences::class, Alarm::class], version = 4, exportSchema = false)
abstract class ClockDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDao

    abstract fun alarmDao(): AlarmDao

}

