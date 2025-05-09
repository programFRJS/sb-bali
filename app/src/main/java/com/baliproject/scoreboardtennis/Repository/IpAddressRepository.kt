package com.baliproject.scoreboardtennis.Repository

import com.baliproject.scoreboardtennis.Dao.IpAddressDao
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity
import com.baliproject.scoreboardtennis.Entity.WifiEntity

class IpAddressRepository(private val dao: IpAddressDao) {
    suspend fun insertBlocking(ip: String) {
        val ipEntity = IpAddressEntity(ipAddress = ip)
        dao.insertIpAddress(ipEntity)
    }

}
