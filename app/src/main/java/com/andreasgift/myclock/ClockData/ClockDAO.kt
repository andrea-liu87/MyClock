package com.andreasgift.myclock.ClockData

import androidx.lifecycle.LiveData
import androidx.room.*
import com.andreasgift.myclock.Clock.Clock

@Dao
interface ClockDAO {

    @Query("SELECT * FROM clock_table_data ORDER BY id ASC")
    fun getAllClock(): LiveData<List<Clock>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertClock(clock: Clock)

    @Delete
    suspend fun deleteClock(clock: Clock)

    @Query("DELETE FROM clock_table_data")
    suspend fun deleteAll()
}