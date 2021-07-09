package com.andreasgift.myclock.alarm

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.media.RingtoneManager
import android.os.Vibrator
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.Observer
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.R
import com.andreasgift.myclock.alarmdata.AlarmRepository
import com.andreasgift.myclock.alarmdata.AlarmViewModel
import com.dci.dev.ktimber.logInfo
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AlarmService : LifecycleService() {
    @Inject
    lateinit var alarmRepository: AlarmRepository

    lateinit var mediaPlayer: MediaPlayer
    lateinit var vibrator: Vibrator
    lateinit var label: String
    private var isAlarmOneTime: Boolean = true
    private var id: Int = 0
    lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate() {
        super.onCreate()
        logInfo("onCreate AlarmService")

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        mediaPlayer = MediaPlayer.create(this, soundUri)
        mediaPlayer.isLooping = true

        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        alarmViewModel = AlarmViewModel(alarmRepository)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        logInfo("onStartCommand AlarmService")
        label = intent?.getStringExtra(Constants.ALARM_LABEL_KEY) ?: ""
        id = intent?.getIntExtra(Constants.ALARM_ID, 0) ?: 0
        isAlarmOneTime = intent?.getBooleanExtra(Constants.ALARM_NON_REPEATING, true) ?: true

        val notification = createNotificationBuilder(this, label).build()
        mediaPlayer.start()
        val pattern: LongArray = longArrayOf(0, 100, 1000)
        vibrator.vibrate(pattern, 0)
        if (isAlarmOneTime) {
            alarmViewModel.getAlarm(id)?.observe(this, Observer {
                if (it.isOn) {
                    it.setAlarmScheduleOff(this)
                    alarmViewModel.updateAlarm(it)
                }
            })
            isAlarmOneTime = false
        }
        startForeground(1, notification)
        return START_STICKY
    }


    override fun onDestroy() {
        super.onDestroy()
        logInfo("onDestroy AlarmService")
        stopForeground(true)
        mediaPlayer.stop()
        vibrator.cancel()
    }

    fun createNotificationBuilder(
        context: Context,
        alarmLabel: String = ""
    ): NotificationCompat.Builder {
        val dismissIntent = Intent(this, ActionReceiver::class.java)
        dismissIntent.putExtra("DISMISSALARM", true)
        dismissIntent.putExtra(Constants.ALARM_ID, id)
        dismissIntent.putExtra(Constants.ALARM_NON_REPEATING, isAlarmOneTime)
        val dismissPendingIntent = PendingIntent.getBroadcast(this, 0, dismissIntent, 0)

        val snoozeIntent = Intent(this, ActionReceiver::class.java)
        snoozeIntent.putExtra("SNOOZEALARM", true)
        snoozeIntent.putExtra(Constants.ALARM_ID, id)
        snoozeIntent.putExtra(Constants.ALARM_NON_REPEATING, isAlarmOneTime)
        val snoozePendingIntent = PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)

        val notificationIntent = Intent(this, RingActivity::class.java)
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        notificationIntent.putExtra(Constants.ALARM_LABEL_KEY, label)
        notificationIntent.putExtra(Constants.ALARM_ID, id)
        notificationIntent.putExtra(Constants.ALARM_NON_REPEATING, isAlarmOneTime)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notStyle = NotificationCompat.BigTextStyle()
        val bigincon = BitmapFactory.decodeResource(resources, R.drawable.alarm_clock)
        val notificationBuiler = NotificationCompat.Builder(context, Constants.CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setLargeIcon(bigincon)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setOngoing(true)
            .setContentTitle(label)
            .setColor(resources.getColor(R.color.colorPrimary, null))
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_baseline_cancel, "DISMISS", dismissPendingIntent)
            .addAction(R.drawable.ic_alarm, "SNOOZE", snoozePendingIntent)
            .setStyle(notStyle)
        return notificationBuiler
    }
}