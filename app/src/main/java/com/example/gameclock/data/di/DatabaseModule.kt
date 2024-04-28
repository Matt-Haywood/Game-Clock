package com.example.gameclock.data.di

import android.content.Context
import androidx.room.Room
import com.example.gameclock.data.ClockDatabase
import com.example.gameclock.data.alarms.AlarmDao
import com.example.gameclock.data.alarms.AlarmRepository
import com.example.gameclock.data.clockthemes.ClockDao
import com.example.gameclock.data.clockthemes.ClockThemePreferencesRepository
import com.example.gameclock.data.clockthemes.OfflineClockThemePreferencesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideClockDatabase(
        @ApplicationContext context: Context,
    ) = Room.databaseBuilder(
        context,
        ClockDatabase::class.java,
        "clock_database"
    )
        //TODO: Change fallback and migration method for room database
        .fallbackToDestructiveMigration()
        .build(
    )

    @Singleton
    @Provides
    fun provideClockThemePreferencesRepository(clockDao: ClockDao): ClockThemePreferencesRepository {
        return OfflineClockThemePreferencesRepository(clockDao)
    }

    @Singleton
    @Provides
    fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository {
        return AlarmRepository(alarmDao)
    }

    @Singleton
    @Provides
    fun provideClockDao(clockDatabase: ClockDatabase): ClockDao {
        return clockDatabase.clockDao()
    }

    @Singleton
    @Provides
    fun provideAlarmDao(clockDatabase: ClockDatabase): AlarmDao {
        return clockDatabase.alarmDao()
    }
}