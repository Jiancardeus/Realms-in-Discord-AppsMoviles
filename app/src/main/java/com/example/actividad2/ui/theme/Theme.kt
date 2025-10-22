package com.example.actividad2.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme

// Paleta de colores para el TCG

// Definición de un esquema de colores oscuro
private val DarkColorPalette = darkColorScheme(
    primary = TealAccent, // Color principal (botones, acentos)
    secondary = DarkGrayBackground,
    background = DarkerBackground, // Fondo de la aplicación
    surface = DarkGrayBackground, // Superficies de cards y contenedores
    onPrimary = Color.Black, // Texto sobre el color primario
    onBackground = Color.White,
    onSurface = Color.White,
    error = RedError
)

// Asegúrate de que el nombre de esta función coincida con lo que usas en MainActivity.kt
@Composable
fun Actividad2Theme(
    content: @Composable () -> Unit
) {
    // Aplicamos el esquema de color oscuro
    MaterialTheme(
        colorScheme = DarkColorPalette,
        typography = Typography, // Asumo que el archivo Typography.kt existe
        content = content
    )
}


