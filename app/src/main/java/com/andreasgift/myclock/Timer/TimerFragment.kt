package com.andreasgift.myclock.Timer


import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_timer.view.*


/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {
    lateinit var mview: View
    lateinit var countDownTimer: CountDownTimer
    lateinit var soundUri: Uri
    lateinit var ringtone: Ringtone

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mview = inflater.inflate(com.andreasgift.myclock.R.layout.fragment_timer, container, false)
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this.activity, soundUri)
        ringtone.isLooping = true

        mview.hour_picker.minValue = 0
        mview.hour_picker.maxValue = 3

        mview.minutes_picker.minValue = 0
        mview.minutes_picker.maxValue = 60

        mview.second_picker.minValue = 0
        mview.second_picker.maxValue = 60

        mview.start_button.setOnClickListener(startListener)
        mview.stop_button.setOnClickListener(resetListener)

        return mview
    }

    private val startListener = View.OnClickListener {
        val milisecondsTime = mview.second_picker.value * 1000L +
                mview.minutes_picker.value * 60 * 1000L +
                mview.hour_picker.value * 60 * 60 * 1000L
        countDownTimer = countdownTimer(milisecondsTime)
        countDownTimer.start()
    }

    private val resetListener = View.OnClickListener {
        countDownTimer.cancel()
    }

    private fun countdownTimer(milisSecond: Long): CountDownTimer {
        val timer = object : CountDownTimer(milisSecond, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val updateTime = millisUntilFinished.toInt() / 1000

                val hour = updateTime / 3600
                mview.hour_picker.value = hour

                val minutes = (updateTime % 3600) / 60
                mview.minutes_picker.value = minutes

                val seconds = updateTime % 60
                mview.second_picker.value = seconds
            }

            override fun onFinish() {
                timerForRingtone().start()
                mview.hour_picker.value = 0
                mview.minutes_picker.value = 0
                mview.second_picker.value = 0
            }
        }
        return timer
    }

    private fun timerForRingtone(): CountDownTimer {
        val timer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                ringtone.stop()
            }

            override fun onTick(millisUntilFinished: Long) {
                ringtone.play()
            }
        }
        return timer
    }

    override fun onDestroy() {
        countDownTimer.cancel()
        mview.hour_picker.value = 0
        mview.minutes_picker.value = 0
        mview.second_picker.value = 0
        super.onDestroy()
    }
}
