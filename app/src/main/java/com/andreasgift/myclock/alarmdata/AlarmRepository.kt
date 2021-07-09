package com.andreasgift.myclock.alarmdata

import androidx.lifecycle.LiveData
import com.andreasgift.myclock.alarm.Alarm
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AlarmRepository {
    val allAlarmData: Flow<List<Alarm>>
    suspend fun insertAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun deleteAll()
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun turnOffAlarm(id: Int)
    suspend fun getAlarm(id: Int): LiveData<Alarm>
}

class AlarmRepositoryImpl @Inject constructor(private val alarmDao: AlarmDao) : AlarmRepository {

    override val allAlarmData: Flow<List<Alarm>> = alarmDao.getAllAlarm()

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

    override suspend fun turnOffAlarm(id: Int) {
        alarmDao.turnOff(id)
    }

    override suspend fun getAlarm(id: Int): LiveData<Alarm> {
        return alarmDao.getAlarm(id)
    }
}