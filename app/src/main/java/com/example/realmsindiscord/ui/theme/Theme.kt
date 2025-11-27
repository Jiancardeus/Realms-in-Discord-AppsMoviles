package com.example.realmsindiscord.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme

private val DarkColorPalette = darkColorScheme(
    primary = TealAccent,
    secondary = DarkGrayBackground,
    background = DarkerBackground,
    surface = DarkGrayBackground,
    onPrimary = Color.Black,
    onBackground = Color.White,
    onSurface = Color.White,
    error = RedError
)

@Composable
fun RealmsInDiscordTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = Typography,
        content = content
    )
}