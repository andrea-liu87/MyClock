package com.andreasgift.myclock

import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.clock.Clock
import com.andreasgift.myclock.clock.ClockFragment
import com.arbelkilani.clock.listener.ClockListener
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

        setClockType(holder, isAnalog)

        countryTZ?.let {
            formattedDate.timeZone = TimeZone.getTimeZone(countryTZ)
            holder.itemview.date_tv.text = formattedDate.format(Calendar.getInstance().time)
            holder.itemview.digital_clock_tv.timeZone = countryTZ
            holder.itemview.country_tv.text = countryTZ
            //holder.itemview.analog_clock.setTimezone(countryTZ) TODO waiting for the library to update code
        } ?: run {
            holder.itemview.date_tv.text = formattedDate.format(Calendar.getInstance().time)
            holder.itemview.country_tv.text = ""
            setClockType(holder, isAnalog)
        }
    }

    override fun getItemCount(): Int = clockList?.size ?: 0

    fun setData(data : ArrayList<Clock>){
        clockList = data
        notifyDataSetChanged()
    }

    fun setClockType(holder: ClockViewHolder, isAnalog: Boolean){
        if (isAnalog) {
            holder.itemview.digital_clock_tv.visibility = View.GONE
            holder.itemview.analog_clock.visibility = View.VISIBLE
        } else{
            holder.itemview.analog_clock.visibility = View.GONE
            holder.itemview.digital_clock_tv.visibility = View.VISIBLE
        }
    }

    fun showDigitalClock() {
        isAnalog = false
    }

    fun showAnalogClock() {
        isAnalog = true
    }

}