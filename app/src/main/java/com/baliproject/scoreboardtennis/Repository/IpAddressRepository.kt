package com.baliproject.scoreboardtennis.Repository

import com.baliproject.scoreboardtennis.Dao.IpAddressDao
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity

class IpAddressRepository(private val dao: IpAddressDao) {
    suspend fun insert(ipAddress: IpAddressEntity) {
        dao.insertIpAddress(ipAddress)
    }
}
