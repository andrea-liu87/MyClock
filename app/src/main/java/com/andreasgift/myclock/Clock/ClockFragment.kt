package com.andreasgift.myclock.Clock


import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.ClockRecyclerViewAdapter
import com.andreasgift.myclock.R
import com.andreasgift.myclock.Helper.Constants
import kotlinx.android.synthetic.main.fragment_clock.view.*
import kotlin.collections.ArrayList


/**
 * Fragment for Clock
 */
class ClockFragment : Fragment() {

    lateinit var sharedPref: SharedPreferences

    private var isAnalogshow: Boolean = false

    lateinit var  view: ViewGroup

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ClockRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref =
            activity!!.getSharedPreferences(Constants().PREF_KEY_MANUAL_CLOCK, Context.MODE_PRIVATE)
        isAnalogshow = sharedPref.getBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        view  = inflater.inflate(R.layout.fragment_clock, container, false) as ViewGroup

        viewAdapter = ClockRecyclerViewAdapter(getData(), isAnalogshow)
        recyclerView = view.findViewById<RecyclerView>(R.id.clock_rv).apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = viewAdapter
        }



        view.clock_switch.isChecked = isAnalogshow
        view.clock_switch.setOnCheckedChangeListener(switchCheckListener)
        view.add_clock_fab.setOnClickListener(fabClickListener)

        val swipeCallback = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // TODO remove function at adapter
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        return view
    }


    //Listener for Analog or Digital Clock Switch
    val switchCheckListener: CompoundButton.OnCheckedChangeListener =
        CompoundButton.OnCheckedChangeListener { switchView, isChecked ->
            if (isChecked) {
                sharedPref.edit() {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, true)
                    commit()
                }
                isAnalogshow = true
                viewAdapter.showAnalogClock()
            } else {
                sharedPref.edit {
                    putBoolean(Constants().PREF_KEY_MANUAL_CLOCK, false)
                }
                isAnalogshow = false
                viewAdapter.showDigitalClock()
            }
            viewAdapter.notifyDataSetChanged();
        }

    //Listener for fab button
    val fabClickListener: View.OnClickListener = View.OnClickListener {
        val dialog = CountryListFragment()
        dialog.show(activity!!.supportFragmentManager, "Country List Fragment")
        Toast.makeText(this.requireContext(), "Clock is added", Toast.LENGTH_LONG).show()
    }

    //Dummy data for testing
    fun getData(): ArrayList<Clock> {
        val clockList = arrayListOf<Clock>()
        clockList.add(Clock())
        clockList.add(Clock("America/Los_Angeles"))
        clockList.add(Clock("Singapore"))
        return clockList
    }
}
