package com.baliproject.scoreboardtennis.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baliproject.scoreboardtennis.Entity.MatchEntity

@Dao
interface MatchDao {

    @Query("SELECT * FROM match_data WHERE id = 0")
    suspend fun getMatch(): MatchEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMatch(match: MatchEntity)
}

