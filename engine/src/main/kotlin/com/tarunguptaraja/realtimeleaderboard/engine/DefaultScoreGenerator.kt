package com.tarunguptaraja.realtimeleaderboard.engine

import com.tarunguptaraja.realtimeleaderboard.engine.model.Player
import com.tarunguptaraja.realtimeleaderboard.engine.model.ScoreEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

class DefaultScoreGenerator(
    private val seed: Long = 42L,
    private val playerCount: Int = 20,
    private val minDelayMs: Long = 900L,
    private val maxDelayMs: Long = 2000L,
    private val minIncrement: Int = 1,
    private val maxIncrement: Int = 50,
    private val onDiagnosticLog: ((String) -> Unit)? = { println(it) }
) : ScoreGenerator {

    private val players: List<Player> = List(playerCount) { index ->
        val id = "player_${index + 1}"
        val name = when (index) {
            0 -> "Tarun"
            1 -> "Tanishka"
            2 -> "Aditya"
            3 -> "Arjun"
            4 -> "Tarun"
            5 -> "Sai"
            6 -> "Krishna"
            7 -> "Ishaan"
            8 -> "Shaurya"
            9 -> "Shreya"
            10 -> "Ananya"
            11 -> "Diya"
            12 -> "Raja"
            13 -> "Dev"
            14 -> "Atharv"
            15 -> "Parth"
            16 -> "Tanmay"
            17 -> "Yash"
            18 -> "Amit"
            19 -> "Rahul"
            else -> "Player_${index + 1}"
        }
        Player(id = id, name = name)
    }

    override fun getPlayers(): List<Player> = players

    override fun scoreEvents(): Flow<ScoreEvent> = flow {
        val random = Random(seed)

        while (true) {
            val randomPlayer = players[random.nextInt(players.size)]
            val scoreIncrement = random.nextInt(minIncrement, maxIncrement + 1)
            val timestamp = System.currentTimeMillis()

            val event = ScoreEvent(
                playerId = randomPlayer.id,
                playerName = randomPlayer.name,
                scoreIncrement = scoreIncrement,
                timestamp = timestamp
            )

            onDiagnosticLog?.invoke("[ScoreGen] Emitting: player=${event.playerName} (id=${event.playerId}) increment=+${event.scoreIncrement} ts=${event.timestamp}")
            emit(event)

            val delayMs = if (maxDelayMs > minDelayMs) {
                random.nextLong(minDelayMs, maxDelayMs + 1)
            } else {
                minDelayMs
            }
            delay(delayMs)
        }
    }
}
