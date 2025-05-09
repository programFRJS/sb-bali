package com.baliproject.scoreboardtennis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.baliproject.scoreboardtennis.Entity.WifiEntity
import com.baliproject.scoreboardtennis.Repository.IpAddressRepository
import com.baliproject.scoreboardtennis.Repository.WifiInformationRepository
import kotlinx.coroutines.Dispatchers

class WifiViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: WifiInformationRepository

    init {
        val dao = AppDatabase.getDatabase(application).wifiDao()
        repository = WifiInformationRepository(dao)
    }

    suspend fun saveWifiBlocking(ssid: String, password: String) {
        repository.insertBlocking(ssid, password)
    }
}