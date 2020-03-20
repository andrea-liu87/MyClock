package com.andreasgift.myclock.Alarm

import android.app.AlarmManager
import android.app.IntentService
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.andreasgift.myclock.Helper.Constants

class SnoozeService : IntentService("snooze") {
    override fun onHandleIntent(intent: Intent?) {
        val id = intent?.getIntExtra("notification_id", 1254)
        val label = intent?.getStringExtra(Constants().ALARM_LABEL_KEY) ?: ""
        val snoozeTiming = 300000L

        val notificationManager =
            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        id?.let {
            notificationManager.cancel(id)
        }

        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(this, AlarmReceiver::class.java)
        intent?.putExtra(Constants().ALARM_LABEL_KEY, label)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + snoozeTiming,
            pendingIntent
        )
    }
}