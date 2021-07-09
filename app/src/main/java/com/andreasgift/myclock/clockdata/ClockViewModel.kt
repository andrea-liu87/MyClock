package com.andreasgift.myclock.clockdata

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.andreasgift.myclock.clock.Clock
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClockViewModel @Inject constructor(
    private val clockRepo: ClockRepository
) : ViewModel() {

    val allClock = clockRepo.allClockData.asLiveData()

    fun insertClock(clock: Clock) = viewModelScope.launch {
        clockRepo.insertClock(clock)
    }

    fun deleteClock(clock: Clock) = viewModelScope.launch {
        clockRepo.deleteClock(clock)
    }

    fun deleteAll() = viewModelScope.launch {
        clockRepo.deleteAll()
    }

    val isAnalog: Boolean = clockRepo.isAnalog
}