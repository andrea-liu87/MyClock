package com.andreasgift.myclock

import android.os.Build
import android.view.View
import android.widget.TextClock
import android.widget.TextView
import android.widget.TimePicker
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import com.andreasgift.myclock.alarm.Alarm
import com.andreasgift.myclock.clock.Clock
import java.text.SimpleDateFormat
import java.util.*

object BindingAdapter {

    @BindingAdapter("app:setTime")
    @JvmStatic
    fun setTimeText(view: TextView, alarm: Alarm) {
        if (alarm.isAM) {
            view.text = String.format(
                "%02d:%02d AM",
                alarm.alarmHour,
                alarm.alarmMin
            )
        } else {
            view.text = String.format(
                "%02d:%02d PM",
                alarm.alarmHour % 12,
                alarm.alarmMin
            )
        }
    }

    @BindingAdapter("app:visibleOrGone")
    @JvmStatic
    fun setVisible(view: View, isVisible: Boolean) {
        if (isVisible) {
            view.visibility = View.VISIBLE
        } else {
            view.visibility = View.GONE
        }
    }

    @BindingAdapter("app:setDate")
    @JvmStatic
    fun setDate(tv: TextView, clock: Clock) {
        val formattedDate = SimpleDateFormat("EEEE, d MMMM")
        val countryTZ = clock.timeZone

        countryTZ?.let {
            formattedDate.timeZone = TimeZone.getTimeZone(countryTZ)
            tv.text = formattedDate.format(Calendar.getInstance().time)
        } ?: run {
            tv.text = formattedDate.format(Calendar.getInstance().time)
        }
    }

    @BindingAdapter("app:setCountry")
    @JvmStatic
    fun setCountry(tv: TextView, clock: Clock) {
        val countryTZ = clock.timeZone

        countryTZ?.let {
            tv.text = countryTZ
        } ?: run {
            tv.text = ""
        }
    }

    @BindingAdapter("app:setTZ")
    @JvmStatic
    fun setTimezone(view: TextClock, clock: Clock) {
        val countryTZ = clock.timeZone
        countryTZ?.let {
            view.timeZone = countryTZ
        }
    }

    @BindingAdapter("app:setTimezone")
    @JvmStatic
    fun setTimezone(view: com.arbelkilani.clock.Clock, clock: Clock) {
        val countryTZ = clock.timeZone
        countryTZ?.let {
            view.setTimezone(countryTZ)
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @BindingAdapter("app:setTimePicker")
    @JvmStatic
    fun setTimePicker(view: TimePicker, alarm: Alarm?) {
        alarm?.let {
            view.hour = alarm.alarmHour
            view.minute = alarm.alarmMin
        }
    }

    @BindingAdapter("app:setDay")
    @JvmStatic
    fun setToogleBUtton(view: ToggleButton, day: Boolean?) {
        day?.let {
            view.isChecked = it
        }
    }
}