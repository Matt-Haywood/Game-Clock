package com.example.gameclock.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "alarm_table")
data class Alarm(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var minute: String = "00",
    var hour: String = "00",
    var title: String = "",
    var isEnabled: Boolean = false,

)
