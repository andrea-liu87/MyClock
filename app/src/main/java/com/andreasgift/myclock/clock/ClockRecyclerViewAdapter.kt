package com.andreasgift.myclock.clock

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.databinding.ItemClockBinding

class ClockRecyclerViewAdapter(
    private var isAnalog: Boolean = false
) : RecyclerView.Adapter<ClockRecyclerViewAdapter.ClockViewHolder>() {

    private var clockList: ArrayList<Clock>? = null

    class ClockViewHolder(private val dataBinding: ItemClockBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
        fun bind(clock: Clock, isAna: Boolean) {
            dataBinding.apply {
                clockdata = clock
                isAnalog = isAna
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemClockBinding.inflate(layoutInflater, parent, false)
        return ClockViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        clockList?.let {
            holder.bind(clockList!!.get(position), isAnalog)
        }
    }

    override fun getItemCount(): Int {
        return clockList?.size ?: 0
    }

    fun getItem(position: Int): Clock? {
        return clockList?.get(position)
    }

    fun submitData(clockList: ArrayList<Clock>) {
        this.clockList = clockList
        notifyDataSetChanged()
    }

    fun changeAnalog(isAnalog: Boolean) {
        this.isAnalog = isAnalog
    }
}