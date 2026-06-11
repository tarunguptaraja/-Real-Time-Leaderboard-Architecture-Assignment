package com.tarunguptaraja.realtimeleaderboard.ui.screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RankBadge(
    rank: Int,
    modifier: Modifier = Modifier
) {
    when (rank) {
        1 -> Text(text = "🥇", fontSize = 28.sp, modifier = modifier)
        2 -> Text(text = "🥈", fontSize = 28.sp, modifier = modifier)
        3 -> Text(text = "🥉", fontSize = 28.sp, modifier = modifier)
        else -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer)
            ) {
                Text(
                    text = "#$rank",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 13.sp
                    )
                )
            }
        }
    }
}
