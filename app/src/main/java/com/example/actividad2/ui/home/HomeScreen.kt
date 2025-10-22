package com.example.actividad2.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onNavigateToLibrary: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "¡BIENVENIDO AL TCG!", color = Color.Black)
            Button(onClick = onNavigateToLibrary) {
                Text("Ir a la Biblioteca")
            }
            Button(onClick = onLogout) {
                Text("Cerrar Sesión")
            }
        }
    }
}