package com.andreasgift.myclock.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.Ringtone
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.activity_alarm_notif.*

class AlarmNotifActivity : AppCompatActivity() {
    private val snoozeTiming = 300000L

    private var label: String = ""
    private var ringtone: Ringtone? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm_notif)

        wakeScreen()

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this@AlarmNotifActivity, soundUri)

        intent.getStringExtra(Constants().ALARM_LABEL_KEY)?.let {
            label = it
            this.label_tv.text = label
        }
        playSound()
    }

    private fun wakeScreen() {
        val window = window
        window.addFlags(
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
        )
    }

    fun dismissButton() {
        stopSound()
        finish()
    }

    fun snoozeButton() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(this, AlarmReceiver::class.java)
        intent.putExtra(Constants().ALARM_LABEL_KEY, label)
        val pendingIntent = PendingIntent.getBroadcast(
            this@AlarmNotifActivity,
            0,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + snoozeTiming,
            pendingIntent
        )
        stopSound()
        finish()
    }

    private fun playSound() {
        if (Build.VERSION.SDK_INT >= 28) {
            ringtone?.isLooping = true
        }
        ringtone?.play()
    }

    private fun stopSound() {
        if (ringtone!!.isPlaying) {
            ringtone?.stop()
        }
    }
}
