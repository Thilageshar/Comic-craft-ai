package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = ComicRed,
    onPrimary = Color.White,
    primaryContainer = ComicRedDark,
    onPrimaryContainer = Color.White,
    secondary = ComicYellow,
    onSecondary = ComicInk,
    secondaryContainer = ComicYellowDark,
    onSecondaryContainer = Color.White,
    tertiary = ComicBlue,
    background = ComicBgDark,
    surface = ComicSurfaceDark,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF334155),
    outline = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = ComicRed,
    onPrimary = Color.White,
    primaryContainer = ComicRedLight,
    onPrimaryContainer = ComicRedDark,
    secondary = ComicYellow,
    onSecondary = ComicInk,
    secondaryContainer = ComicYellowLight,
    onSecondaryContainer = ComicInk,
    tertiary = ComicBlue,
    background = ComicBgLight,
    surface = ComicCardBg,
    onBackground = ComicInk,
    onSurface = ComicInk,
    surfaceVariant = Color(0xFFF1F5F9),
    outline = ComicInk
)

@Composable
fun ComicCraftTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
