package com.andreasgift.myclock.clockdata

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andreasgift.myclock.clock.Clock

@Database(entities = arrayOf(Clock::class), version = 1)
abstract class ClockROOMDatabase : RoomDatabase() {

    abstract fun clockDao(): ClockDAO

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: ClockROOMDatabase? = null

        fun getDatabase(context: Context): ClockROOMDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ClockROOMDatabase::class.java,
                    "clock_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}