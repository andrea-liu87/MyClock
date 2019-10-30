package com.andreasgift.myclock.Alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.AlarmData.AlarmViewModel
import com.andreasgift.myclock.Clock.SwipeCallback
import kotlinx.android.synthetic.main.fragment_alarm.view.*
import java.util.*
import kotlin.collections.ArrayList


class AlarmFragment : Fragment(), AlarmRecyclerViewAdapter.switchHandler {

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AlarmRecyclerViewAdapter

    private lateinit var alarmViewModel: AlarmViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmViewModel = ViewModelProviders.of(this).get(AlarmViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            com.andreasgift.myclock.R.layout.fragment_alarm,
            container,
            false
        ) as ViewGroup

        viewAdapter = AlarmRecyclerViewAdapter(null, this)
        recyclerView =
            view.findViewById<RecyclerView>(com.andreasgift.myclock.R.id.alarm_rv).apply {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(this.context)
                adapter = viewAdapter
            }
        recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        alarmViewModel.allAlarmData.observe(this, Observer {
            viewAdapter.setData(ArrayList(it))
        })

        val swipedCallback = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val alarm: Alarm = viewAdapter.getItem(position)
                alarmViewModel.deleteAlarm(alarm)
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipedCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        view.add_alarm_fab.setOnClickListener(fabListener)

        return view
    }

    override fun updateAlarmData(alarm: Alarm) {
        alarmViewModel.updateAlarm(alarm)
        alarm.setAlarmSchOnOff(alarm.isOn, this.requireActivity())
    }

    val fabListener = View.OnClickListener {
        val fragment = EditAlarmFragment()
        fragment.show(activity!!.supportFragmentManager, "EditAlarmFragment")
    }
}