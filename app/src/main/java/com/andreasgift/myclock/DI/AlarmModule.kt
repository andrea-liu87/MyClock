package com.andreasgift.myclock.DI

import android.content.Context
import androidx.room.Room
import com.andreasgift.myclock.alarmdata.AlarmDao
import com.andreasgift.myclock.alarmdata.AlarmROOMDatabase
import com.andreasgift.myclock.alarmdata.AlarmRepository
import com.andreasgift.myclock.alarmdata.AlarmRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AlarmModule {

    @Provides
    @Singleton
    fun provideAlarmROOMDatabase(@ApplicationContext appContext: Context): AlarmROOMDatabase =
        Room.databaseBuilder(
            appContext,
            AlarmROOMDatabase::class.java,
            "alarm_database"
        ).build()


    @Provides
    @Singleton
    fun provideAlarmDao(database: AlarmROOMDatabase): AlarmDao = database.alarmDao()


    @Provides
    @Singleton
    fun provideAlarmRepository(alarmDao: AlarmDao): AlarmRepository = AlarmRepositoryImpl(alarmDao)
}
