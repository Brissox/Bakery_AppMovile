package com.example.prueba.ui.theme
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = BrownPrimary,
    onPrimary = Color.White,
    secondary = PeachAccent,
    onSecondary = Color.White,
    background = CreamPrimary,
    onBackground = TextPrimary,
    surface = CreamLight,
    onSurface = TextPrimary
)

private val DarkColorScheme = darkColorScheme(
    primary = BrownDark,
    onPrimary = Color.White,
    secondary = PeachAccent,
    onSecondary = Color.White,
    background = Color(0xFF2B1D17),
    onBackground = Color(0xFFFFF5E1),
    surface = Color(0xFF3E2723),
    onSurface = Color(0xFFFFEFD5)
)

@Composable
fun PruebaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}