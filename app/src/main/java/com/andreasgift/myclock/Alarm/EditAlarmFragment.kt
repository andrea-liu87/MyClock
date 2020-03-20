package com.andreasgift.myclock.Alarm

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.andreasgift.myclock.AlarmData.AlarmViewModel
import com.andreasgift.myclock.Helper.putArgs
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.fragment_alarm_edit.view.*
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*

private const val ALARM_KEY = "alarmKey"

class EditAlarmFragment : DialogFragment() {
    private var alarm: Alarm? = null
    val alarmViewModel: AlarmViewModel by sharedViewModel<AlarmViewModel>()

    companion object {
        fun newInstance(alarm: Alarm? = null) = EditAlarmFragment().putArgs {
            putSerializable(ALARM_KEY, alarm)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        alarm = arguments?.getSerializable(ALARM_KEY) as Alarm?
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = buildDialog(requireContext())
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    private fun buildDialog(context: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(context)

        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.fragment_alarm_edit, null)
        alarm?.let {
            updateUi(view, it)
        }
        builder.setView(view)
            .setPositiveButton(context.getString(R.string.set),
                DialogInterface.OnClickListener { dialog, id ->
                    alarm?.let {
                        updateAlarm(view, it)
                    } ?: run {
                        addNewAlarm(view)
                    }
                })
            .setNegativeButton(context.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id ->
                })
        return builder
    }

    @TargetApi(23)
    private fun updateUi(view: View, alarm: Alarm) {
        view.time_picker.hour = alarm.alarmHour
        view.time_picker.minute = alarm.alarmMin
        view.label_et.setText(alarm.label)
        alarm.days?.forEach {
            when (it) {
                Calendar.MONDAY -> view.mon_button.isChecked = true
                Calendar.TUESDAY -> view.tue_button.isChecked = true
                Calendar.WEDNESDAY -> view.wed_button.isChecked = true
                Calendar.THURSDAY -> view.thu_button.isChecked = true
                Calendar.FRIDAY -> view.fri_button.isChecked = true
                Calendar.SATURDAY -> view.sat_button.isChecked = true
                Calendar.SUNDAY -> view.sun_button.isChecked = true
            }
        }
    }

    @TargetApi(23)
    private fun addNewAlarm(view: View) {
        val newAlarm = Alarm(
            view.time_picker.hour,
            view.time_picker.minute,
            view.time_picker.hour < 12,
            alarmRepeatScheduleList(view),
            true,
            view.label_et.text.toString(),
            (0..10000).random()
        )
        alarmViewModel.insertAlarm(newAlarm)
        Log.d("alarm id : ", newAlarm.id.toString() + " is added")
        newAlarm.setAlarmScheduleOn(this.requireActivity())
    }

    @TargetApi(23)
    private fun updateAlarm(view: View, alarm: Alarm) {
        alarm.setAlarmScheduleOff(this.requireActivity())
        val updatedAlarm = Alarm(
            view.time_picker.hour,
            view.time_picker.minute,
            view.time_picker.hour < 12,
            alarmRepeatScheduleList(view),
            true,
            view.label_et.text.toString(),
            alarm.id
        )
        alarmViewModel.updateAlarm(updatedAlarm)
        updateUi(view, updatedAlarm)
        Log.d("alarm id : ", alarm.id.toString() + " is updated")
        updatedAlarm.setAlarmScheduleOn(this.requireActivity())
    }

    private fun alarmRepeatScheduleList(view: View): ArrayList<Int>? {
        val arrayList = arrayListOf<Int>()
        if (view.mon_button.isChecked) {
            arrayList.add(Calendar.MONDAY)
        }
        if (view.tue_button.isChecked) {
            arrayList.add(Calendar.TUESDAY)
        }
        if (view.wed_button.isChecked) {
            arrayList.add(Calendar.WEDNESDAY)
        }
        if (view.thu_button.isChecked) {
            arrayList.add(Calendar.THURSDAY)
        }
        if (view.fri_button.isChecked) {
            arrayList.add(Calendar.FRIDAY)
        }
        if (view.sat_button.isChecked) {
            arrayList.add(Calendar.SATURDAY)
        }
        if (view.sun_button.isChecked) {
            arrayList.add(Calendar.SUNDAY)
        }
        if (arrayList.size == 0) {
            return null
        } else {
            return arrayList
        }
    }
}