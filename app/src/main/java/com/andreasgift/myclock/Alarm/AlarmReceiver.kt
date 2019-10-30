package com.andreasgift.myclock.Alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.andreasgift.myclock.Helper.Constants

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val nextintent = Intent(context, AlarmNotifActivity::class.java).apply {
            intent?.getStringExtra(Constants().ALARM_LABEL_KEY)?.let {
                this.putExtra(Constants().ALARM_LABEL_KEY, it)
            }
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context!!.startActivity(nextintent)
    }
}