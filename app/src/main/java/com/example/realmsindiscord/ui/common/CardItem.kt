package com.example.realmsindiscord.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun CardItem(card: Card) {
    val factionColor = getFactionColor(card.faction ?: "Neutral")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .padding(4.dp)
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(factionColor, factionColor.copy(alpha = 0.7f))
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header: Costo y Facción
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Badge de Costo
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(
                            color = TealAccent,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${card.cost}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }

                // Badge de Facción
                Box(
                    modifier = Modifier
                        .height(24.dp)
                        .padding(horizontal = 8.dp)
                        .background(
                            color = factionColor.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = factionColor,
                            shape = RoundedCornerShape(6.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = getFactionAbbreviation(card.faction ?: "Neutral"),
                        color = factionColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp)
                    )
                }
            }

            // Nombre de la Carta
            Text(
                text = card.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )

            // Imagen de la Carta
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = card.imageResId),
                    contentDescription = card.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Overlay para mejorar legibilidad del texto
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = 0.1f)
                                )
                            )
                        )
                )
            }

            // Stats: Ataque y Salud
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatBadge(
                    label = "ATK",
                    value = card.attack,
                    color = Color(0xFFFF6B6B), // Rojo
                    icon = "⚔️"
                )

                Spacer(modifier = Modifier.width(8.dp))

                StatBadge(
                    label = "HP",
                    value = card.health,
                    color = Color(0xFF4ECDC4), // Verde
                    icon = "❤️"
                )
            }

            // Tipo y Descripción
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            ) {
                Text(
                    text = card.type.uppercase(),
                    color = factionColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                if (card.description.isNotBlank()) {
                    Text(
                        text = card.description,
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 2.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun StatBadge(label: String, value: Int, color: Color, icon: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = icon,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 2.dp)
        )
        Text(
            text = label,
            color = color,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "$value",
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

fun getFactionColor(faction: String): Color {
    return when (faction) {
        "Caballeros Solares" -> Color(0xFFFFD700) // Dorado
        "Corrupción" -> Color(0xFF9B59B6) // Púrpura
        else -> Color(0xFF61DAFB) // Teal por defecto
    }
}

fun getFactionAbbreviation(faction: String): String {
    return when (faction) {
        "Caballeros Solares" -> "SOL"
        "Corrupción" -> "CORR"
        else -> "NEUT"
    }
}