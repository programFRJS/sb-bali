package com.baliproject.scoreboardtennis.API.CreateGame

data class CreateGameRequest(
    val name: String,
    val court: String,
    val date: String
)

