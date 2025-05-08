package com.baliproject.scoreboardtennis.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientWifi {
    private var retrofit: Retrofit? = null

    fun getClient(baseUrl: String): WifiApiService {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!.create(WifiApiService::class.java)
    }
}
