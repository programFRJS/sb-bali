package com.baliproject.scoreboardtennis.Dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.baliproject.scoreboardtennis.Entity.WifiEntity

@Dao
interface WifiDao {

    @Query("SELECT * FROM wifi WHERE id = 0")
    suspend fun getWifiInformation(): WifiEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWifi(wifi: WifiEntity)
}
