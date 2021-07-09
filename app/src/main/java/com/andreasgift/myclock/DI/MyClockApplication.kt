package com.andreasgift.myclock.DI

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.andreasgift.myclock.Helper.Constants
import com.dci.dev.ktimber.KTimber
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyClockApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannnel()
        KTimber.startWithFileLogger(this.applicationContext)
    }

    private fun createNotificationChannnel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                Constants.CHANNEL_ID,
                "Alarm Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}