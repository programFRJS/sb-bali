package com.baliproject.scoreboardtennis.Dao

import androidx.room.*
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity

@Dao
interface IpAddressDao {

    @Query("SELECT * FROM ip_address WHERE id = 0")
    suspend fun getIpAddress(): IpAddressEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIpAddress(ip: IpAddressEntity)
}
