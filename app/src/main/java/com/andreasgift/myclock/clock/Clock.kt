package com.andreasgift.myclock.clock

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clock_table_data")
data class Clock(
    val timeZone: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
