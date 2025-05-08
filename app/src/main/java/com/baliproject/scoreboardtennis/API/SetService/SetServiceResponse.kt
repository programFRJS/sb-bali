package com.baliproject.scoreboardtennis.API.SetService

data class SetServiceResponse(
    val success: Boolean,
    val data: ServiceData,
    val message: String
)

data class ServiceData(
    val player: String
)

data class SetServiceResetResponse(
    val success: Boolean,
    val data: Any?,
    val message: String
)


