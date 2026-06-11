package com.tarunguptaraja.realtimeleaderboard.ui.state

import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry

data class LeaderboardUiState(
    val entries: List<LeaderboardEntry> = emptyList(),
    val isRunning: Boolean = false,
    val playerCount: Int = 0
)
