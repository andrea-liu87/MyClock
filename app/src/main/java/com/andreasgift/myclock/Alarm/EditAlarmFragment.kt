package com.andreasgift.myclock.Alarm

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.andreasgift.myclock.AlarmData.AlarmViewModel
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.fragment_alarm_edit.view.*
import java.util.*
import kotlin.collections.ArrayList

class EditAlarmFragment(var alarm: Alarm? = null) : DialogFragment() {

    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel::class.java)
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

        builder.setView(view)
            .setPositiveButton(context.getString(R.string.set),
                DialogInterface.OnClickListener { dialog, id ->
                    alarm?.let {
                        updateAlarm(it)
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
    private fun addNewAlarm(view: View) {
        val newAlarm = Alarm(
            view.time_picker.hour,
            view.time_picker.minute,
            view.time_picker.hour < 12,
            alarmToggleGetData(view),
            true,
            view.label_et.text.toString() ?: ""
        )
        alarmViewModel.insertAlarm(newAlarm)
        newAlarm.setAlarmScheduleOn(this.requireActivity(), newAlarm.id)
    }

    private fun updateAlarm(alarm: Alarm) {

    }

    private fun alarmToggleGetData(view: View): ArrayList<Int>? {
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