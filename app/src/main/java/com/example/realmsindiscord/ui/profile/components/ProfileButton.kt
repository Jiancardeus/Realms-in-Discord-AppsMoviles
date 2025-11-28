package com.example.realmsindiscord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.viewmodel.profile.ProfileViewModel

@Composable
fun ProfileButton(
    viewModel: ProfileViewModel,
    onEditProfile: (User) -> Unit,
    modifier: Modifier = Modifier
) {
    val user by viewModel.userState.collectAsState()
    val isExpanded by viewModel.isProfileExpanded.collectAsState()

    // Debug temporal
    println("DEBUG: ProfileButton - user = $user")
    println("DEBUG: ProfileButton - isExpanded = $isExpanded")

    Box(modifier = modifier) {
        // Botón compacto del perfil
        Card(
            onClick = { viewModel.toggleProfileExpanded() },
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Avatar y nombre
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Avatar circular
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = user?.username?.take(1)?.uppercase() ?: "U",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = user?.username ?: "Usuario",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Nivel ${user?.level ?: 1}",
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            fontSize = 12.sp
                        )
                    }
                }

                // Indicador de expansión
                Text(
                    text = if (isExpanded) "▲" else "▼",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 16.sp
                )
            }
        }

        // Panel expandido del perfil
        if (isExpanded) {
            ProfilePanel(
                user = user,
                onClose = { viewModel.toggleProfileExpanded() },
                onEditProfile = {
                    if (user != null) {
                        onEditProfile(user!!)
                        viewModel.toggleProfileExpanded()
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(280.dp)
                    .offset(y = 70.dp)
            )
        }
    }
}

@Composable
fun ProfilePanel(
    user: User?,
    onClose: () -> Unit,
    onEditProfile: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Header del perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Perfil de Jugador",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Información del usuario - TODA LA SECCIÓN CLICKABLE
            UserInfoSection(user, onEditProfile)

            Spacer(modifier = Modifier.height(16.dp))

            // Estadísticas
            StatsSection(user)
        }
    }
}

@Composable
fun UserInfoSection(
    user: User?,
    onEditProfile: () -> Unit
) {
    // Hacer toda la sección clickable
    Column(
        modifier = Modifier
            .clickable { onEditProfile() }
            .fillMaxWidth()
    ) {
        Text(
            text = "Información",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.padding(vertical = 4.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Información del usuario
        InfoRow("Usuario", user?.username ?: "N/A")
        InfoRow("Email", user?.email ?: "N/A")
        InfoRow("Nivel", user?.level.toString())

        // Agregar feedback visual al hacer click
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Toca para editar perfil →",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
fun StatsSection(user: User?) {
    Column {
        Text(
            text = "Estadísticas",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(8.dp))

        InfoRow("Victorias", user?.wins?.toString() ?: "0")
        InfoRow("Derrotas", user?.losses?.toString() ?: "0")
        InfoRow("Empates", user?.draws?.toString() ?: "0")

        Spacer(modifier = Modifier.height(8.dp))

        // Barra de progreso de experiencia
        Text(
            text = "Experiencia: ${user?.experience ?: 0}/${(user?.level ?: 1) * 1000}",
            style = MaterialTheme.typography.bodySmall
        )
        LinearProgressIndicator(
            progress = { (user?.experience?.toFloat() ?: 0f) / ((user?.level ?: 1) * 1000f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
        )
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}