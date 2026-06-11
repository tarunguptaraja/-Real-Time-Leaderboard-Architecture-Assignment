package com.tarunguptaraja.realtimeleaderboard.domain.di

import com.tarunguptaraja.realtimeleaderboard.domain.ranking.DefaultRankingEngine
import com.tarunguptaraja.realtimeleaderboard.domain.ranking.RankingEngine
import com.tarunguptaraja.realtimeleaderboard.domain.repository.LeaderboardRepository
import org.koin.dsl.module

val domainModule = module {
    single<RankingEngine> { DefaultRankingEngine() }
    single { LeaderboardRepository(get(), get()) }
}
