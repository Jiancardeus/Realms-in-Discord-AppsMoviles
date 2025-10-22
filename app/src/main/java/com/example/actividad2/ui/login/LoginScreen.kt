package com.example.actividad2.ui.login

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2.R // Importar recursos (drawable/mipmap)
import com.example.actividad2.ui.theme.DarkGrayBackground
import com.example.actividad2.ui.theme.TealAccent // Colores de ejemplo

@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onLoginSuccess: (username: String) -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Reacciona al estado de autenticación exitosa
    if (uiState.isAuthenticated) {
        onLoginSuccess(username)
    }

    // El contenedor principal (simulando .login-container)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Contenedor del formulario con el estilo semitransparente de tu CSS
        Card(
            modifier = Modifier.fillMaxWidth(0.9f),
            colors = CardDefaults.cardColors(containerColor = DarkGrayBackground.copy(alpha = 0.85f)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(30.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.portada_r_d),
                    contentDescription = "Logo TCG",
                    modifier = Modifier.size(150.dp)
                )

                Text("Realms in Discord TCG", fontSize = 24.sp, color = TealAccent, fontWeight = FontWeight.Bold)

                // Campo de Usuario
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Campo de Contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña", color = Color.White.copy(alpha = 0.7f)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Mensaje de Error
                if (uiState.error != null) {
                    Text(text = uiState.error!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                }

                // Botón de Ingresar
                Button(
                    onClick = { viewModel.login(username, password) },
                    enabled = !uiState.isLoading,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
                ) {
                    Text(if (uiState.isLoading) "Cargando..." else "INGRESAR", color = Color.Black, fontWeight = FontWeight.Bold)
                }

                // Enlace a Registro
                Text("¿No tienes cuenta? Regístrate",
                    color = TealAccent,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }
    }
}