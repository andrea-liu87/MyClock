package com.andreasgift.myclock.AlarmData

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andreasgift.myclock.Alarm.Alarm
import kotlinx.coroutines.launch

class AlarmViewModel(val alarmRepository: AlarmRepository) : ViewModel() {

    private val alarmRepo: AlarmRepository

    val allAlarmData: LiveData<List<Alarm>>

    init {
        alarmRepo = alarmRepository
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

    fun updateAlarm(alarm: Alarm) = viewModelScope.launch {
        alarmRepo.updateAlarm(alarm)
    }

    fun getAlarm(id: Int): LiveData<Alarm>? {
        var alarm: LiveData<Alarm>? = null
        viewModelScope.launch {
            alarm = alarmRepo.getAlarm(id)
        }
        return alarm
    }
}