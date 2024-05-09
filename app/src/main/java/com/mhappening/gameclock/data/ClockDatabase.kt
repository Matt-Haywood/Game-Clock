package com.mhappening.gameclock.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mhappening.gameclock.data.alarms.AlarmDao
import com.mhappening.gameclock.data.clockthemes.ClockDao
import com.mhappening.gameclock.model.Alarm
import com.mhappening.gameclock.model.ClockThemePreferences
import com.mhappening.gameclock.model.Converters


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [ClockThemePreferences::class, Alarm::class], version = 14,
//    autoMigrations = [AutoMigration(from = 13, to = 14)]
)
@TypeConverters(Converters::class)
abstract class ClockDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDao

    abstract fun alarmDao(): AlarmDao

}

