package com.tarunguptaraja.realtimeleaderboard.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.tarunguptaraja.realtimeleaderboard.domain.ranking.DefaultRankingEngine
import com.tarunguptaraja.realtimeleaderboard.domain.repository.LeaderboardRepository
import com.tarunguptaraja.realtimeleaderboard.engine.DefaultScoreGenerator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LeaderboardRepositoryTest {

    @Test
    fun testIntegration_generatorToRepositoryToRankingEngine() = runTest {
        val generator = DefaultScoreGenerator(seed = 42L, minDelayMs = 10L, maxDelayMs = 10L)

        val rankingEngine = DefaultRankingEngine()
        val repository = LeaderboardRepository(generator, rankingEngine)

        repository.observeLeaderboard().test {
            val initial = awaitItem()
            assertThat(initial).isEmpty()

            backgroundScope.launch {
                repository.startProcessing()
            }


            val firstUpdate = awaitItem()
            assertThat(firstUpdate).isNotEmpty()
            
            val secondUpdate = awaitItem()
            assertThat(secondUpdate).isNotEmpty()

            cancelAndConsumeRemainingEvents()

        }
    }
}
