package com.tarunguptaraja.realtimeleaderboard.ui.screen.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column


import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarunguptaraja.realtimeleaderboard.ui.theme.Gold
import com.tarunguptaraja.realtimeleaderboard.ui.theme.RankUp
import kotlinx.coroutines.delay

@Composable
fun ScoreText(
    score: Long,
    lastScoreChange: Int,
    modifier: Modifier = Modifier
) {
    var showIncrement by remember { mutableStateOf(false) }
    var displayedIncrement by remember { mutableStateOf(0) }

    LaunchedEffect(score) {
        if (lastScoreChange > 0) {
            displayedIncrement = lastScoreChange
            showIncrement = true
            delay(1500)
            showIncrement = false
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = String.format("%,d", score),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 20.sp
                )
            )
            Text(
                text = "points",
                style = MaterialTheme.typography.labelSmall.copy(
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        AnimatedVisibility(
            visible = showIncrement,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { -it / 2 }
        ) {
            Text(
                text = "▲+$displayedIncrement",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = RankUp,
                    fontSize = 12.sp
                )
            )
        }
    }
}
