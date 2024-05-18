package com.mhappening.gameclock.data.di

import android.content.Context
import androidx.room.Room
import com.mhappening.gameclock.data.roomDatabase.ClockDatabase
import com.mhappening.gameclock.data.alarms.AlarmDao
import com.mhappening.gameclock.data.alarms.AlarmRepository
import com.mhappening.gameclock.data.clockthemes.ClockDao
import com.mhappening.gameclock.data.clockthemes.ClockThemePreferencesRepository
import com.mhappening.gameclock.data.clockthemes.OfflineClockThemePreferencesRepository
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