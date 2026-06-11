package com.tarunguptaraja.realtimeleaderboard.domain.model

data class LeaderboardEntry(
    val playerId: String,
    val playerName: String,
    val score: Long,
    val rank: Int,
    val previousRank: Int,     // Track rank movement
    val lastScoreChange: Int   // Track recent score increment for highlight animation
)
