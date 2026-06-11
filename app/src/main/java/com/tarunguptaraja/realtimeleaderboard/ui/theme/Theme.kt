package com.tarunguptaraja.realtimeleaderboard.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Blue700,
    onPrimary = Color.White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue800,
    secondary = Amber800,
    onSecondary = Color.White,
    secondaryContainer = Amber50,
    onSecondaryContainer = Amber900,
    background = Color(0xFFF0F4C3), // Soft light tint background
    onBackground = Color(0xFF102A43),
    surface = Color.White,
    onSurface = Color(0xFF102A43),
    surfaceVariant = Blue50,
    onSurfaceVariant = Color(0xFF486581),
    error = RankDown,
    onError = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Blue200,
    onPrimary = Color(0xFF102A43),
    primaryContainer = Blue800,
    onPrimaryContainer = Blue100,
    secondary = Amber300,
    onSecondary = Color(0xFF102A43),
    secondaryContainer = Amber900,
    onSecondaryContainer = Amber50,
    background = Color(0xFF0F172A), // Slate 900
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),    // Slate 800
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155), // Slate 700
    onSurfaceVariant = Color(0xFF94A3B8),
    error = RankDown,
    onError = Color.White
)

@Composable
fun RealTimeLeaderboardTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Set default dynamicColor to false to ensure our custom theme is shown by default
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}