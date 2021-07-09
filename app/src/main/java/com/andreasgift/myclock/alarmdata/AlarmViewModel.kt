package com.andreasgift.myclock.alarmdata

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andreasgift.myclock.alarm.Alarm
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlarmViewModel @Inject constructor(private val alarmRepo: AlarmRepository) : ViewModel() {
    val allAlarmData: LiveData<List<Alarm>> = alarmRepo.allAlarmData.asLiveData()

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

    fun turnOffAlarm(id: Int) {
        viewModelScope.launch {
            alarmRepo.turnOffAlarm(id)
        }
    }
}