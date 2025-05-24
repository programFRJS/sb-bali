package com.baliproject.scoreboardtennis.API

import SetPlayerRequest
import SetPlayerResponse
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameRequest
import com.baliproject.scoreboardtennis.API.CreateGame.CreateGameResponse
import com.baliproject.scoreboardtennis.API.MatchStatus.UpdateStatusResponse
import com.baliproject.scoreboardtennis.API.ResetGame.ResetGameResponse
import com.baliproject.scoreboardtennis.API.ResetGame.ResetStatusMatchResponse
import com.baliproject.scoreboardtennis.API.Score.SetScoreResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResetResponse
import com.baliproject.scoreboardtennis.API.SetAdvantage.SetAdvantageResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResetResponse
import com.baliproject.scoreboardtennis.API.SetService.SetServiceResponse
import com.baliproject.scoreboardtennis.API.UpdateSetScore.UpdateSetScoreResponse


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Path

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

    @POST("game/create")
    fun createGame(@Body request: CreateGameRequest): Call<CreateGameResponse>

    @POST("game/set-name/{slug}")
    fun setPlayerNames(
        @Path("slug") slug: String,
        @Body body: SetPlayerRequest
    ): Call<SetPlayerResponse>

    @POST("game/set-score/{slug}/{player}/{score}")
    fun setScore(
        @Path("slug") slug: String,
        @Path("player") player: String,
        @Path("score") score: Int
    ): Call<SetScoreResponse>

    @POST("game/set-service/{slug}/{player}")
    fun setService(
        @Path("slug") slug: String,
        @Path("player") player: String
    ): Call<SetServiceResponse>

    @POST("game/reset-service/{slug}")
    fun resetService(
        @Path("slug") slug: String
    ): Call<SetServiceResetResponse>

    @POST("game/set-advantage/{slug}/{player}")
    fun setAdvantage(
        @Path("slug") slug: String,
        @Path("player") player: String
    ): Call<SetAdvantageResponse>

    @POST("game/reset-advantage/{slug}")
    fun resetAdvantage(
        @Path("slug") slug: String
    ): Call<SetAdvantageResetResponse>

    @POST("game/set/{slug}/{player}/{setNumber}/{score}")
    fun updateSetScore(
        @Path("slug") slug: String,
        @Path("player") player: String,
        @Path("setNumber") setNumber: Int,
        @Path("score") score: Int
    ): Call<UpdateSetScoreResponse>

    @POST("game/reset/{slug}")
    fun resetGame(
        @Path("slug") slug: String
    ): Call<ResetGameResponse>

    @POST("game/set-status/{slug}/{status}")
    fun updateMatchStatus(
        @Path("slug") slug: String,
        @Path("status") status: String
    ): Call<UpdateStatusResponse>

    @POST("game/reset/{slug}")
    fun resetMatchStatus(
        @Path("slug") slug: String
    ): Call<ResetStatusMatchResponse>
}
