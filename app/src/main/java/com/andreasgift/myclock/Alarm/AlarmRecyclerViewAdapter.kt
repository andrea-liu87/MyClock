package com.andreasgift.myclock.Alarm

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.item_alarm.view.*
import java.util.*
import kotlin.collections.ArrayList

class AlarmRecyclerViewAdapter(
    private var alarmList: ArrayList<Alarm>?
) : RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmViewHolder>() {

    class AlarmViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm, parent, false)
        return AlarmViewHolder(view)
    }

    override fun getItemCount(): Int {
        return alarmList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        val alarm = alarmList!!.get(position)
        holder.itemView.label_alarm_tv.setText(alarm.label)
        holder.itemView.alarm_switch.isChecked = alarm.isOn
        setTimeText(alarm, holder)
        setDaysToogle(alarm, holder)
    }

    /**
     * Set days for repeating alarm toggle button
     */
    private fun setDaysToogle(
        alarm: Alarm,
        holder: AlarmViewHolder
    ) {
        alarm.days?.let {
            if (it.contains(Calendar.MONDAY)) {
                holder.itemView.mon_button.isChecked = true
            }
            if (it.contains(Calendar.TUESDAY)) {
                holder.itemView.tue_button.isChecked = true
            }
            if (it.contains(Calendar.WEDNESDAY)) {
                holder.itemView.wed_button.isChecked = true
            }
            if (it.contains(Calendar.THURSDAY)) {
                holder.itemView.thu_button.isChecked = true
            }
            if (it.contains(Calendar.FRIDAY)) {
                holder.itemView.fri_button.isChecked = true
            }
            if (it.contains(Calendar.SATURDAY)) {
                holder.itemView.sat_button.isChecked = true
            }
            if (it.contains(Calendar.SUNDAY)) {
                holder.itemView.sat_button.isChecked = true
            }
        }
    }

    /**
     * Set text for alarm timing
     */
    private fun setTimeText(
        alarm: Alarm,
        holder: AlarmViewHolder
    ) {
        if (alarm.isAM) {
            holder.itemView.alarm_tv.setText(
                String.format(
                    "%02d:%02d AM",
                    alarm.alarmHour,
                    alarm.alarmMin
                )
            )
        } else {
            holder.itemView.alarm_tv.setText(
                String.format(
                    "%02d:%02d PM",
                    alarm.alarmHour % 12,
                    alarm.alarmMin
                )
            )
        }
    }

    fun getItem(position: Int): Alarm {
        return alarmList!!.get(position)
    }

    fun setData(data: ArrayList<Alarm>) {
        alarmList = data
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        alarmList!!.remove(alarmList!!.get(position))
        notifyDataSetChanged()
    }
}