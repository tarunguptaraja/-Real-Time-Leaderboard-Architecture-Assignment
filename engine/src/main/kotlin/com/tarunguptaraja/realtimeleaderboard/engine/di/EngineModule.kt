package com.tarunguptaraja.realtimeleaderboard.engine.di

import com.tarunguptaraja.realtimeleaderboard.engine.DefaultScoreGenerator
import com.tarunguptaraja.realtimeleaderboard.engine.ScoreGenerator
import org.koin.dsl.module

val engineModule = module {
    single<ScoreGenerator> { DefaultScoreGenerator() }
}
