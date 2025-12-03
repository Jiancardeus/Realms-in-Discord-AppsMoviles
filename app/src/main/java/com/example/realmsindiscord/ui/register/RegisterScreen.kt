package com.example.realmsindiscord.ui.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import com.example.realmsindiscord.ui.common.DefaultErrorState
import com.example.realmsindiscord.ui.common.DefaultLoadingState
import com.example.realmsindiscord.ui.theme.DarkGrayBackground
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.register.RegisterViewModel

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = hiltViewModel(),
    onRegistrationSuccess: () -> Unit,
    onNavigateToLogin: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegar al home cuando el registro es exitoso
    LaunchedEffect(uiState.registerResource) {
        if (uiState.registerResource.isSuccess) {
            onRegistrationSuccess()
            viewModel.resetState()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
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

                    // Manejo de estados
                    when {
                        uiState.registerResource.isLoading -> {
                            DefaultLoadingState(
                                message = "Creando cuenta...",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        uiState.registerResource.isError -> {
                            DefaultErrorState(
                                message = uiState.registerResource.getOrNull()?.toString() ?: "Error desconocido",
                                onRetry = { viewModel.register(uiState.username, uiState.email, uiState.password) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        else -> {
                            // Formulario de registro
                            RegisterForm(
                                username = uiState.username,
                                email = uiState.email,
                                password = uiState.password,
                                onUsernameChange = viewModel::updateUsername,
                                onEmailChange = viewModel::updateEmail,
                                onPasswordChange = viewModel::updatePassword,
                                onRegister = { viewModel.register(uiState.username, uiState.email, uiState.password) },
                                onNavigateToLogin = onNavigateToLogin,
                                isLoading = uiState.registerResource.isLoading
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegisterForm(
    username: String,
    email: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onRegister: () -> Unit,
    onNavigateToLogin: () -> Unit,
    isLoading: Boolean
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // Campo de Email
        OutlinedTextField(
            value = email,
            onValueChange = onEmailChange,
            label = { Text("Correo Electrónico") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        // Campo de Usuario
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        )

        // Botón de Registro
        Button(
            onClick = onRegister,
            enabled = !isLoading && username.isNotBlank() && email.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
        ) {
            Text(
                text = if (isLoading) "Registrando..." else "REGISTRARSE",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }

        // Enlace a Login
        Text(
            text = "¿Ya tienes cuenta? Inicia Sesión",
            color = TealAccent,
            modifier = Modifier
                .clickable { onNavigateToLogin() }
                .align(Alignment.CenterHorizontally)
        )
    }
}