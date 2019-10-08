package com.andreasgift.myclock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.clock.Clock
import kotlinx.android.synthetic.main.item_clock.view.*
import java.text.SimpleDateFormat

class ClockRecyclerViewAdapter(private var clockList : ArrayList<Clock>?) : RecyclerView.Adapter<ClockRecyclerViewAdapter.ClockViewHolder>() {

    class ClockViewHolder(val itemview : View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clock, parent, false)
        return ClockViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        val formattedDate = SimpleDateFormat("EEEE, d MMMM")
        var calendar = clockList!!.get(position)!!.timezoneCalendar
        holder.itemview.date_tv.text = formattedDate.format(calendar.time)
    }

    override fun getItemCount(): Int = clockList?.size ?: 0

    fun setData(data : ArrayList<Clock>){
        clockList = data
        notifyDataSetChanged()
    }
}