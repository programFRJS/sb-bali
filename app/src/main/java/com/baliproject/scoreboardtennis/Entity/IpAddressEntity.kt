package com.baliproject.scoreboardtennis.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ip_address")
data class IpAddressEntity(
    @PrimaryKey val id: Int = 0,
    val ipAddress: String
)

