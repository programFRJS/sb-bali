package com.baliproject.scoreboardtennis.Entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "match_data")
data class MatchEntity(
    @PrimaryKey val id: Int = 0,

    val slug: String?,
    val playerA1: String?,
    val playerA2: String?,
    val playerB1: String?,
    val playerB2: String?,
    val eventName: String?,
    val court: String?,
    val date: String?,
    val scoreA: Int,
    val scoreB: Int,
    val setNumber: Int,
    val set1A: Int,
    val set1B: Int,
    val set2A: Int,
    val set2B: Int,
    val set3A: Int,
    val set3B: Int,
    val serviceA: Int,
    val serviceB: Int,
    val advantageA: Int,
    val advantageB: Int,
    val matchStatus: String?
)


