package com.andreasgift.myclock.ClockData

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.andreasgift.myclock.Clock.Clock
import kotlinx.coroutines.launch

// Class extends AndroidViewModel and requires application as a parameter.
class ClockViewModel(application: Application) : AndroidViewModel(application) {

    // The ViewModel maintains a reference to the repository to get data.
    private val clockRepo: ClockRepository

    // LiveData gives us updated data when they change.
    val allClock: LiveData<List<Clock>>

    init {
        val clockDao = ClockROOMDatabase.getDatabase(application).clockDao()
        clockRepo = ClockRepository(clockDao)
        allClock = clockRepo.allClockData
    }

    // The implementation of insert() is completely hidden from the UI.
    // We don't want insert to block the main thread, so we're launching a new
    // coroutine. ViewModels have a coroutine scope based on their lifecycle called
    // viewModelScope which we can use here.
    fun insertClock(clock: Clock) = viewModelScope.launch {
        clockRepo.insertClock(clock)
    }

    fun deleteClock(clock: Clock) = viewModelScope.launch {
        clockRepo.deleteClock(clock)
    }

    fun deleteAll() = viewModelScope.launch {
        clockRepo.deleteAll()
    }
}