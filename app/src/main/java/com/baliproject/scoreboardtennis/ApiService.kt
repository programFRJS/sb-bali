package com.baliproject.scoreboardtennis

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

data class ResponseData(val status: String)

interface ApiService {
    @FormUrlEncoded
    @POST("/update_scores")
    fun updateScores(
        @Field("scoreA") scoreA: Int,
        @Field("scoreB") scoreB: Int,
        @Field("reset") reset: Int
    ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/update_service")
    fun updateService(
        @Field("serviceA") serviceA: Int,
        @Field("serviceB") serviceB: Int,
        @Field("advantageA") advantageA: Int,
        @Field("advantageB") advantageB: Int,
        @Field("reset") reset: Int
    ): Call<ResponseData>

//    @FormUrlEncoded
//    @POST("/update_advantage")
//    fun updateAdvantage(
//        @Field("advantageA") advantageA: Int,
//        @Field("advantageB") advantageB: Int
//    ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/enter_player")
    fun enterPlayer(
        @Field("playerA1") playerA1: String,
        @Field("playerA2") playerA2: String,
        @Field("playerB1") playerB1: String,
        @Field("playerB2") playerB2: String
    ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/update_brightness")
    fun updateBrigthness(
        @Field("persen") persen: Int,
    ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/reset")
    fun reset(
        @Field("scoreA") scoreA: Int,
        @Field("scoreB") scoreB: Int,
        @Field("Set") Set: Int,
        @Field("set1A") set1A: Int,
        @Field("set1B") set1B: Int,
        @Field("set2A") set2A: Int,
        @Field("set2B") set2B: Int,
        @Field("set3A") set3A: Int,
        @Field("set3B") set3B: Int,
        @Field("serviceA") serviceA: Int,
        @Field("serviceB") serviceB: Int,
        @Field("advantageA") advantageA: Int,
        @Field("advantageB") advantageB: Int,
        @Field("reset") reset: Int,
    ): Call<ResponseData>

    @FormUrlEncoded
    @POST("/update_set")
    fun updateSet(
        @Field("Set") Set: Int,
        @Field("set1A") set1A: Int,
        @Field("set1B") set1B: Int,
        @Field("set2A") set2A: Int,
        @Field("set2B") set2B: Int,
        @Field("set3A") set3A: Int,
        @Field("set3B") set3B: Int,
        @Field("reset") reset: Int
    ): Call<ResponseData>
}
