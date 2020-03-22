package com.andreasgift.myclock.Alarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreasgift.myclock.Helper.Constants
import java.io.Serializable
import java.util.*

@Entity(tableName = "alarm_data_table")
class Alarm(

    @ColumnInfo(name = "alarm_hour")
    var alarmHour: Int,

    @ColumnInfo(name = "alarm_min")
    var alarmMin: Int,

    @ColumnInfo(name = "is_am")
    var isAM: Boolean,

    @ColumnInfo(name = "alarm_days")
    var days: ArrayList<Int>?,

    @ColumnInfo(name = "is_on")
    var isOn: Boolean,

    @ColumnInfo(name = "label")
    var label: String,

    @PrimaryKey
    val id: Int
) : Serializable {


    fun setAlarmScheduleOn(activity: Activity) {
        days?.let {
            it.forEach {
                setRepeatingAlarm(it, activity)
            }
        } ?: run {
            setOneTimeAlarm(activity)
        }
        if (!isOn) {
            isOn = true
        }
    }

    fun setAlarmScheduleOff(activity: Context) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        Log.d("alarm id : ", Integer.toString(id) + " is turned off")
        days?.let {
            it.forEach {
                val alarmIntent = createAlarmPendingIntent(activity, id * 10 + it)
                alarmManager.cancel(alarmIntent)
            }
        } ?: run {
            val alarmIntent = createAlarmPendingIntent(activity, id)
            alarmManager.cancel(alarmIntent)
        }
        if (isOn) {
            isOn = false
        }
    }

    private fun setRepeatingAlarm(dayOfWeek: Int, context: Context) {
        Log.d("alarm id : repeating al", dayOfWeek.toString())
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = createCalendarAlarm(dayOfWeek)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 7)
        }

        val repeatAlarmId = id * 10 + dayOfWeek
        val alarmIntent = createAlarmPendingIntent(context, repeatAlarmId)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY * 7,
            alarmIntent
        )
    }

    private fun setOneTimeAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = createCalendarAlarm()
        val alarmIntent = createAlarmPendingIntent(context, id)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
    }

    private fun createCalendarAlarm(dayOfWeek: Int? = null): Calendar {
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            if (dayOfWeek != null) {
                set(Calendar.DAY_OF_WEEK, dayOfWeek)
            }
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMin)
            if (!isAM) {
                set(Calendar.AM_PM, Calendar.PM)
            }
        }
        return calendar
    }

    private fun createAlarmPendingIntent(context: Context, intentId: Int): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra(Constants().ALARM_LABEL_KEY, label)
            intent.putExtra(Constants().ALARM_ID, id)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            PendingIntent.getBroadcast(
                context,
                intentId,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        return alarmIntent
    }
}