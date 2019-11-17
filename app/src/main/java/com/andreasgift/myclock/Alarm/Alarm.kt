package com.andreasgift.myclock.Alarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.room.*
import com.andreasgift.myclock.Helper.Constants
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "alarm_data_table")
class Alarm(

    @ColumnInfo(name = "alarm_hour")
    var alarmHour: Int,

    @ColumnInfo(name = "alarm_min")
    var alarmMin: Int,

    @ColumnInfo(name = "is_am")
    var isAM: Boolean,

    var days: ArrayList<Int>?,

    @ColumnInfo(name = "is_on")
    var isOn: Boolean,

    var label: String,

    @PrimaryKey
    val id: Int
) {

    fun setAlarmSchOnOff(isOn: Boolean, activity: Activity) {
        if (isOn) {
            setAlarmScheduleOn(activity)
        }
        if (!isOn) {
            setAlarmScheduleOff(activity)
        }
    }


    fun setAlarmScheduleOn(activity: Activity) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("ALARM ON", Integer.toString(id))
        val alarmIntent = Intent(activity, AlarmReceiver::class.java).let { intent ->
            intent.putExtra(Constants().ALARM_LABEL_KEY, label)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            PendingIntent.getBroadcast(
                activity,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMin)
        }

        days?.let {
            it.forEach {
                calendar.set(Calendar.DAY_OF_WEEK, it) }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_HOUR,
                alarmIntent
            )
        } ?:run{
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                alarmIntent)
        }
    }

    fun setAlarmScheduleOff(activity: Activity) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("ALARM OFF", Integer.toString(id))
        val alarmIntent = Intent(activity, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(
                activity,
                id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )}
        alarmManager.cancel(alarmIntent)
    }
}