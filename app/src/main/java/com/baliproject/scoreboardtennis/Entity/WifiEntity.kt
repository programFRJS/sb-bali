package com.baliproject.scoreboardtennis.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "wifi")
data class WifiEntity(
    @PrimaryKey val id: Int = 0,
    val ssid: String,
    val password: String
)

