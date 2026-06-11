package com.tarunguptaraja.realtimeleaderboard.di

import com.tarunguptaraja.realtimeleaderboard.ui.viewmodel.LeaderboardViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { LeaderboardViewModel(get()) }
}
