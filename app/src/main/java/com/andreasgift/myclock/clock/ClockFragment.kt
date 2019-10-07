package com.andreasgift.myclock.clock


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.R
import com.andreasgift.myclock.helper.Constants
import kotlinx.android.synthetic.main.fragment_clock.view.*
import java.text.SimpleDateFormat
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class ClockFragment : Fragment() {

    private var calendar = Calendar.getInstance()

    val sharedPref = activity?.getSharedPreferences(Constants().PREF_KEY_MANUAL_CLOCK, Context.MODE_PRIVATE)

    lateinit var  view: ViewGroup


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view  = inflater.inflate(R.layout.fragment_clock, container, false) as ViewGroup

        view.clock_switch.setOnCheckedChangeListener { switchView, isChecked ->
            if (isChecked){
                showAnalogClock()
                sharedPref?.edit {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, true)
                    commit()}
            } else {
                showDigitalCLock()
                sharedPref?.edit {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)
                    commit()}
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        setupPreferences()
        setupCalendar(calendar)
    }

    fun setupCalendar(mcalendar: Calendar){
        val formattedDate = SimpleDateFormat("EEEE, d MMMM")
        view.date_tv.text = formattedDate.format(mcalendar.time)
    }

    fun setupPreferences(){
        val isAnalogshow = sharedPref?.getBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)
        if (isAnalogshow == true){
            showAnalogClock()
        } else {
            showDigitalCLock()
        }
    }

    private fun showAnalogClock() {
        view.digital_clock_tv.visibility = View.GONE
        view.analog_clock.visibility = View.VISIBLE
    }

    private fun showDigitalCLock() {
        view.digital_clock_tv.visibility = View.VISIBLE
        view.analog_clock.visibility = View.GONE
    }
}
