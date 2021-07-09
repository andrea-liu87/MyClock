package com.andreasgift.myclock.alarm

import android.content.Intent
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import com.andreasgift.myclock.alarmdata.AlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RescheduleAlarmsService : LifecycleService() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        alarmRepository.allAlarmData.asLiveData().observe(this, Observer<List<Alarm?>?> {
            fun onChanged(alarms: List<Alarm>) {
                for (a in alarms) {
                    if (a.isOn) {
                        a.setAlarmScheduleOn(this.applicationContext)
                    }
                }
            }
        })
        return START_STICKY
    }

}