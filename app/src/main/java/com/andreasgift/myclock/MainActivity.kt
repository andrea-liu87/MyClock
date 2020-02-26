package com.andreasgift.myclock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.About.AboutDialog
import com.andreasgift.myclock.Alarm.AlarmFragment
import com.andreasgift.myclock.Clock.ClockFragment
import com.andreasgift.myclock.Stopwatch.StopwatchFragment
import com.andreasgift.myclock.Timer.TimerFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            setFragment(ClockFragment())
        }
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setOnMenuItemClickListener(toolBarListener)
        navigation.setOnNavigationItemSelectedListener(navigationListener)
    }

    private fun setFragment(fragment: Fragment) {
        if (fragment_container == null) {
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, fragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                .commit()
        }
    }

    val toolBarListener = Toolbar.OnMenuItemClickListener {
        when (it.itemId) {
            R.id.about -> {
                AboutDialog().show(supportFragmentManager, "about_dialog")
            }
        }
        true
    }

    val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.navigation_clock -> {
                setFragment(ClockFragment())
            }
            R.id.navigation_alarm -> {
                setFragment(AlarmFragment())
            }
            R.id.navigation_stopwatch -> {
                setFragment(StopwatchFragment())
            }
            R.id.navigation_timer -> {
                setFragment(TimerFragment())
            }
        }
        true
    }
}
