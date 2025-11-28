// En ui/deck/components/DeckPanel.kt - VERSIÓN CORREGIDA
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
import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckValidationResult
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun DeckPanel(
    deck: Deck?,
    availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>, // AGREGADO
    validationResult: DeckValidationResult?,
    onRemoveCard: (String) -> Unit,
    onDeckNameChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // NOMBRE DEL MAZO
        OutlinedTextField(
            value = deck?.name ?: "",
            onValueChange = onDeckNameChange,
            label = { Text("Nombre del Mazo", color = Color.White) },
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

        // ESTADÍSTICAS DEL MAZO
        DeckStats(deck = deck, validationResult = validationResult)

        Spacer(modifier = Modifier.height(16.dp))

        // LISTA DE CARTAS EN EL MAZO
        val totalCards = deck?.cards?.sumOf { it.count } ?: 0
        Text(
            "Cartas en el Mazo ($totalCards/30)",
            color = TealAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // LISTA DE CARTAS
        if (deck?.cards?.isNotEmpty() == true) {
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(deck.cards, key = { it.cardId }) { deckCard ->
                    DeckCardItem(
                        deckCard = deckCard,
                        availableCards = availableCards, // PASAR availableCards
                        onRemove = { onRemoveCard(deckCard.cardId) }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
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
                    "No hay cartas en el mazo",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        // MENSAJES DE VALIDACIÓN
        validationResult?.errors?.forEach { error ->
            Text(
                text = "• $error",
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        }
    }
}

@Composable
private fun DeckStats(
    deck: Deck?,
    validationResult: DeckValidationResult?
) {
    val totalCards = deck?.cards?.sumOf { it.count } ?: 0
    val isValid = validationResult?.isValid ?: false

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isValid) Color.Green.copy(alpha = 0.1f)
            else Color.Red.copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                "Estadísticas del Mazo",
                color = TealAccent,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Total Cartas:", color = Color.White)
                Text("$totalCards/30",
                    color = if (totalCards in 25..30) Color.Green else Color.Red)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Cartas Únicas:", color = Color.White)
                Text("${deck?.cards?.size ?: 0}", color = TealAccent)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Estado:", color = Color.White)
                Text(
                    if (isValid) "VÁLIDO" else "INVÁLIDO",
                    color = if (isValid) Color.Green else Color.Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}