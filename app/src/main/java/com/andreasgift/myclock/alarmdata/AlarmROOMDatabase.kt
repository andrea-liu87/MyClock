package com.andreasgift.myclock.alarmdata

import android.content.Context
import androidx.room.*
import com.andreasgift.myclock.alarm.Alarm
import com.google.gson.Gson
import java.util.*


@Database(entities = arrayOf(Alarm::class), version = 1)
@TypeConverters(AlarmROOMDatabase.Converters::class)
abstract class AlarmROOMDatabase : RoomDatabase() {

    abstract fun alarmDao(): AlarmDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AlarmROOMDatabase? = null

        fun getDatabase(context: Context): AlarmROOMDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmROOMDatabase::class.java,
                    "alarm_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    class Converters {
        @TypeConverter
        fun fromArrayList(value: ArrayList<Int>?): String? {
            return value?.let {
                Gson().toJson(it)
            }
        }

        @TypeConverter
        fun fromInt(value: String?): ArrayList<Int>? {
            val arrayList = arrayListOf<Int>()
            val stringList = value?.let { it.split("[", ",", "]") }
            stringList?.let {
                if (it.contains("1")) {
                    arrayList.add(Calendar.SUNDAY)
                }
                if (it.contains("2")) {
                    arrayList.add(Calendar.MONDAY)
                }
                if (it.contains("3")) {
                    arrayList.add(Calendar.TUESDAY)
                }
                if (it.contains("4")) {
                    arrayList.add(Calendar.WEDNESDAY)
                }
                if (it.contains("5")) {
                    arrayList.add(Calendar.THURSDAY)
                }
                if (it.contains("6")) {
                    arrayList.add(Calendar.FRIDAY)
                }
                if (it.contains("7")) {
                    arrayList.add(Calendar.SATURDAY)
                }
            }
            if (arrayList.size == 0) {
                return null
            } else {
                return arrayList
            }
        }
    }
}