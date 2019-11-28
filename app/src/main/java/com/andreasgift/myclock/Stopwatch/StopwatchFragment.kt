package com.andreasgift.myclock.Stopwatch


import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.R
import kotlinx.android.synthetic.main.fragment_stopwatch.*
import kotlinx.android.synthetic.main.fragment_stopwatch.view.*

class StopwatchFragment : Fragment() {
    private var milisecondTime: Long = 0
    private var startTime: Long = 0
    private var updateTime: Long = 0
    private var timeBuff: Long = 0
    private var seconds: Int = 0
    private var minutes: Int = 0
    private var millisecond: Int = 0

    lateinit var handler: Handler
    private lateinit var runnable: Runnable

    private val startButtonListener = View.OnClickListener {
        startTime = SystemClock.uptimeMillis()
        handler.postDelayed(runnable, 0)
        stop_button.isEnabled = true
        start_button.isEnabled = false
    }

    private val stopButtonListener = View.OnClickListener {
        handler.removeCallbacks(runnable)
        timeBuff += milisecondTime
        stop_button.isEnabled = false
        start_button.isEnabled = false
    }

    private val resetButtonListener = View.OnClickListener {
        milisecondTime = 0L
        startTime = 0L
        timeBuff = 0L
        updateTime = 0L
        millisecond = 0
        minutes = 0

        start_button.isEnabled = true
        stopwatch_tv.text = "00:00:000"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        runnable = object : Runnable {
            override fun run() {

                milisecondTime = SystemClock.uptimeMillis() - startTime

                updateTime = timeBuff + milisecondTime

                seconds = (updateTime / 1000).toInt()

                minutes = seconds / 60

                seconds = seconds % 60

                millisecond = (updateTime % 1000).toInt()

                stopwatch_tv.text = String.format(
                    "%02d:%02d:%03d",
                    minutes,
                    seconds,
                    millisecond
                )

                handler.postDelayed(runnable, 0)
            }
        }

        val view = inflater.inflate(R.layout.fragment_stopwatch, container, false)
        view.start_button.setOnClickListener(startButtonListener)
        view.stop_button.setOnClickListener(stopButtonListener)
        view.reset_button.setOnClickListener(resetButtonListener)

        handler = Handler()
        return view
    }
}
