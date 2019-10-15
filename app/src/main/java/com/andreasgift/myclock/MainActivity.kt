package com.andreasgift.myclock

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.andreasgift.myclock.Clock.ClockFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (findViewById<FrameLayout>(R.id.fragment_container) != null){
            if (savedInstanceState != null){
                return
            }
            val mainFragment : Fragment = ClockFragment()
            supportFragmentManager.beginTransaction().add(R.id.fragment_container, mainFragment).commit()
        }
    }
}
