package com.example.realmsindiscord.ui.common

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun CardItem(
    card: com.example.realmsindiscord.data.remote.model.CardModel,
    onClick: () -> Unit
) {
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
            // 🖼️ IMAGEN PRIMERO - MÁS ARRIBA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(Color.DarkGray.copy(alpha = 0.3f))
            ) {
                val imageResId = getImageResourceFromUrl(card.imageUrl)

                if (imageResId != 0) {
                    androidx.compose.foundation.Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = "Imagen de ${card.name}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.DarkGray.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🎴",
                                fontSize = 24.sp
                            )
                            Text(
                                text = "Sin Imagen",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

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

                // Costo
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
        "caballeros solares" -> Color(0xFFFFD700) // Dorado
        "corrupción" -> Color(0xFF9B59B6) // Púrpura
        "fire" -> Color(0xFFFF6B35)
        "water" -> Color(0xFF4FC3F7)
        "earth" -> Color(0xFF81C784)
        "air" -> Color(0xFFE1F5FE)
        "neutral" -> Color(0xFFB0BEC5)
        else -> TealAccent
    }
}

// Función para obtener el recurso de imagen desde la URL
private fun getImageResourceFromUrl(imageUrl: String): Int {
    return when {
        imageUrl.contains("espadachin_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.espadachin_solar
        imageUrl.contains("sacerdote_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.sacerdote_solar
        imageUrl.contains("porta_estandarte", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.porta_estandarte
        imageUrl.contains("alabardero_real", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.alabardero_real
        imageUrl.contains("heroe_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.heroe_solar
        imageUrl.contains("rey_paladin", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.rey_paladin
        imageUrl.contains("mago_de_batalla", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.mago_de_batalla
        imageUrl.contains("avatar_de_la_luz", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.avatar_de_la_luz
        imageUrl.contains("acolito_putrido", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.acolito_putrido
        imageUrl.contains("huevo_de_la_podredumbre", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.huevo_de_la_podredumbre
        imageUrl.contains("sin_luz", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.sin_luz
        imageUrl.contains("amalgama_de_putrefaccion", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.amalgama_de_putrefaccion
        imageUrl.contains("neuro_amalgama", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.neuro_amalgama
        imageUrl.contains("corruptores", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.corruptores
        imageUrl.contains("gran_devorador", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.gran_devorador
        imageUrl.contains("senor_de_la_podredumbre", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.senor_de_la_podredumbre
        imageUrl.contains("gran_cancer", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.gran_cancer
        imageUrl.contains("miasma_toxica", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.miasma_toxica
        imageUrl.contains("quistes", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.quistes
        imageUrl.contains("campo_de_corrupcion", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.campo_de_corrupcion
        else -> 0
    }
}