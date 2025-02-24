package com.baliproject.scoreboardtennis

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

data class ResponseData(val status: String)

interface ApiService {
    @FormUrlEncoded
    @POST("/update_scores")
    fun updateScores(
        @Field("scoreA") scoreA: Int,
        @Field("scoreB") scoreB: Int
    ): Call<ResponseData>
}
