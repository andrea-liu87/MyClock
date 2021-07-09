package com.andreasgift.myclock.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.andreasgift.myclock.Helper.Constants
import com.dci.dev.ktimber.logInfo
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


    fun setAlarmScheduleOn(context: Context) {
        days?.let {
            it.forEach {
                setRepeatingAlarm(it, context)
            }
        } ?: run {
            setOneTimeAlarm(context)
        }
        if (!isOn) {
            isOn = true
        }
        logInfo("setAlarmSecheduleOn $alarmHour.$alarmMin Alarm")
    }

    fun setAlarmScheduleOff(activity: Context) {
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        days?.let {
            it.forEach {
                val alarmIntent = createAlarmPendingIntent(activity, id * 10 + it, false)
                alarmManager.cancel(alarmIntent)
            }
        } ?: run {
            val alarmIntent = createAlarmPendingIntent(activity, id, true)
            alarmManager.cancel(alarmIntent)
        }
        if (isOn) {
            isOn = false
        }
        logInfo("setAlarmSecheduleOff $alarmHour.$alarmMin Alarm")
    }

    private fun setRepeatingAlarm(dayOfWeek: Int, context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = createCalendarAlarm(dayOfWeek)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 7)
        }

        val repeatAlarmId = id * 10 + dayOfWeek
        val alarmIntent = createAlarmPendingIntent(context, repeatAlarmId, false)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            24 * 60 * 60 * 1000 * 7,
            alarmIntent
        )
        logInfo("setRepeatingAlarm $dayOfWeek Alarm")
    }

    private fun setOneTimeAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val calendar = createCalendarAlarm()
        val alarmIntent = createAlarmPendingIntent(context, id, true)

        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            alarmIntent
        )
        logInfo("setOneTimeAlarm $alarmHour.$alarmMin Alarm")
    }

    private fun createCalendarAlarm(dayOfWeek: Int? = null): Calendar {
        val now = Calendar.getInstance()
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            if (dayOfWeek != null) {
                set(Calendar.DAY_OF_WEEK, dayOfWeek)
            } else {
                // if alarm time has already passed, increment day by 1 (only for non repeating)
                if (alarmHour < now.get(Calendar.HOUR_OF_DAY) && alarmMin < now.get(Calendar.MINUTE)) {
                    set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH) + 1)
                }
            }
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMin)
            if (!isAM) {
                set(Calendar.AM_PM, Calendar.PM)
            }
        }
        return calendar
    }

    private fun createAlarmPendingIntent(
        context: Context,
        intentId: Int,
        isOneTime: Boolean
    ): PendingIntent {
        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            intent.putExtra(Constants.ALARM_LABEL_KEY, label)
            intent.putExtra(Constants.ALARM_ID, id)
            intent.putExtra(Constants.ALARM_NON_REPEATING, isOneTime)
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