package com.andreasgift.myclock.Alarm

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.andreasgift.myclock.Helper.Constants


class AlarmReceiver : BroadcastReceiver() {
    private val snoozeTiming = 600000L
    private val CHANNEL_ID = "notification-channel-id"
    private val NOTIFICATION_ID = 1254
    private var label = ""

    private lateinit var ringtone: Ringtone
    private lateinit var soundUri: Uri

    override fun onReceive(context: Context, intent: Intent) {
        label = intent.getStringExtra(Constants().ALARM_LABEL_KEY)
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val mPowerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (mPowerManager.isInteractive) {
            createNotificationChannel(context)
            val builder = createNotificationBuilder(context, label)
            val notification = builder.build()
            notification.flags = Notification.FLAG_INSISTENT
            notification.flags = Notification.FLAG_NO_CLEAR
            with(NotificationManagerCompat.from(context)) {
                notify(NOTIFICATION_ID, notification)
            }
        } else {
            Intent(context, AlarmNotifActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                putExtra(Constants().ALARM_LABEL_KEY, label)
                context.startActivity(this)
            }
        }
    }

    fun createNotificationBuilder(
        context: Context,
        content: String = ""
    ): NotificationCompat.Builder {
        val notificationBuiler = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setContentTitle(context.getString(com.andreasgift.myclock.R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOngoing(true)
            .setSound(soundUri)
            .addAction(
                android.R.drawable.ic_lock_idle_alarm,
                "SNOOZE",
                snoozeButtonPendingIntent(context, content)
            )
            .addAction(
                android.R.drawable.ic_menu_close_clear_cancel,
                "DISMISS",
                dismissPendingIntent(context)
            )
        return notificationBuiler
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "ALARM-NOTIFICATION-CHANNEL"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = "Notification for alarm ringing"
            }
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(soundUri, audioAttributes)
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun snoozeButtonPendingIntent(context: Context, label: String): PendingIntent {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(context, AlarmReceiver::class.java)
        nextintent.putExtra(Constants().ALARM_LABEL_KEY, label)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            21,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.set(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + snoozeTiming,
            pendingIntent
        )
        return pendingIntent
    }

    private fun dismissPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, dismissAlarmReceiver::class.java)
        intent.putExtra("notification_id", NOTIFICATION_ID)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            15,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        return pendingIntent
    }

    class dismissAlarmReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val id = intent.getIntExtra("notification_id", 1254)
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(id)
        }
    }
}