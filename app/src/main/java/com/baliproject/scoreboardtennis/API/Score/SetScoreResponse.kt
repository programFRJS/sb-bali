package com.baliproject.scoreboardtennis.API.Score

data class SetScoreResponse(
    val success: Boolean,
    val data: ScoreData,
    val message: String
)

data class ScoreData(
    val player: String,
    val score: String
)

