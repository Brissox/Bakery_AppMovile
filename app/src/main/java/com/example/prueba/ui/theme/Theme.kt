package com.example.prueba.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Colores para modo claro
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

//COLOR MODO OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = Color.White,          // color de botones y elementos primarios
    onPrimary = Color.Black,        // texto sobre botones (si quieres)
    secondary = Color.White,        // acentos (pueden ser botones secundarios)
    onSecondary = Color.Black,      // texto sobre acentos
    background = Color.Black,       // fondo negro
    onBackground = Color.White,     // texto principal blanco
    surface = Color.Black,          // tarjetas y recuadros negros
    onSurface = Color.White,        // texto sobre tarjetas
    error = Color(0xFFFF5555),     // rojo brillante para errores
    onError = Color.White,          // texto sobre errores
    outline = Color.White           // bordes blancos
)
@Composable
fun PruebaTheme(
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
