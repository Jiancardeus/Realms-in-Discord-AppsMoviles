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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.ui.theme.TealAccent
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
        modifier = modifier.width(320.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E2E)
        )
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            // Header del perfil
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "👤 Perfil de Jugador",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Información del usuario
            UserInfoSection(user, onEditProfile)

            Spacer(modifier = Modifier.height(20.dp))

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
    var isExpanded by remember { mutableStateOf(true) }

    Column {
        // Header de Información como botón desplegable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📋 INFORMACIÓN",
                style = MaterialTheme.typography.titleSmall,
                color = TealAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            // Ícono desplegable
            Text(
                text = if (isExpanded) "▲" else "▼",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Contenido desplegable
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEditProfile() }
                    .background(
                        color = Color(0xFF2D2D3D),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                // Información del usuario
                InfoRow("👤 Usuario", user?.username ?: "N/A", Color.White, TealAccent)
                InfoRow("📧 Email", user?.email ?: "N/A", TealAccent, TealAccent)
                InfoRow("⭐ Nivel", user?.level?.toString() ?: "1", Color.White, TealAccent)

                Spacer(modifier = Modifier.height(12.dp))

                // Indicador de acción
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Editar perfil",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "→",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp
                    )
                }
            }
        }

        // Línea divisoria
        if (isExpanded) {
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun StatsSection(user: User?) {
    var isExpanded by remember { mutableStateOf(true) } // Expandido por defecto

    Column {
        // Header de Estadísticas como botón desplegable
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded }
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📊 ESTADÍSTICAS",
                style = MaterialTheme.typography.titleSmall,
                color = TealAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Text(
                text = if (isExpanded) "▲" else "▼",
                color = TealAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Contenido desplegable
        if (isExpanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = Color(0xFF2D2D3D),
                        shape = MaterialTheme.shapes.medium
                    )
                    .padding(16.dp)
            ) {
                InfoRow("✅ Victorias", user?.wins?.toString() ?: "0", Color.White, TealAccent)
                InfoRow("❌ Derrotas", user?.losses?.toString() ?: "0", Color.White, TealAccent)
                InfoRow("🤝 Empates", user?.draws?.toString() ?: "0", Color.White, TealAccent)

                Spacer(modifier = Modifier.height(12.dp))

                // Barra de progreso de experiencia con mejor contraste
                Column {
                    Text(
                        text = "🎯 EXPERIENCIA",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${user?.experience ?: 0}/${(user?.level ?: 1) * 1000}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TealAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(
                        progress = { (user?.experience?.toFloat() ?: 0f) / ((user?.level ?: 1) * 1000f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp),
                        color = TealAccent,
                        trackColor = Color(0xFF404050)
                    )
                }
            }
        }
    }
}

@Composable
fun InfoRow(label: String, value: String, labelColor: Color = Color.White, valueColor: Color = TealAccent) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColor,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold
        )
    }
}