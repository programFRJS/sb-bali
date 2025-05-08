package com.baliproject.scoreboardtennis.API.CreateGame

data class CreateGameResponse(
    val success: Boolean,
    val data: GameData,
    val message: String
)

data class GameData(
    val slug: String,
    val name: String,
    val court: String,
    val date: String
)

