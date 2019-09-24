package com.andreasgift.myclock.clock


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.fragment_clock.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class ClockFragment : Fragment() {

    private var calendar = Calendar.getInstance().time

    lateinit var  view: ViewGroup

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view  = inflater.inflate(R.layout.fragment_clock, container, false) as ViewGroup
        setupCalendar()
        return view
    }

    fun setupCalendar(){
        calendar = Calendar.getInstance().time
        val formattedDate = SimpleDateFormat("EEEE, d MMMM")
        view.date_tv.text = formattedDate.format(calendar)
    }
}
