package com.andreasgift.myclock.ClockData

import androidx.lifecycle.LiveData
import com.andreasgift.myclock.Clock.Clock

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class ClockRepository(private val clockDAO: ClockDAO) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allClockData: LiveData<List<Clock>> = clockDAO.getAllClock()

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

}