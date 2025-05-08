package com.baliproject.scoreboardtennis.API.UpdateSetScore

import com.google.gson.annotations.SerializedName

data class UpdateSetScoreResponse(
    val success: Boolean,
    val data: SetScoreData,
    val message: String
)

data class SetScoreData(
    val player: String,
    @SerializedName("set_number")
    val setNumber: String,
    val score: String
)

