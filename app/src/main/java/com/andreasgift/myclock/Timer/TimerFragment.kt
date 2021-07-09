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
import com.andreasgift.myclock.databinding.FragmentTimerBinding


/**
 * A simple [Fragment] subclass.
 */
class TimerFragment : Fragment() {
    private var _binding: FragmentTimerBinding? = null
    private val binding get() = _binding!!

    var countDownTimer: CountDownTimer? = null
    lateinit var soundUri: Uri
    lateinit var ringtone: Ringtone

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTimerBinding.inflate(layoutInflater, container, false)
        val mview = binding.root
        soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        ringtone = RingtoneManager.getRingtone(this.activity, soundUri)
        ringtone.isLooping = true

        binding.hourPicker.minValue = 0
        binding.hourPicker.maxValue = 3

        binding.minutesPicker.minValue = 0
        binding.minutesPicker.maxValue = 60

        binding.secondPicker.minValue = 0
        binding.secondPicker.maxValue = 60

        binding.startButton.setOnClickListener(startListener)
        binding.stopButton.setOnClickListener(resetListener)

        return mview
    }

    private val startListener = View.OnClickListener {
        val milisecondsTime = binding.secondPicker.value * 1000L +
                binding.minutesPicker.value * 60 * 1000L +
                binding.hourPicker.value * 60 * 60 * 1000L
        countDownTimer = countdownTimer(milisecondsTime)
        countDownTimer?.start()
    }

    private val resetListener = View.OnClickListener {
        countDownTimer?.cancel()
    }

    private fun countdownTimer(milisSecond: Long): CountDownTimer {
        val timer = object : CountDownTimer(milisSecond, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val updateTime = millisUntilFinished.toInt() / 1000

                val hour = updateTime / 3600
                binding.hourPicker.value = hour

                val minutes = (updateTime % 3600) / 60
                binding.minutesPicker.value = minutes

                val seconds = updateTime % 60
                binding.secondPicker.value = seconds
            }

            override fun onFinish() {
                timerForRingtone().start()
                binding.hourPicker.value = 0
                binding.minutesPicker.value = 0
                binding.secondPicker.value = 0
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
        countDownTimer?.cancel()
        binding.hourPicker.value = 0
        binding.minutesPicker.value = 0
        binding.secondPicker.value = 0
        super.onDestroy()
    }
}
