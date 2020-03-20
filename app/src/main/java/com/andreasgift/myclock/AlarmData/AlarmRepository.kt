package com.andreasgift.myclock.AlarmData

import androidx.lifecycle.LiveData
import com.andreasgift.myclock.Alarm.Alarm

interface AlarmRepository {
    val allAlarmData: LiveData<List<Alarm>>
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun deleteAll()
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun getAlarm(id: Int): LiveData<Alarm>
}

class AlarmRepositoryImpl(private val dao: AlarmDao) : AlarmRepository {

    val alarmDao = dao

    override val allAlarmData: LiveData<List<Alarm>> = alarmDao.getAllAlarm()

    override suspend fun insertAlarm(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm)
    }

    override suspend fun deleteAll() {
        alarmDao.deleteAll()
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.update(alarm)
    }

    override suspend fun getAlarm(id: Int): LiveData<Alarm> {
        return alarmDao.getAlarm(id)
    }
}