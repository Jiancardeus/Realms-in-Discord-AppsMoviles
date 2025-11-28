// En ui/login/LoginScreen.kt - REFACTORIZAR:
package com.example.realmsindiscord.ui.login

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
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.realmsindiscord.viewmodel.login.LoginViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navegar al home cuando el login es exitoso
    LaunchedEffect(uiState.loginResource) {
        if (uiState.loginResource.isSuccess) {
            onLoginSuccess()
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

                    Text("Realms in Discord TCG", fontSize = 24.sp, color = TealAccent, fontWeight = FontWeight.Bold)

                    // Manejo de estados
                    when {
                        uiState.loginResource.isLoading -> {
                            DefaultLoadingState(
                                message = "Iniciando sesión...",
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        uiState.loginResource.isError -> {
                            DefaultErrorState(
                                message = uiState.loginResource.getOrNull()?.toString() ?: "Error desconocido",
                                onRetry = { viewModel.login(uiState.username, uiState.password) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        else -> {
                            // Formulario de login
                            LoginForm(
                                username = uiState.username,
                                password = uiState.password,
                                onUsernameChange = viewModel::updateUsername,
                                onPasswordChange = viewModel::updatePassword,
                                onLogin = { viewModel.login(uiState.username, uiState.password) },
                                onNavigateToRegister = onNavigateToRegister
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LoginForm(
    username: String,
    password: String,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLogin: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        // Campo de Usuario
        OutlinedTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = { Text("Usuario", color = Color.White.copy(alpha = 0.7f)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = { Text("Contraseña", color = Color.White.copy(alpha = 0.7f)) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        // Botón de Ingresar
        Button(
            onClick = onLogin,
            enabled = username.isNotBlank() && password.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
        ) {
            Text("INGRESAR", color = Color.Black, fontWeight = FontWeight.Bold)
        }

        // Enlace a Registro
        Text(
            text = "¿No tienes cuenta? Regístrate",
            color = TealAccent,
            modifier = Modifier
                .clickable { onNavigateToRegister() }
                .align(Alignment.CenterHorizontally)
        )
    }
}