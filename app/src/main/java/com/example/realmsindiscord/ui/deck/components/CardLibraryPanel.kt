package com.example.realmsindiscord.ui.deck.components

import androidx.compose.foundation.layout.Arrangement
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
fun CardLibraryPanel(
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
            .padding(16.dp)
    ) {
        // BARRA DE BÚSQUEDA
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            label = { Text("Buscar cartas...", color = Color.White) },
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
            "Filtros por Facción",
            color = TealAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // BOTONES DE FACCIONES
        FactionFilterRow(
            selectedFaction = selectedFaction,
            onFactionSelect = onFactionSelect
        )

        Spacer(modifier = Modifier.height(16.dp))

        // CONTADOR DE CARTAS
        Text(
            "Cartas disponibles: ${cards.size}",
            color = Color.White,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LISTA DE CARTAS (LazyColumn en lugar de LazyVerticalGrid)
        if (cards.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(cards) { card ->
                    LibraryCardItem(
                        card = card,
                        onClick = { onCardClick(card) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "No se encontraron cartas",
                    color = Color.Gray,
                    fontSize = 14.sp
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
    val factions = listOf("Todas", "Solares", "Corrupcion", "Earth", "Air", "Neutral")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
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
                        text = faction,
                        color = if (selectedFaction == faction) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}