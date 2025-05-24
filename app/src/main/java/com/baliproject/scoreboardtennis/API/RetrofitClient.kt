package com.baliproject.scoreboardtennis.API

import android.content.Context
import com.baliproject.scoreboardtennis.BuildConfig
import com.baliproject.scoreboardtennis.Database.AppDatabase
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val DEFAULT_BASE_URL = BuildConfig.SCOREBOARD_BASE_URL_API
    private const val TOKEN = BuildConfig.SCOREBOARD_TOKEN_API  // ganti sesuai token kamu

    private var retrofitInstance: Retrofit? = null

    // Interceptor untuk menambahkan header Authorization
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .addHeader("Authorization", "Bearer $TOKEN")
            .build()
        chain.proceed(newRequest)
    }

    val retrofit: Retrofit
        get() = retrofitInstance ?: Retrofit.Builder()
            .baseUrl(DEFAULT_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()

    suspend fun init(context: Context) {
        val db = AppDatabase.getDatabase(context)
        val ipEntity = db.ipAddressDao().getIpAddress()

        val ip = ipEntity?.ipAddress ?: "192.168.100.55"
        val baseUrl = "http://$ip/"

        val gson = GsonBuilder()
            .setLenient()
            .create()

        retrofitInstance = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(authInterceptor)
                    .build()
            )
            .build()
    }
}


