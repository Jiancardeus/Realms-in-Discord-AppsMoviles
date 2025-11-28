package com.example.realmsindiscord.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider // ← CAMBIAR ESTO
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.profile.ProfileManagementViewModel

@Composable
fun ProfileManagementScreen(
    user: User,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: ProfileManagementViewModel = hiltViewModel()


    LaunchedEffect(user.id) {
        viewModel.setUserDirectly(user)
    }

    val state by viewModel.state.collectAsState()
    val currentUser by viewModel.userState.collectAsState()

    // Usar el usuario del ViewModel o el parámetro como fallback
    val displayUser = currentUser ?: user

    ProfileManagementContent(
        user = displayUser,
        state = state,
        onBack = onBack,
        onLogout = onLogout,
        onStartEditingUsername = { viewModel.startEditingUsername(displayUser.username) },
        onCancelEditing = { viewModel.cancelEditing() },
        onUpdateUsername = { newUsername -> viewModel.updateNewUsername(newUsername) },
        onSaveUsername = { viewModel.saveUsername(displayUser.id, displayUser.username) },
        onShowDeleteConfirmation = { viewModel.showDeleteConfirmation() },
        onHideDeleteConfirmation = { viewModel.hideDeleteConfirmation() },
        onDeleteAccount = { viewModel.deleteAccount(displayUser.id, onLogout) },
        onClearMessages = { viewModel.clearMessages() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileManagementContent(
    user: User,
    state: com.example.realmsindiscord.viewmodel.profile.ProfileManagementState,
    onBack: () -> Unit,
    onLogout: () -> Unit,
    onStartEditingUsername: () -> Unit,
    onCancelEditing: () -> Unit,
    onUpdateUsername: (String) -> Unit,
    onSaveUsername: () -> Unit,
    onShowDeleteConfirmation: () -> Unit,
    onHideDeleteConfirmation: () -> Unit,
    onDeleteAccount: () -> Unit,
    onClearMessages: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Gestión de Perfil",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Text("← Volver", color = TealAccent)
                    }
                },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.8f))
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Título principal
                Text(
                    text = "# Gestión de Perfil",
                    color = TealAccent,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Información Actual
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "**Información Actual**",
                            color = TealAccent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        InfoRow("Usuario actual", user.username)
                        InfoRow("Email", user.email)
                        InfoRow("Nivel", user.level.toString())
                        InfoRow("Victorias", user.wins.toString())
                        InfoRow("Derrotas", user.losses.toString())
                        InfoRow("Empates", user.draws.toString())
                        InfoRow("Experiencia", "${user.experience}/${user.level * 1000}")
                    }
                }

                // CAMBIAR Divider por HorizontalDivider
                HorizontalDivider(
                    color = TealAccent.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                )

                // Cambiar Nombre de Usuario
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Black.copy(alpha = 0.7f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "## Cambiar Nombre de Usuario",
                            color = TealAccent,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (state.isEditingUsername) {
                            // Modo edición
                            OutlinedTextField(
                                value = state.newUsername,
                                onValueChange = onUpdateUsername,
                                label = { Text("Nuevo nombre de usuario") },
                                modifier = Modifier.fillMaxWidth(),
                                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White)
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = onCancelEditing,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text("Cancelar")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = onSaveUsername,
                                    colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
                                ) {
                                    Text("Guardar")
                                }
                            }
                        } else {
                            // Modo visualización
                            Button(
                                onClick = onStartEditingUsername,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = TealAccent)
                            ) {
                                Text("Cambiar Nombre de Usuario")
                            }
                        }

                        // Mensajes de error/éxito
                        state.error?.let { error ->
                            Text(
                                text = error,
                                color = Color.Red,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }

                        state.successMessage?.let { success ->
                            Text(
                                text = success,
                                color = Color.Green,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }

                // Zona Peligrosa
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Red.copy(alpha = 0.1f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "## Zona Peligrosa",
                            color = Color.Red,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text = "Esta acción no se puede deshacer. Se eliminarán todos tus datos permanentemente.",
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        if (state.showDeleteConfirmation) {
                            // Confirmación de eliminación
                            Text(
                                text = "¿Estás seguro de que quieres eliminar tu cuenta?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(bottom = 16.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Button(
                                    onClick = onHideDeleteConfirmation,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Gray)
                                ) {
                                    Text("Cancelar")
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                Button(
                                    onClick = onDeleteAccount,
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                                ) {
                                    Text("Confirmar Eliminación")
                                }
                            }
                        } else {
                            // Botón de eliminación
                            Button(
                                onClick = onShowDeleteConfirmation,
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                            ) {
                                Text("Eliminar Cuenta")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            color = Color.White,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            color = TealAccent,
            fontWeight = FontWeight.Bold
        )
    }
}