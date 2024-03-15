package com.example.gameclock.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.gameclock.model.ClockThemePreferences


/**
 * Database class with a singleton Instance object.
 */
@Database(entities = [ClockThemePreferences::class], version = 2, exportSchema = false)
//@TypeConverters(AppThemeConverter::class)
abstract class ClockPreferencesDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDao

    companion object {
        @Volatile
        private var Instance: ClockPreferencesDatabase? = null

        fun getDatabase(context: Context): ClockPreferencesDatabase {
            // if the Instance is not null, return it, otherwise create a new database instance.
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    ClockPreferencesDatabase::class.java,
                    "clock_preferences_database"
                )
                    /**
                     * Setting this option in your app's database builder means that Room
                     * permanently deletes all data from the tables in your database when it
                     * attempts to perform a migration with no defined migration path.
                     */

                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

class AppThemeConverter {
//    @TypeConverter
//    fun fromAppTheme(theme: AppTheme): String {
//        return theme.themeName
//    }
//
//    @TypeConverter
//    fun toAppTheme(themeName: String): AppTheme {
//        return when (themeName) {
//            "Default" -> AppTheme.Default
//            "Light" -> AppTheme.Light
//            "Dark" -> AppTheme.Dark
//            "Red" -> AppTheme.Red
//            else -> throw IllegalArgumentException("Unknown theme name: $themeName")
//        }
//    }
}