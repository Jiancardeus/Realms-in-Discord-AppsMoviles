package com.example.realmsindiscord.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun CardItem(
    card: com.example.realmsindiscord.data.remote.model.CardModel,
    onClick: () -> Unit
) {
    // DEBUG: Verificar la URL de la imagen
    println("DEBUG CARD: ${card.name} - Image URL: '${card.imageUrl}' - Blank: ${card.imageUrl.isBlank()}")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.8f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // IMAGEN DE LA CARTA - VERSIÓN MEJORADA
            CardImageSection(card)

            Spacer(modifier = Modifier.height(8.dp))

            // Encabezado: Nombre y Costo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = card.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Costo con diseño mejorado
                Box(
                    modifier = Modifier
                        .background(TealAccent, CircleShape)
                        .size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${card.cost}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // SOLO 3 ATRIBUTOS: Ataque, Vida/Defensa, Tipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Ataque (si es mayor a 0)
                if (card.attack > 0) {
                    StatBadge("⚔️${card.attack}", Color.Red)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Vida o Defensa (usar el que tenga valor)
                val defenseValue = if (card.defense > 0) card.defense else card.health
                if (defenseValue > 0) {
                    StatBadge("❤️$defenseValue", Color.Green)
                } else {
                    Spacer(modifier = Modifier.weight(1f))
                }

                // Tipo de carta
                Text(
                    text = card.type,
                    color = Color.Yellow.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Descripción
            Text(
                text = card.description,
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 12.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Facción
            Text(
                text = card.faction,
                color = getFactionColor(card.faction),
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun CardImageSection(card: com.example.realmsindiscord.data.remote.model.CardModel) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.DarkGray.copy(alpha = 0.3f))
    ) {
        // Usar SubcomposeAsyncImage para mejor control
        SubcomposeAsyncImage(
            model = if (card.imageUrl.isNotBlank()) card.imageUrl else null,
            contentDescription = "Imagen de ${card.name}",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        ) {
            val state = painter.state
            if (state is coil.compose.AsyncImagePainter.State.Loading || card.imageUrl.isBlank()) {
                // Loading o sin imagen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.DarkGray.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (card.imageUrl.isBlank()) "Sin Imagen" else "Cargando...",
                            color = Color.Gray,
                            fontSize = 12.sp
                        )
                        if (card.imageUrl.isNotBlank()) {
                            Text(
                                text = card.imageUrl.take(30) + "...",
                                color = Color.Gray,
                                fontSize = 8.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            } else if (state is coil.compose.AsyncImagePainter.State.Error) {
                // Error cargando imagen
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Error imagen",
                            color = Color.Red,
                            fontSize = 12.sp
                        )
                        Text(
                            text = card.imageUrl.take(30) + "...",
                            color = Color.Red,
                            fontSize = 8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            } else {
                // Imagen cargada correctamente
                SubcomposeAsyncImageContent()
            }
        }
    }
}

@Composable
private fun StatBadge(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold
    )
}

private fun getFactionColor(faction: String): Color {
    return when (faction.lowercase()) {
        "fire" -> Color(0xFFFF6B35)
        "water" -> Color(0xFF4FC3F7)
        "earth" -> Color(0xFF81C784)
        "air" -> Color(0xFFE1F5FE)
        "neutral" -> Color(0xFFB0BEC5)
        else -> TealAccent
    }
}