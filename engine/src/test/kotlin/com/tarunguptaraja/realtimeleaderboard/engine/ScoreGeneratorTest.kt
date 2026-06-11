package com.tarunguptaraja.realtimeleaderboard.engine

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ScoreGeneratorTest {

    @Test
    fun testGetPlayers_returnsCorrectCount() {
        val generator = DefaultScoreGenerator()
        val players = generator.getPlayers()
        assertThat(players).hasSize(20)
        assertThat(players.first().name).isEqualTo("Tarun")
    }

    @Test
    fun testScoreEvents_areDeterministicWithSameSeed() = runTest {
        val generator1 = DefaultScoreGenerator(seed = 12345L, minDelayMs = 0, maxDelayMs = 0)
        val generator2 = DefaultScoreGenerator(seed = 12345L, minDelayMs = 0, maxDelayMs = 0)

        val events1 = mutableListOf<String>()
        val events2 = mutableListOf<String>()

        generator1.scoreEvents().take(5).collect { events1.add(it.playerId) }
        generator2.scoreEvents().take(5).collect { events2.add(it.playerId) }

        assertThat(events1).isEqualTo(events2)
    }

    @Test
    fun testScoreEvents_areNotSameWithDifferentSeed() = runTest {
        val generator1 = DefaultScoreGenerator(seed = 11111L, minDelayMs = 0, maxDelayMs = 0)
        val generator2 = DefaultScoreGenerator(seed = 22222L, minDelayMs = 0, maxDelayMs = 0)

        val events1 = mutableListOf<String>()
        val events2 = mutableListOf<String>()

        generator1.scoreEvents().take(10).collect { events1.add(it.playerId) }
        generator2.scoreEvents().take(10).collect { events2.add(it.playerId) }

        // While collision is technically possible, it's highly improbable to match all 10
        assertThat(events1).isNotEqualTo(events2)
    }

    @Test
    fun testScoreEvents_allIncrementsArePositive() = runTest {
        val generator = DefaultScoreGenerator(seed = 999L, minDelayMs = 0, maxDelayMs = 0)
        
        generator.scoreEvents().take(20).test {
            for (i in 0 until 20) {
                val item = awaitItem()
                assertThat(item.scoreIncrement).isGreaterThan(0)
                assertThat(item.scoreIncrement).isAtMost(50)
            }
            awaitComplete()
        }
    }

    @Test
    fun testScoreEvents_referenceValidPlayers() = runTest {
        val generator = DefaultScoreGenerator(seed = 456L, minDelayMs = 0, maxDelayMs = 0)
        val validPlayerIds = generator.getPlayers().map { it.id }.toSet()

        generator.scoreEvents().take(10).test {
            for (i in 0 until 10) {
                val item = awaitItem()
                assertThat(validPlayerIds).contains(item.playerId)
            }
            awaitComplete()
        }
    }
}
