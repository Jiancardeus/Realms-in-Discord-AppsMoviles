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
import androidx.compose.ui.draw.shadow
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
            .height(320.dp) // ← Más altura
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
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header muy compacto
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CostBadge(cost = card.cost)

                Text(
                    text = getFactionAbbreviation(card.faction ?: "Neutral"),
                    color = factionColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 10.sp
                )
            }

            // Nombre compacto
            Text(
                text = card.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            // IMAGEN MUY DESTACADA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp) // ← Mucha más altura
                    .clip(RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Image(
                    painter = painterResource(id = card.imageResId),
                    contentDescription = card.name,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit // ← Imagen completa sin recortar
                )
            }

            // Stats y tipo en sección inferior compacta
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stats
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatBadge(
                        label = "ATAQUE",
                        value = card.attack,
                        color = Color(0xFFFF6B6B),
                        icon = "⚔️"
                    )

                    StatBadge(
                        label = "VIDA",
                        value = card.health,
                        color = Color(0xFF4ECDC4),
                        icon = "❤️"
                    )
                }

                // Tipo
                Text(
                    text = card.type.uppercase(),
                    color = factionColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 4.dp)
                )

                // Descripción (solo si cabe)
                if (card.description.isNotBlank() && card.description.length < 50) {
                    Text(
                        text = card.description,
                        color = Color.LightGray,
                        fontSize = 9.sp,
                        maxLines = 1,
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

@Composable
fun CostBadge(cost: Int) {
    Box(
        modifier = Modifier
            .size(32.dp),
        contentAlignment = Alignment.Center
    ) {
        // Fondo con gradiente
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF64B5F6),
                            Color(0xFF1976D2),
                            Color(0xFF0D47A1)
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
                .border(
                    width = 1.5.dp,
                    color = Color(0xFFBBDEFB),
                    shape = RoundedCornerShape(10.dp)
                )
        )

        // Efecto de brillo
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
        )

        // Contenido
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "⚡",
                fontSize = 12.sp,
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Text(
                text = "$cost",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 12.sp,
                modifier = Modifier.shadow(2.dp)
            )
        }
    }
}
fun getFactionAbbreviation(faction: String): String {
    return when (faction) {
        "Caballeros Solares" -> "SOL"
        "Corrupción" -> "CORR"
        else -> "NEUT"
    }
}