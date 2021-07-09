package com.andreasgift.myclock.clockdata

import android.content.SharedPreferences
import com.andreasgift.myclock.Helper.Constants
import com.andreasgift.myclock.clock.Clock
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ClockRepository @Inject constructor(
    private val clockDAO: ClockDAO,
    private val sharedPref: SharedPreferences
) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allClockData: Flow<List<Clock>> = clockDAO.getAllClock()

    // The suspend modifier tells the compiler that this must be called from a
    // coroutine or another suspend function.
    suspend fun insertClock(clock: Clock) {
        clockDAO.insertClock(clock)
    }

    suspend fun deleteClock(clock: Clock) {
        clockDAO.deleteClock(clock)
    }

    suspend fun deleteAll() {
        clockDAO.deleteAll()
    }

    val isAnalog: Boolean = sharedPref.getBoolean(Constants.PREF_KEY_MANUAL_CLOCK, false)

}