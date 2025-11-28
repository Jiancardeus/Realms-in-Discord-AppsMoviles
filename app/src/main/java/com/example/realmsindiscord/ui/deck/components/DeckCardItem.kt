package com.example.realmsindiscord.ui.deck.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.domain.models.DeckCard
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun DeckCardItem(
    deckCard: DeckCard,
    cardName: String,
    cardCost: Int,
    cardType: String,
    onRemove: () -> Unit,
    onAdd: (() -> Unit)? = null
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
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
            // Información de la carta
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cardName,
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Row {
                    Text(
                        text = "Costo: $cardCost",
                        color = TealAccent,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "Tipo: $cardType",
                        color = Color.Yellow,
                        fontSize = 12.sp
                    )
                }
            }

            // Controles de cantidad
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
                    Text("−", color = Color.White, fontSize = 12.sp)
                }

                // Botón de agregar (si no excede el límite y existe la función)
                if (deckCard.count < 2 && onAdd != null) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Button(
                        onClick = onAdd,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Green),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("+", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}