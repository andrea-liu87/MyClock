package com.andreasgift.myclock.Alarm

import androidx.room.*

@Entity(tableName = "alarm_data_table")
data class Alarm(

    @ColumnInfo(name = "alarm_hour")
    var alarmHour: Int,

    @ColumnInfo(name = "alarm_min")
    var alarmMin: Int,

    @ColumnInfo(name = "is_am")
    var isAM: Boolean,

    var days: ArrayList<Int>?,

    @ColumnInfo(name = "is_on")
    var isOn: Boolean,

    var label: String,

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)