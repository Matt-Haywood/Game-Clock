package com.mhappening.gameclock.data.roomDatabase

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mhappening.gameclock.data.alarms.AlarmDao
import com.mhappening.gameclock.data.clockthemes.ClockDao
import com.mhappening.gameclock.data.timers.TimerDao
import com.mhappening.gameclock.model.Alarm
import com.mhappening.gameclock.model.ClockThemePreferences
import com.mhappening.gameclock.model.Converters
import com.mhappening.gameclock.model.Timer


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [ClockThemePreferences::class, Alarm::class, Timer::class], version = 16,
    autoMigrations = [
        AutoMigration(from = 14, to = 15, spec = ClockAutoMigrationSpec::class),
        AutoMigration(from = 15, to = 16)
    ]

)
@TypeConverters(Converters::class)
abstract class ClockDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDao

    abstract fun alarmDao(): AlarmDao

    abstract fun timerDao(): TimerDao

}

