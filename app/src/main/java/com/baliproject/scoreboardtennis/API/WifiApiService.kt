package com.baliproject.scoreboardtennis.API

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface WifiApiService {
    @FormUrlEncoded
    @POST("wifi-settings")
    suspend fun sendWifiCredentials(
        @Field("ssid") ssid: String,
        @Field("password") password: String
    ): Response<ResponseBody>
}
