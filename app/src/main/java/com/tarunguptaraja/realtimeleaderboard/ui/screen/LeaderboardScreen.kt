package com.tarunguptaraja.realtimeleaderboard.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry
import com.tarunguptaraja.realtimeleaderboard.ui.screen.components.LeaderboardRow
import com.tarunguptaraja.realtimeleaderboard.ui.state.LeaderboardUiState
import com.tarunguptaraja.realtimeleaderboard.ui.theme.Gold
import com.tarunguptaraja.realtimeleaderboard.ui.theme.RealTimeLeaderboardTheme
import com.tarunguptaraja.realtimeleaderboard.ui.viewmodel.LeaderboardViewModel

@Composable
fun LeaderboardScreen(
    viewModel: LeaderboardViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LeaderboardScreenContent(uiState = uiState, modifier = modifier)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreenContent(
    uiState: LeaderboardUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Trophy Icon",
                            tint = Gold,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Live Leaderboard",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    fontSize = 22.sp
                                )
                            )
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF4CAF50))
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${uiState.playerCount} players • Live Updates",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = Color.White.copy(alpha = 0.8f),
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                itemsIndexed(
                    items = uiState.entries,
                    key = { _, entry -> entry.playerId }
                ) { index, entry ->
                    LeaderboardRow(
                        entry = entry,
                        isAlternating = index % 2 == 1,
                        modifier = Modifier.animateItem()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenPreview() {
    val sampleEntries = listOf(
        LeaderboardEntry("1", "AlphaWarrior", 2500L, 1, 1, 0),
        LeaderboardEntry("2", "BetaKnight", 2100L, 2, 2, 50),
        LeaderboardEntry("3", "GammaSage", 2100L, 2, 2, 0),
        LeaderboardEntry("4", "DeltaSniper", 1850L, 4, 3, 10),
        LeaderboardEntry("5", "EpsilonMage", 1500L, 5, 5, 0)
    )
    val uiState = LeaderboardUiState(
        entries = sampleEntries,
        isRunning = true,
        playerCount = sampleEntries.size
    )
    RealTimeLeaderboardTheme(darkTheme = false) {
        LeaderboardScreenContent(uiState = uiState)
    }
}

@Preview(showBackground = true)
@Composable
fun LeaderboardScreenDarkPreview() {
    val sampleEntries = listOf(
        LeaderboardEntry("1", "AlphaWarrior", 2500L, 1, 1, 0),
        LeaderboardEntry("2", "BetaKnight", 2100L, 2, 2, 50),
        LeaderboardEntry("3", "GammaSage", 2100L, 2, 2, 0),
        LeaderboardEntry("4", "DeltaSniper", 1850L, 4, 3, 10),
        LeaderboardEntry("5", "EpsilonMage", 1500L, 5, 5, 0)
    )
    val uiState = LeaderboardUiState(
        entries = sampleEntries,
        isRunning = true,
        playerCount = sampleEntries.size
    )
    RealTimeLeaderboardTheme(darkTheme = true) {
        LeaderboardScreenContent(uiState = uiState)
    }
}
