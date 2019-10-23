package com.andreasgift.myclock.AlarmData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.andreasgift.myclock.Alarm.Alarm
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val alarmRepo: AlarmRepository

    val allAlarmData: LiveData<List<Alarm>>

    init {
        val alarmDao = AlarmROOMDatabase.getDatabase(application).alarmDao()
        alarmRepo = AlarmRepository(alarmDao)
        allAlarmData = alarmRepo.allAlarmData
    }

    fun insertAlarm(alarm: Alarm) = viewModelScope.launch {
        alarmRepo.insertAlarm(alarm)
    }

    fun deleteAlarm(alarm: Alarm) = viewModelScope.launch {
        alarmRepo.deleteAlarm(alarm)
    }

    fun deleteAll() = viewModelScope.launch {
        alarmRepo.deleteAll()
    }
}