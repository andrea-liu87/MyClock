package com.andreasgift.myclock.alarmdata

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andreasgift.myclock.alarm.Alarm
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Query("SELECT * FROM alarm_data_table ORDER BY id ASC")
    fun getAllAlarm(): Flow<List<Alarm>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(alarm: Alarm)

    @Delete
    suspend fun delete(alarm: Alarm)

    @Query("DELETE FROM alarm_data_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(alarm: Alarm)

    @Query("UPDATE alarm_data_table SET is_on = 1 WHERE id = :id")
    suspend fun turnOff(id: Int)

    @Query("SELECT * FROM alarm_data_table WHERE id = :id")
    fun getAlarm(id: Int): LiveData<Alarm>
}