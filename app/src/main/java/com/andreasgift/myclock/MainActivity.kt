package com.andreasgift.myclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.Alarm.AlarmFragment
import com.andreasgift.myclock.Clock.ClockFragment
import com.andreasgift.myclock.Stopwatch.StopwatchFragment
import com.andreasgift.myclock.Timer.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var mainFragment: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return
            }
            mainFragment = ClockFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, mainFragment)
                .commit()
        }

        navigation.setOnNavigationItemSelectedListener(navigationListener)
    }

    val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_clock -> {
                mainFragment = ClockFragment()
            }
            R.id.navigation_alarm -> {
                mainFragment = AlarmFragment()
            }
            R.id.navigation_stopwatch -> {
                mainFragment = StopwatchFragment()
            }
            R.id.navigation_timer -> {
                mainFragment = TimerFragment()
            }
            else -> false
        }
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, mainFragment)
            .commit()
        true
    }
}
