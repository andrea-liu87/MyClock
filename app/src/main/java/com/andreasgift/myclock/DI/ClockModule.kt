package com.andreasgift.myclock.DI

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.clockdata.ClockDAO
import com.andreasgift.myclock.clockdata.ClockROOMDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CLockModule {

    @Provides
    @Singleton
    fun provideClockROOMDatabase(@ApplicationContext appContext: Context): ClockROOMDatabase =
        Room.databaseBuilder(
            appContext,
            ClockROOMDatabase::class.java,
            "clock_database"
        ).build()


    @Provides
    @Singleton
    fun provideClockDao(database: ClockROOMDatabase): ClockDAO = database.clockDao()

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences =
        context.getSharedPreferences(Constants.PREF_KEY_MANUAL_CLOCK, Context.MODE_PRIVATE)
}