package com.andreasgift.myclock.alarm

import android.app.AlarmManager
import android.app.KeyguardManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.alarmdata.AlarmViewModel
import com.andreasgift.myclock.databinding.ActivityRingBinding
import com.dci.dev.ktimber.logInfo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRingBinding
    private var label = "Wake Up .. "
    private val alarmViewModel by viewModels<AlarmViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        logInfo("onCrete RingActivity")
        showWhenLockedAndTurnScreenOn()
        super.onCreate(savedInstanceState)
        val intentdata = this.intent.getStringExtra(Constants.ALARM_LABEL_KEY)
        val id = this.intent.getIntExtra(Constants.ALARM_ID, 0)
        val isOneTimeAlarm = this.intent.getBooleanExtra(Constants.ALARM_NON_REPEATING, true)
        intentdata?.let { label = intentdata }
        if (isOneTimeAlarm) {
            alarmViewModel.getAlarm(id)?.observe(this, Observer {
                it.setAlarmScheduleOff(this)
                alarmViewModel.updateAlarm(it)
            })
        }

        binding = ActivityRingBinding.inflate(layoutInflater)
        binding.label = this.label

        setContentView(binding.root)
    }

    fun dismissButton(view: View) {
        logInfo("dismissButton RingActivity")
        val intentService = Intent(applicationContext, AlarmService::class.java)
        applicationContext.stopService(intentService)
        finish()
    }

    fun snoozeButton(view: View) {
        logInfo("snoozeButton RingActivity")
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val nextintent = Intent(this, AlarmService::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            nextintent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis() + Constants.snoozeTiming,
            pendingIntent
        )
        val intentService = Intent(applicationContext, AlarmService::class.java)
        applicationContext.stopService(intentService)
        finish()
    }

    private fun showWhenLockedAndTurnScreenOn() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                        or WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }
        with(getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requestDismissKeyguard(this@RingActivity, null)
            }
        }
    }
}