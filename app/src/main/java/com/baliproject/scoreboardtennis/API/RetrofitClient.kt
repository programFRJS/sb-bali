package com.baliproject.scoreboardtennis.API

import android.content.Context
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DEFAULT_BASE_URL = "http://192.168.100.55/"
    private var retrofitInstance: Retrofit? = null

    // Versi sinkron untuk akses biasa (gunakan setelah init di Application/Activity)
    val retrofit: Retrofit
        get() = retrofitInstance ?: Retrofit.Builder()
            .baseUrl(DEFAULT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    // Fungsi suspend untuk inisialisasi dari Room
    suspend fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val ipEntity = db.ipAddressDao().getIpAddress() // suspend function

        val ip = ipEntity?.ipAddress ?: "192.168.100.55"
        val baseUrl = "http://$ip/"

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson)) // pakai gson custom
            .build()



    }
}

