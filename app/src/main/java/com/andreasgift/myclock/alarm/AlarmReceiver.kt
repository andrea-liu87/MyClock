package com.andreasgift.myclock.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.andreasgift.myclock.Helper.Constants
import com.dci.dev.ktimber.logInfo

class AlarmReceiver : BroadcastReceiver() {
    private var label: String? = ""
    private var alarmId: Int = 0
    private var isALarmOneTime: Boolean = true

    override fun onReceive(context: Context, intent: Intent) {
        logInfo("onReceiver AlarmReceiver")
        label = intent.getStringExtra(Constants.ALARM_LABEL_KEY) ?: ""
        alarmId = intent.getIntExtra(Constants.ALARM_ID, 0)
        isALarmOneTime = intent.getBooleanExtra(Constants.ALARM_NON_REPEATING, true)

        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.action)) {
            startRescheduleAlarmsService(context)
        } else {
            startAlarmService(context)
        }
    }

    private fun startAlarmService(context: Context) {
        logInfo("startAlarmService AlarmReceiver")
        val intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(Constants.ALARM_LABEL_KEY, label)
        intentService.putExtra(Constants.ALARM_ID, alarmId)
        intentService.putExtra(Constants.ALARM_NON_REPEATING, isALarmOneTime)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }

    private fun startRescheduleAlarmsService(context: Context) {
        val intentService = Intent(context, RescheduleAlarmsService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intentService)
        } else {
            context.startService(intentService)
        }
    }
}