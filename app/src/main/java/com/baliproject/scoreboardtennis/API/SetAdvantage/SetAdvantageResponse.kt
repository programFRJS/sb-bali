package com.baliproject.scoreboardtennis.API.SetAdvantage

data class SetAdvantageResponse(
    val success: Boolean,
    val data: ServiceData,
    val message: String
)

data class ServiceData(
    val player: String
)

data class SetAdvantageResetResponse(
    val success: Boolean,
    val data: Any?,
    val message: String
)
