package com.tarunguptaraja.realtimeleaderboard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.tarunguptaraja.realtimeleaderboard.ui.screen.LeaderboardScreen
import com.tarunguptaraja.realtimeleaderboard.ui.theme.RealTimeLeaderboardTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RealTimeLeaderboardTheme {
                LeaderboardScreen(viewModel = koinViewModel())
            }
        }
    }
}