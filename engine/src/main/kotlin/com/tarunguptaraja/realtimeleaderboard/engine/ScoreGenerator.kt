package com.tarunguptaraja.realtimeleaderboard.engine

import com.tarunguptaraja.realtimeleaderboard.engine.model.Player
import com.tarunguptaraja.realtimeleaderboard.engine.model.ScoreEvent
import kotlinx.coroutines.flow.Flow

interface ScoreGenerator {
    /**
     * Exposes a continuous stream of score events.
     */
    fun scoreEvents(): Flow<ScoreEvent>

    /**
     * Returns the list of active players in this session.
     */
    fun getPlayers(): List<Player>
}
