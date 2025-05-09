package com.baliproject.scoreboardtennis.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.baliproject.scoreboardtennis.Entity.IpAddressEntity
import com.baliproject.scoreboardtennis.Repository.IpAddressRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class IpAddressViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: IpAddressRepository

    init {
        val dao = AppDatabase.getDatabase(application).ipAddressDao()
        repository = IpAddressRepository(dao)
    }

    suspend fun saveIpAddressBlocking(ip: String) {
        repository.insertBlocking(ip)
    }
}
