package com.tarunguptaraja.realtimeleaderboard

import android.app.Application
import com.tarunguptaraja.realtimeleaderboard.di.appModule
import com.tarunguptaraja.realtimeleaderboard.domain.di.domainModule
import com.tarunguptaraja.realtimeleaderboard.engine.di.engineModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class LeaderboardApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@LeaderboardApplication)
            modules(
                engineModule,
                domainModule,
                appModule
            )
        }
    }
}
