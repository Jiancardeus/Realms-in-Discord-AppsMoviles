package com.example.actividad2.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2.R
import com.example.actividad2.ui.theme.DarkGrayBackground
import com.example.actividad2.ui.theme.TealAccent
import com.example.actividad2.domain.useCase.RegisterUseCase

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onRegistrationSuccess: (username: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Reacciona al estado de registro exitoso
    if (uiState.registrationResult == RegisterUseCase.RegisterResult.Success) {
        onRegistrationSuccess(username) // Navega al Home
        viewModel.resetState()
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo NUEVA
        Image(
            painter = painterResource(id = R.drawable.my_app_icon),
            contentDescription = "App Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Contenedor principal con el mismo estilo de Login
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

                    Text("Crear Cuenta TCG", fontSize = 24.sp, color = TealAccent, fontWeight = FontWeight.Bold)

                    // 1. Campo de Email (NUEVO)
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Correo Electrónico") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    )

                    // 2. Campo de Usuario
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        label = { Text("Usuario") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    )

                    // 3. Campo de Contraseña
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !uiState.isLoading
                    )

                    // Mensaje de Error
                    if (uiState.error != null) {
                        Text(text = uiState.error!!, color = Color.Red, style = MaterialTheme.typography.bodySmall)
                    }

                    // Botón de Registro
                    Button(
                        onClick = { viewModel.register(username, email, password) },
                        enabled = !uiState.isLoading && username.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
                    ) {
                        Text(if (uiState.isLoading) "Registrando..." else "REGISTRARSE", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    // Enlace a Login - ¡SE MANTIENE INTACTO!
                    Text("¿Ya tienes cuenta? Inicia Sesión",
                        color = TealAccent,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}