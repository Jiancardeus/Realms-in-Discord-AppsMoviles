package com.example.realmsindiscord.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.viewmodel.profile.ProfileManagementViewModel

@Composable
fun ProfileManagementScreen(
    user: User?,
    onBack: () -> Unit,
    onAccountDeleted: () -> Unit,
    viewModel: ProfileManagementViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val userState by viewModel.userState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

    // Mostrar loading mientras carga
    if (state.isLoading && userState == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Cargando información del usuario...")
        }
        return
    }

    // Mostrar error si no se pudo cargar el usuario
    if (userState == null && state.error != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Error: ${state.error}",
                color = MaterialTheme.colorScheme.error
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Volver")
            }
        }
        return
    }

    val currentUser = userState ?: return

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestión de Perfil",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onBack) {
                Text("Volver")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Mensajes de estado
        state.error?.let { error ->
            AlertMessage(message = error, isError = true) {
                viewModel.clearMessages()
            }
        }

        state.successMessage?.let { message ->
            AlertMessage(message = message, isError = false) {
                viewModel.clearMessages()
            }
        }

        // Sección de información de usuario
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Información Actual",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                InfoRow("Usuario actual", currentUser.username)
                InfoRow("Email", currentUser.email)
                InfoRow("Nivel", currentUser.level.toString())
                InfoRow("Victorias", currentUser.wins.toString())
                InfoRow("Derrotas", currentUser.losses.toString())
                InfoRow("Empates", currentUser.draws.toString())
                InfoRow("Experiencia", currentUser.experience.toString())
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de cambiar nombre de usuario
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Cambiar Nombre de Usuario",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (state.isEditingUsername) {
                    OutlinedTextField(
                        value = state.newUsername,
                        onValueChange = { viewModel.updateNewUsername(it) },
                        label = { Text("Nuevo nombre de usuario") },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { viewModel.cancelEditing() },
                            enabled = !state.isLoading
                        ) {
                            Text("Cancelar")
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.saveUsername(currentUser.id, currentUser.username) },
                            enabled = !state.isLoading && state.newUsername.isNotBlank()
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text("Guardar")
                            }
                        }
                    }
                } else {
                    Button(
                        onClick = { viewModel.startEditingUsername(currentUser.username) },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    ) {
                        Text("Cambiar Nombre de Usuario")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sección de eliminar cuenta
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Zona Peligrosa",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Esta acción no se puede deshacer. Se eliminarán todos tus datos permanentemente.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onErrorContainer
                )

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { viewModel.showDeleteConfirmation() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !state.isLoading
                ) {
                    Text("Eliminar Cuenta")
                }
            }
        }

        // Diálogo de confirmación para eliminar cuenta
        if (state.showDeleteConfirmation) {
            AlertDialog(
                onDismissRequest = {
                    if (!state.isLoading) {
                        viewModel.hideDeleteConfirmation()
                    }
                },
                title = { Text("¿Eliminar cuenta?") },
                text = {
                    Text("Esta acción no se puede deshacer. ¿Estás seguro de que quieres eliminar tu cuenta permanentemente?")
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.deleteAccount(currentUser.id, onAccountDeleted) },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                        enabled = !state.isLoading
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onError
                            )
                        } else {
                            Text("Eliminar")
                        }
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            if (!state.isLoading) {
                                viewModel.hideDeleteConfirmation()
                            }
                        },
                        enabled = !state.isLoading
                    ) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Medium)
        Text(text = value, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun AlertMessage(message: String, isError: Boolean, onDismiss: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isError) MaterialTheme.colorScheme.errorContainer
            else MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = message,
                color = if (isError) MaterialTheme.colorScheme.onErrorContainer
                else MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onDismiss) {
                Text("OK")
            }
        }
    }
}