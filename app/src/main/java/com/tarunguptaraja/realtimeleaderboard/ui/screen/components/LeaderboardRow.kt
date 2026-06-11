package com.tarunguptaraja.realtimeleaderboard.ui.screen.components

import androidx.compose.animation.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.tarunguptaraja.realtimeleaderboard.domain.model.LeaderboardEntry
import com.tarunguptaraja.realtimeleaderboard.ui.theme.Bronze
import com.tarunguptaraja.realtimeleaderboard.ui.theme.Gold
import com.tarunguptaraja.realtimeleaderboard.ui.theme.RankDown
import com.tarunguptaraja.realtimeleaderboard.ui.theme.RankUp
import com.tarunguptaraja.realtimeleaderboard.ui.theme.ScoreFlash
import com.tarunguptaraja.realtimeleaderboard.ui.theme.Silver

@Composable
fun LeaderboardRow(
    entry: LeaderboardEntry,
    isAlternating: Boolean,
    modifier: Modifier = Modifier
) {
    val defaultColor = if (isAlternating) {
        MaterialTheme.colorScheme.surfaceVariant
    } else {
        MaterialTheme.colorScheme.surface
    }

    val backgroundColor = remember { Animatable(defaultColor) }

    LaunchedEffect(defaultColor) {
        if (!backgroundColor.isRunning) {
            backgroundColor.snapTo(defaultColor)
        }
    }

    LaunchedEffect(entry.score) {
        if (entry.lastScoreChange > 0) {
            backgroundColor.animateTo(ScoreFlash, animationSpec = tween(150))
            backgroundColor.animateTo(defaultColor, animationSpec = tween(850))
        }
    }

    val border = when (entry.rank) {
        1 -> BorderStroke(2.dp, Gold)
        2 -> BorderStroke(1.5.dp, Silver)
        3 -> BorderStroke(1.dp, Bronze)
        else -> null
    }

    val cardColors = CardDefaults.cardColors(
        containerColor = backgroundColor.value
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp),
        shape = RoundedCornerShape(12.dp),
        border = border,
        colors = cardColors,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                RankBadge(rank = entry.rank)

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = entry.playerName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.weight(1f)
                )

                val rankDiff = entry.previousRank - entry.rank
                if (rankDiff > 0) {
                    Text(
                        text = "▲$rankDiff",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RankUp,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                } else if (rankDiff < 0) {
                    Text(
                        text = "▼${-rankDiff}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = RankDown,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        ),
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }

            ScoreText(
                score = entry.score,
                lastScoreChange = entry.lastScoreChange
            )
        }
    }
}
