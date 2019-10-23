package com.andreasgift.myclock.AlarmData

import androidx.lifecycle.LiveData
import com.andreasgift.myclock.Alarm.Alarm

class AlarmRepository(private val alarmDao: AlarmDao) {

    val allAlarmData: LiveData<List<Alarm>> = alarmDao.getAllAlarm()

    suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    suspend fun deleteAll() {
        alarmDao.deleteAll()
    }
}