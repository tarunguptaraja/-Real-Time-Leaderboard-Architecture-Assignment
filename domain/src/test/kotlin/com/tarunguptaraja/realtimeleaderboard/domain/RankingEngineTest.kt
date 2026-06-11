package com.tarunguptaraja.realtimeleaderboard.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tarunguptaraja.realtimeleaderboard.domain.ranking.DefaultRankingEngine
import com.tarunguptaraja.realtimeleaderboard.engine.model.ScoreEvent
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RankingEngineTest {

    private lateinit var rankingEngine: DefaultRankingEngine

    @Before
    fun setUp() {
        rankingEngine = DefaultRankingEngine()
    }

    @Test
    fun testInitialLeaderboard_isEmpty() = runTest {
        rankingEngine.observeLeaderboard().test {
            val initial = awaitItem()
            assertThat(initial).isEmpty()
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun testProcessScoreEvent_singlePlayer() = runTest {
        rankingEngine.observeLeaderboard().test {
            var items = awaitItem()
            assertThat(items).isEmpty()

            rankingEngine.processScoreEvent(
                ScoreEvent("p1", "Alpha", 10, 1000L)
            )

            items = awaitItem()
            assertThat(items).hasSize(1)
            val entry = items.first()
            assertThat(entry.playerId).isEqualTo("p1")
            assertThat(entry.playerName).isEqualTo("Alpha")
            assertThat(entry.score).isEqualTo(10)
            assertThat(entry.rank).isEqualTo(1)
            assertThat(entry.previousRank).isEqualTo(1)
            assertThat(entry.lastScoreChange).isEqualTo(10)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun testProcessScoreEvent_denseRankingAndTies() = runTest {
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", 50, 1000L))
        rankingEngine.processScoreEvent(ScoreEvent("p2", "Beta", 50, 1001L))
        rankingEngine.processScoreEvent(ScoreEvent("p3", "Gamma", 30, 1002L))

        rankingEngine.observeLeaderboard().test {
            val items = awaitItem()
            assertThat(items).hasSize(3)

            val alpha = items.first { it.playerId == "p1" }
            val beta = items.first { it.playerId == "p2" }
            val gamma = items.first { it.playerId == "p3" }

            assertThat(alpha.rank).isEqualTo(1)
            assertThat(beta.rank).isEqualTo(1)
            assertThat(gamma.rank).isEqualTo(3)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun testScores_onlyIncrease() = runTest {
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", 10, 1000L))
        
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", 0, 1001L))
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", -5, 1002L))

        rankingEngine.observeLeaderboard().test {
            val items = awaitItem()
            val alpha = items.first { it.playerId == "p1" }
            assertThat(alpha.score).isEqualTo(10)
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun testRankMovementTracking() = runTest {
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", 20, 1000L))
        rankingEngine.processScoreEvent(ScoreEvent("p2", "Beta", 10, 1001L))

        rankingEngine.processScoreEvent(ScoreEvent("p2", "Beta", 20, 1002L))

        rankingEngine.observeLeaderboard().test {
            val items = awaitItem()
            val beta = items.first { it.playerId == "p2" }
            val alpha = items.first { it.playerId == "p1" }

            // Beta moved 2 -> 1
            assertThat(beta.rank).isEqualTo(1)
            assertThat(beta.previousRank).isEqualTo(2)

            // Alpha moved 1 -> 2
            assertThat(alpha.rank).isEqualTo(2)
            assertThat(alpha.previousRank).isEqualTo(1)

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun testLastScoreChangeHighlight_onlyForActivePlayer() = runTest {
        rankingEngine.processScoreEvent(ScoreEvent("p1", "Alpha", 20, 1000L))
        rankingEngine.processScoreEvent(ScoreEvent("p2", "Beta", 10, 1001L))

        rankingEngine.observeLeaderboard().test {
            val items = awaitItem()
            val alpha = items.first { it.playerId == "p1" }
            val beta = items.first { it.playerId == "p2" }

            assertThat(beta.lastScoreChange).isEqualTo(10)
            assertThat(alpha.lastScoreChange).isEqualTo(0)

            cancelAndConsumeRemainingEvents()
        }
    }
}
