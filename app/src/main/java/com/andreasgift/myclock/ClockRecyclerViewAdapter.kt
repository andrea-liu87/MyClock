package com.andreasgift.myclock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.clock.Clock
import kotlinx.android.synthetic.main.item_clock.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ClockRecyclerViewAdapter(
    private var clockList: ArrayList<Clock>?,
    private var isAnalog: Boolean = false
) : RecyclerView.Adapter<ClockRecyclerViewAdapter.ClockViewHolder>() {

    class ClockViewHolder(val itemview : View) : RecyclerView.ViewHolder(itemview)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_clock, parent, false)
        return ClockViewHolder(view)
    }

    override fun onBindViewHolder(holder: ClockViewHolder, position: Int) {
        val formattedDate = SimpleDateFormat("EEEE, d MMMM")
        val countryTZ = clockList?.get(position)?.timeZone
        countryTZ?.let {
            formattedDate.timeZone = TimeZone.getTimeZone(countryTZ)
            holder.itemview.date_tv.text = formattedDate.format(Calendar.getInstance().time)
            holder.itemview.digital_clock_tv.timeZone = countryTZ
            holder.itemview.country_tv.text = countryTZ
        } ?: run {
            holder.itemview.date_tv.text = formattedDate.format(
                Calendar.getInstance().time
            )
            holder.itemview.country_tv.text = ""
        }
    }

    override fun getItemCount(): Int = clockList?.size ?: 0

    fun setData(data : ArrayList<Clock>){
        clockList = data
        notifyDataSetChanged()
    }

    fun showDigitalClock() {
        //TODO show digital clock method
    }

    fun showAnalogClock() {
        //TODO show analog clock method
    }
}