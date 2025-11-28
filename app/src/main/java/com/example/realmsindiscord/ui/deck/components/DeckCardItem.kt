// En ui/deck/components/DeckCardItem.kt
package com.example.realmsindiscord.ui.deck.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.R
import com.example.realmsindiscord.domain.models.DeckCard
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun DeckCardItem(
    deckCard: DeckCard,
    availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>,
    onRemove: () -> Unit
) {
    // Buscar la carta real por mongoId para mostrar su nombre
    val card = availableCards.find { it.mongoId == deckCard.cardId }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black.copy(alpha = 0.7f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Información de la carta - MOSTRAR NOMBRE REAL
            Text(
                text = card?.name ?: "Carta: ${deckCard.cardId}",
                color = Color.White,
                fontSize = 14.sp,
                modifier = Modifier.weight(1f)
            )

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Contador de copias
                Text(
                    text = "x${deckCard.count}",
                    color = TealAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de eliminar
                Button(
                    onClick = onRemove,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text("X", color = Color.White, fontSize = 12.sp)
                }
            }
        }
    }
}