package com.andreasgift.myclock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.andreasgift.myclock.Helper.Constants
import com.dci.dev.ktimber.logInfo

class ActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        logInfo("onReceiver ActionReceiver")
        if (intent.getBooleanExtra("DISMISSALARM", false)) {
            val intentService = Intent(context, AlarmService::class.java)
            context.stopService(intentService)
        }
        if (intent.getBooleanExtra("SNOOZEALARM", false)) {
            snooze(context)
        }
    }

    fun snooze(context: Context) {
        logInfo("snooze ActionReceiver")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(context, AlarmService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + Constants.snoozeTiming,
            pendingIntent
        )
        val intentService = Intent(context, AlarmService::class.java)
        context.stopService(intentService)
    }
}