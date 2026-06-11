package com.tarunguptaraja.realtimeleaderboard.engine.model

data class ScoreEvent(
    val playerId: String,
    val playerName: String,
    val scoreIncrement: Int,
    val timestamp: Long
)
