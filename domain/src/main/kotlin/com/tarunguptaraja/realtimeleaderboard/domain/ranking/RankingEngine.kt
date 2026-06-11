package com.tarunguptaraja.realtimeleaderboard.domain.ranking

import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry
import com.tarunguptaraja.realtimeleaderboard.engine.model.ScoreEvent
import kotlinx.coroutines.flow.StateFlow

interface RankingEngine {
    /**
     * Exposes the current leaderboard entries sorted by score (descending).
     */
    fun observeLeaderboard(): StateFlow<List<LeaderboardEntry>>

    /**
     * Processes a new score event, recomputing the leaderboard state.
     */
    suspend fun processScoreEvent(event: ScoreEvent)

    /**
     * Resets the leaderboard state to empty.
     */
    fun reset()
}
