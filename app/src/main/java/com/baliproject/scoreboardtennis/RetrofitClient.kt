package com.baliproject.scoreboardtennis

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.55/"
    private const val BASE_URL_2 = "http://192.168.4.3/"

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

//    val retrofit2: Retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL_2)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
}
