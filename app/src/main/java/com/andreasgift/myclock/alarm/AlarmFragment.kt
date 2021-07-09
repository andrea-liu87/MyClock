package com.andreasgift.myclock.alarm

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.andreasgift.myclock.alarmdata.AlarmViewModel
import com.andreasgift.myclock.clock.SwipeCallback
import com.andreasgift.myclock.databinding.FragmentAlarmBinding
import com.dci.dev.ktimber.deleteLogsFile
import com.dci.dev.ktimber.logInfo
import com.dci.dev.ktimber.shareLogsFile
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AlarmFragment : Fragment() {
    private var _binding: FragmentAlarmBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: AlarmRecyclerViewAdapter

    private val alarmViewModel by activityViewModels<AlarmViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAlarmBinding.inflate(layoutInflater, container, false)
        val view = binding.root
        requestIgnoreBatteryOptimization(this.requireContext())
        loggingSetup(this.requireContext())
        logInfo("onCreateView AlaarmFragment")

        viewAdapter = AlarmRecyclerViewAdapter()
        viewAdapter.onItemClickListener = { alarm ->
            showEditAlarmFragment(alarm)
        }
        viewAdapter.onCheckedChangeListener = { alarm -> alarmSwitchChange(alarm) }
        recyclerView = binding.alarmRv.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = viewAdapter
        }
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        alarmViewModel.allAlarmData.observe(this.viewLifecycleOwner, Observer {
            viewAdapter.submitData(it)
        })

        val swipedCallback = object : SwipeCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val alarm: Alarm? = viewAdapter.getItem(position)
                alarm?.let {
                    alarmViewModel.deleteAlarm(alarm)
                    it.setAlarmScheduleOff(activity!!.applicationContext)
                }
            }
        }

        val itemTouchHelper = ItemTouchHelper(swipedCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

        binding.addAlarmFab.setOnClickListener(fabListener)

        return view
    }

    val fabListener = View.OnClickListener {
        showEditAlarmFragment()
    }

    private fun showEditAlarmFragment(alarm: Alarm? = null) {
        val fragment = EditAlarmFragment.newInstance(alarm)
        fragment.show(requireActivity().supportFragmentManager, "EditAlarmFragment")
    }

    private fun alarmSwitchChange(alarm: Alarm) {
        if (alarm.isOn) {
            alarm.setAlarmScheduleOff(this.requireActivity())
        } else {
            alarm.setAlarmScheduleOn(this.requireActivity())
        }
        alarmViewModel.updateAlarm(alarm)
    }

    private fun requestIgnoreBatteryOptimization(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !pm.isIgnoringBatteryOptimizations(
                    context.packageName
                )
            ) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:" + context.packageName)
                startActivity(intent)
            }
        } catch (e: Exception) {
            logInfo("requestIgnoreBatteryOptimization : " + e.toString())
        }
    }

    private fun loggingSetup(context: Context) {
        binding.sendLog.setOnClickListener {
            shareLogsFile(this.requireActivity(), "andrealiureta@gmail.com")
        }

        binding.deleteLog.setOnClickListener {
            deleteLogsFile(context)
            Toast.makeText(context, "Log history is deleted", Toast.LENGTH_LONG).show()
        }
    }
}