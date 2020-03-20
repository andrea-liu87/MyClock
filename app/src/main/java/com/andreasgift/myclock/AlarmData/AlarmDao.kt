package com.andreasgift.myclock.AlarmData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andreasgift.myclock.Alarm.Alarm

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm_data_table ORDER BY id ASC")
    fun getAllAlarm(): LiveData<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("DELETE FROM alarm_data_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(alarm: Alarm)

    @Query("SELECT * FROM alarm_data_table WHERE id = :id")
    fun getAlarm(id: Int): LiveData<Alarm>
}