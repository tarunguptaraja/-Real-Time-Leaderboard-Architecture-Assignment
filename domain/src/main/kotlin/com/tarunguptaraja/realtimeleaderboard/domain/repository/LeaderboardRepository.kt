package com.tarunguptaraja.realtimeleaderboard.domain.repository

import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry
import com.tarunguptaraja.realtimeleaderboard.domain.ranking.RankingEngine
import com.tarunguptaraja.realtimeleaderboard.engine.ScoreGenerator
import kotlinx.coroutines.flow.StateFlow

class LeaderboardRepository(
    private val scoreGenerator: ScoreGenerator,
    private val rankingEngine: RankingEngine
) {
    fun observeLeaderboard(): StateFlow<List<LeaderboardEntry>> =
        rankingEngine.observeLeaderboard()

    suspend fun startProcessing() {
        scoreGenerator.scoreEvents().collect { event ->
            rankingEngine.processScoreEvent(event)
        }
    }
}
