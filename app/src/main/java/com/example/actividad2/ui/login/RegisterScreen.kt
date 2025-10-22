package com.example.actividad2.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel // Importar hiltViewModel para la inyección

/**
 * Pantalla de registro de nuevos usuarios.
 * @param onRegisterSuccess Función a llamar cuando el registro es exitoso.
 * @param onNavigateToLogin Función a llamar para volver a la pantalla de inicio de sesión.
 * @param viewModel ViewModel inyectado por Hilt para manejar la lógica de registro.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onRegisterSuccess: (String) -> Unit,
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    // Estados mutables para los campos de entrada
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Estado de registro proveniente del ViewModel
    val registerState by viewModel.registerState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Título
        Text(
            text = "Crear Cuenta",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Campo de Nombre de Usuario
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nombre de Usuario") },
            leadingIcon = { Icon(Icons.Default.AccountCircle, contentDescription = "Usuario") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = registerState != RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Correo Electrónico
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = registerState != RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Campo de Contraseña
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            enabled = registerState != RegisterState.Loading
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Manejo de Estado (Error y Carga)
        when (val state = registerState) {
            is RegisterState.Error -> {
                Text(
                    text = state.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            else -> {}
        }

        // Botón de Registro
        Button(
            onClick = {
                viewModel.register(username, email, password)
            },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = registerState != RegisterState.Loading &&
                    username.isNotBlank() && email.isNotBlank() && password.length >= 4 // Validación simple de longitud
        ) {
            if (registerState == RegisterState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Registrarse", style = MaterialTheme.typography.titleMedium)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para volver al Login
        TextButton(onClick = onNavigateToLogin) {
            Text("¿Ya tienes cuenta? Inicia Sesión")
        }

        // Navegación al éxito
        LaunchedEffect(registerState) {
            if (registerState == RegisterState.Success) {
                // Llama a la función de navegación provista por el Navigation Host
                onRegisterSuccess(username)
                // Opcional: limpiar el estado después de navegar
                viewModel.resetState()
            }
        }
    }
}
