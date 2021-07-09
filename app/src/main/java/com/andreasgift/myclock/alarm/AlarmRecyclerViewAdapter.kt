package com.andreasgift.myclock.alarm

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Switch
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.R
import com.andreasgift.myclock.databinding.ItemAlarmBinding


class AlarmRecyclerViewAdapter : RecyclerView.Adapter<AlarmRecyclerViewAdapter.AlarmViewHolder>() {
    var onItemClickListener: (alarm: Alarm) -> Unit = {}
    var onCheckedChangeListener: (alarm: Alarm) -> Unit = {}
    private var alarmList: List<Alarm>? = null

    class AlarmViewHolder(private val dataBinding: ItemAlarmBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(alarm: Alarm) {
            dataBinding.apply {
                alarmdata = alarm
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlarmViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemAlarmBinding.inflate(layoutInflater, parent, false)
        return AlarmViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return alarmList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AlarmViewHolder, position: Int) {
        alarmList?.let {
            val alarm = alarmList!!.get(position)
            holder.bind(alarm)
            holder.itemView.setOnClickListener { onItemClickListener(alarm) }
            holder.itemView.findViewById<Switch>(R.id.alarm_switch)
                .setOnClickListener { onCheckedChangeListener(alarm) }
        }
    }

    fun getItem(position: Int): Alarm? {
        alarmList?.let {
            return alarmList!!.get(position)
        }
        return null
    }

    fun submitData(newList: List<Alarm>) {
        alarmList = newList
        notifyDataSetChanged()
    }
}