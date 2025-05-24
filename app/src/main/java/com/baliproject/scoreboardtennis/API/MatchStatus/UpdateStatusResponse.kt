package com.baliproject.scoreboardtennis.API.MatchStatus

data class UpdateStatusResponse(
    val success: Boolean,
    val data: StatusData,
    val message: String
)

data class StatusData(
    val status: String
)

