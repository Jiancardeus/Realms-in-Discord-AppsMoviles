package com.example.realmsindiscord.ui.register

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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.R
import com.example.realmsindiscord.ui.theme.DarkGrayBackground
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.register.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationSuccess: (username: String) -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    if (uiState.registrationResult != null) {
        LaunchedEffect(uiState.registrationResult) {
            if (uiState.registrationResult.toString().contains("Success")) {
                onRegistrationSuccess(username)
                viewModel.resetState()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Imagen de fondo
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

                    // 1. Campo de Email
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

                    // Enlace a Login
                    Text("¿Ya tienes cuenta? Inicia Sesión",
                        color = TealAccent,
                        modifier = Modifier.clickable { onNavigateToLogin() }
                    )
                }
            }
        }
    }
}