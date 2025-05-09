package com.baliproject.scoreboardtennis.Repository

import com.baliproject.scoreboardtennis.Dao.WifiDao
import com.baliproject.scoreboardtennis.Entity.WifiEntity

class WifiInformationRepository(private val dao: WifiDao) {
    suspend fun insertBlocking(ssid: String, password: String) {
        val wifiEntity = WifiEntity(ssid = ssid, password = password)
        dao.insertWifi(wifiEntity)
    }
}

