package com.example.realmsindiscord.ui.deck.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun CardLibraryPanelReal(
    cards: List<CardModel>,
    selectedFaction: String?,
    searchQuery: String,
    onCardClick: (CardModel) -> Unit,
    onFactionSelect: (String?) -> Unit,
    onSearchQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // CONTENIDO SUPERIOR (búsqueda y filtros)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // BARRA DE BÚSQUEDA
            OutlinedTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                label = { Text("🔍 Buscar cartas...", color = Color.White) },
                modifier = Modifier.fillMaxWidth(),
                textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TealAccent,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = TealAccent,
                    unfocusedLabelColor = Color.Gray,
                    cursorColor = TealAccent
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // FILTROS DE FACCIONES
            Text(
                "🎯 Filtrar por Facción",
                color = TealAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            FactionFilterRow(
                selectedFaction = selectedFaction,
                onFactionSelect = onFactionSelect
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CONTADOR DE CARTAS
            Text(
                "📊 Cartas disponibles: ${cards.size}",
                color = TealAccent,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // LISTA DE CARTAS
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (cards.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                ) {
                    items(cards) { card ->
                        LibraryCardItemReal(
                            card = card,
                            onClick = { onCardClick(card) }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
                ) {
                    Text(
                        "🎴",
                        fontSize = 48.sp
                    )
                    Text(
                        "No se encontraron cartas",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun LibraryCardItemReal(
    card: CardModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E1E1E)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Encabezado con nombre y costo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = card.name,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                // Costo
                Box(
                    modifier = Modifier
                        .background(TealAccent, androidx.compose.foundation.shape.CircleShape)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${card.cost}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Estadísticas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceAround
            ) {
                Text("⚔️${card.attack}", color = Color(0xFFFF6B6B), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("🛡️${card.defense}", color = Color(0xFF4FC3F7), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                Text("❤️${card.health}", color = Color(0xFF51CF66), fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Descripción
            Text(
                text = card.description,
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 12.sp,
                maxLines = 2,
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Pie con facción y tipo
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
            ) {
                Text(
                    text = card.faction,
                    color = when (card.faction) {
                        "Caballeros Solares" -> Color(0xFFFFD700)
                        "Corrupción" -> Color(0xFF9B59B6)
                        else -> TealAccent
                    },
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = card.type,
                    color = Color.Yellow,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun FactionFilterRow(
    selectedFaction: String?,
    onFactionSelect: (String?) -> Unit
) {
    val factions = listOf("Todas", "Caballeros Solares", "Corrupción")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        factions.forEach { faction ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedFaction == faction) TealAccent
                    else Color.Gray.copy(alpha = 0.3f)
                ),
                onClick = {
                    onFactionSelect(if (selectedFaction == faction) null else faction)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (faction) {
                            "Caballeros Solares" -> "Solares"
                            "Corrupción" -> "Corrupción"
                            else -> faction
                        },
                        color = if (selectedFaction == faction) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}