package com.andreasgift.myclock.clock


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.ClockRecyclerViewAdapter
import com.andreasgift.myclock.R
import com.andreasgift.myclock.helper.Constants
import kotlinx.android.synthetic.main.fragment_clock.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class ClockFragment : Fragment() {

    val sharedPref = activity?.getSharedPreferences(Constants().PREF_KEY_MANUAL_CLOCK, Context.MODE_PRIVATE)
    val isAnalogshow  = sharedPref?.getBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)

    lateinit var  view: ViewGroup

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ClockRecyclerViewAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view  = inflater.inflate(R.layout.fragment_clock, container, false) as ViewGroup

        viewManager = LinearLayoutManager(this.context)
        viewAdapter = ClockRecyclerViewAdapter(getData())

        recyclerView = view.findViewById<RecyclerView>(R.id.clock_rv).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        view.clock_switch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked){
                sharedPref?.edit {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, true)
                    commit()}
            } else {
                sharedPref?.edit {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)
                    commit()}
            }
        }
        return view
    }

    override fun onResume() {
        super.onResume()
        getData()
    }


    fun getData() : ArrayList<Clock>{
        val clockList = arrayListOf<Clock>()
        clockList.add(Clock(Calendar.getInstance()))
        clockList.add(Clock(Calendar.getInstance()))
        return clockList
    }
}
