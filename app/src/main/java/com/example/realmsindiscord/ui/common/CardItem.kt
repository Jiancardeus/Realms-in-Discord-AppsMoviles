package com.example.realmsindiscord.ui.common

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
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
import com.example.realmsindiscord.R
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun CardItem(
    card: CardModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E) // Gris oscuro para mejor contraste
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // 1. IMAGEN DE LA CARTA
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp) // Un poco más alto para ver mejor
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Black)
            ) {
                // Usamos la función interna actualizada que incluye TODAS las cartas
                val imageResId = getImageResourceFromUrl(card.imageUrl)

                if (imageResId != 0) {
                    Image(
                        painter = painterResource(id = imageResId),
                        contentDescription = card.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop // Crop para llenar el cuadro
                    )
                } else {
                    // Placeholder si falla la imagen
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("🖼️", fontSize = 30.sp)
                            Text("Sin Imagen", color = Color.Gray, fontSize = 10.sp)
                            Text(card.imageUrl, color = Color.DarkGray, fontSize = 8.sp) // Debug
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 2. ENCABEZADO: NOMBRE Y COSTO
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = card.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Círculo de Costo
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(TealAccent, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${card.cost}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 3. ESTADÍSTICAS (Ataque / Defensa / Vida)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                // Ataque (Rojo)
                if (card.attack > 0) {
                    StatBadge("⚔️ ${card.attack}", Color(0xFFFF6B6B))
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Defensa (Azul) - Si tiene
                if (card.defense > 0) {
                    StatBadge("🛡️ ${card.defense}", Color(0xFF4FC3F7))
                    Spacer(modifier = Modifier.width(12.dp))
                }

                // Vida (Verde) - Si tiene (casi siempre)
                if (card.health > 0) {
                    StatBadge("❤️ ${card.health}", Color(0xFF51CF66))
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // 4. DESCRIPCIÓN
            Text(
                text = card.description,
                color = Color.LightGray,
                fontSize = 11.sp,
                lineHeight = 14.sp,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(8.dp))

            // 5. PIE: TIPO Y FACCIÓN
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.type.uppercase(),
                    color = Color.Yellow.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = card.faction.uppercase(),
                    color = getFactionColor(card.faction),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun StatBadge(text: String, color: Color) {
    Text(
        text = text,
        color = color,
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    )
}

// Función auxiliar para spacers horizontales
@Composable
fun Spacer(modifier: Modifier) {
    androidx.compose.foundation.layout.Spacer(modifier = modifier)
}

private fun getFactionColor(faction: String): Color {
    return when (faction.lowercase()) {
        "caballeros solares" -> Color(0xFFFFD700) // Dorado
        "corrupción" -> Color(0xFF9B59B6) // Púrpura
        else -> TealAccent
    }
}

// ✅ ESTA FUNCIÓN ES CRÍTICA: Asocia el texto de la URL con el ID del recurso
private fun getImageResourceFromUrl(imageUrl: String): Int {
    return when {
        // Caballeros Solares
        imageUrl.contains("espadachin_solar", ignoreCase = true) -> R.drawable.espadachin_solar
        imageUrl.contains("sacerdote_solar", ignoreCase = true) -> R.drawable.sacerdote_solar
        imageUrl.contains("porta_estandarte", ignoreCase = true) -> R.drawable.porta_estandarte
        imageUrl.contains("alabardero_real", ignoreCase = true) -> R.drawable.alabardero_real
        imageUrl.contains("heroe_solar", ignoreCase = true) -> R.drawable.heroe_solar
        imageUrl.contains("rey_paladin", ignoreCase = true) -> R.drawable.rey_paladin
        imageUrl.contains("mago_de_batalla", ignoreCase = true) -> R.drawable.mago_de_batalla
        imageUrl.contains("avatar_de_la_luz", ignoreCase = true) -> R.drawable.avatar_de_la_luz

        // Corrupción
        imageUrl.contains("acolito_putrido", ignoreCase = true) -> R.drawable.acolito_putrido
        imageUrl.contains("huevo_de_la_podredumbre", ignoreCase = true) -> R.drawable.huevo_de_la_podredumbre
        imageUrl.contains("sin_luz", ignoreCase = true) -> R.drawable.sin_luz
        imageUrl.contains("amalgama_de_putrefaccion", ignoreCase = true) -> R.drawable.amalgama_de_putrefaccion
        imageUrl.contains("neuro_amalgama", ignoreCase = true) -> R.drawable.neuro_amalgama
        imageUrl.contains("corruptores", ignoreCase = true) -> R.drawable.corruptores
        imageUrl.contains("gran_devorador", ignoreCase = true) -> R.drawable.gran_devorador
        imageUrl.contains("senor_de_la_podredumbre", ignoreCase = true) -> R.drawable.senor_de_la_podredumbre
        imageUrl.contains("gran_cancer", ignoreCase = true) -> R.drawable.gran_cancer
        imageUrl.contains("miasma_toxica", ignoreCase = true) -> R.drawable.miasma_toxica
        imageUrl.contains("quistes", ignoreCase = true) -> R.drawable.quistes
        imageUrl.contains("campo_de_corrupcion", ignoreCase = true) -> R.drawable.campo_de_corrupcion

        else -> 0
    }
}