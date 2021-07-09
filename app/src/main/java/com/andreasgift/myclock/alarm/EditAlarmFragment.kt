package com.andreasgift.myclock.alarm

import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.andreasgift.myclock.Helper.putArgs
import com.andreasgift.myclock.R
import com.andreasgift.myclock.alarmdata.AlarmViewModel
import com.andreasgift.myclock.databinding.FragmentAlarmEditBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

private const val ALARM_KEY = "alarmKey"

@AndroidEntryPoint
class EditAlarmFragment : DialogFragment() {
    private var _binding: FragmentAlarmEditBinding? = null
    private val binding get() = _binding!!

    private var alarm: Alarm? = null
    private val alarmViewModel by activityViewModels<AlarmViewModel>()

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
        _binding = FragmentAlarmEditBinding.inflate(inflater, null, false)
        val view = binding.root
        alarm?.let {
            binding.alarmdata = alarm
        }
        builder.setView(view)
            .setPositiveButton(
                context.getString(R.string.set),
                DialogInterface.OnClickListener { dialog, id ->
                    alarm?.let {
                        updateAlarm(it)
                    } ?: run {
                        addNewAlarm()
                    }
                })
            .setNegativeButton(context.getString(R.string.cancel),
                DialogInterface.OnClickListener { dialog, id ->
                })
        return builder
    }

    @TargetApi(23)
    private fun addNewAlarm() {
        val newAlarm = Alarm(
            binding.timePicker.hour,
            binding.timePicker.minute,
            binding.timePicker.hour < 12,
            alarmRepeatScheduleList(),
            true,
            binding.labelEt.text.toString(),
            (0..10000).random()
        )
        newAlarm.setAlarmScheduleOn(this.requireActivity())
        alarmViewModel.insertAlarm(newAlarm)
    }

    @TargetApi(23)
    private fun updateAlarm(alarm: Alarm) {
        alarm.setAlarmScheduleOff(this.requireActivity())
        val updatedAlarm = Alarm(
            binding.timePicker.hour,
            binding.timePicker.minute,
            binding.timePicker.hour < 12,
            alarmRepeatScheduleList(),
            true,
            binding.labelEt.text.toString(),
            alarm.id
        )
        updatedAlarm.setAlarmScheduleOn(this.requireActivity())
        alarmViewModel.updateAlarm(updatedAlarm)
    }

    private fun alarmRepeatScheduleList(): ArrayList<Int>? {
        val arrayList = arrayListOf<Int>()
        if (binding.monButton.isChecked) {
            arrayList.add(Calendar.MONDAY)
        }
        if (binding.tueButton.isChecked) {
            arrayList.add(Calendar.TUESDAY)
        }
        if (binding.wedButton.isChecked) {
            arrayList.add(Calendar.WEDNESDAY)
        }
        if (binding.thuButton.isChecked) {
            arrayList.add(Calendar.THURSDAY)
        }
        if (binding.friButton.isChecked) {
            arrayList.add(Calendar.FRIDAY)
        }
        if (binding.satButton.isChecked) {
            arrayList.add(Calendar.SATURDAY)
        }
        if (binding.sunButton.isChecked) {
            arrayList.add(Calendar.SUNDAY)
        }
        if (arrayList.size == 0) {
            return null
        } else {
            return arrayList
        }
    }
}