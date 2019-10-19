package com.andreasgift.myclock.Clock

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "clock_table_data")
data class Clock(
    val timeZone: String? = null,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)
