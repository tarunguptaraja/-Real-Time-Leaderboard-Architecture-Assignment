package com.tarunguptaraja.realtimeleaderboard.domain.ranking

import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry
import com.tarunguptaraja.realtimeleaderboard.engine.model.ScoreEvent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class DefaultRankingEngine : RankingEngine {

    private val mutex = Mutex()
    
    private val playerScores = mutableMapOf<String, Long>()
    private val playerNames = mutableMapOf<String, String>()
    private val previousRanks = mutableMapOf<String, Int>()
    private val lastScoreChanges = mutableMapOf<String, Int>()

    private val _leaderboard = MutableStateFlow<List<LeaderboardEntry>>(emptyList())
    override fun observeLeaderboard(): StateFlow<List<LeaderboardEntry>> = _leaderboard.asStateFlow()

    override suspend fun processScoreEvent(event: ScoreEvent) = mutex.withLock {
        if (event.scoreIncrement <= 0) return

        val playerId = event.playerId
        playerNames[playerId] = event.playerName
        
        val currentScore = playerScores[playerId] ?: 0L
        playerScores[playerId] = currentScore + event.scoreIncrement

        lastScoreChanges.clear()
        lastScoreChanges[playerId] = event.scoreIncrement

        val updatedList = computeLeaderboard()
        _leaderboard.value = updatedList
    }

    override fun reset() {
        playerScores.clear()
        playerNames.clear()
        previousRanks.clear()
        lastScoreChanges.clear()
        _leaderboard.value = emptyList()
    }

    private fun computeLeaderboard(): List<LeaderboardEntry> {
        val sortedPlayers = playerScores.entries
            .sortedWith(
                compareByDescending<Map.Entry<String, Long>> { it.value }
                    .thenBy { playerNames[it.key] ?: "" }
            )

        val tempRanks = mutableMapOf<String, Int>()
        var currentRank = 1
        var skippedRankCount = 0
        var lastScore: Long? = null

        for (entry in sortedPlayers) {
            val score = entry.value
            val playerId = entry.key

            if (lastScore != null && score < lastScore) {
                currentRank += skippedRankCount
                skippedRankCount = 1
            } else {
                skippedRankCount++
            }
            lastScore = score
            tempRanks[playerId] = currentRank
        }

        val entries = sortedPlayers.map { entry ->
            val playerId = entry.key
            val score = entry.value
            val name = playerNames[playerId] ?: ""
            val rank = tempRanks[playerId] ?: 1
            val prevRank = previousRanks[playerId] ?: rank
            val scoreChange = lastScoreChanges[playerId] ?: 0

            LeaderboardEntry(
                playerId = playerId,
                playerName = name,
                score = score,
                rank = rank,
                previousRank = prevRank,
                lastScoreChange = scoreChange
            )
        }

        previousRanks.clear()
        previousRanks.putAll(tempRanks)

        return entries
    }
}
