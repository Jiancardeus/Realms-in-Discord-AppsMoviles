package com.example.realmsindiscord.ui.library

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.realmsindiscord.ui.theme.TealAccent

@Composable
fun FactionSelectorScreen(
    currentFaction: String,
    onSelectFaction: (String) -> Unit,
    onGoBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SELECCIONA UNA FACCIÓN",
            color = TealAccent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { onSelectFaction("Caballeros Solares") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFaction == "Caballeros Solares") TealAccent else Color.Gray
                )
            ) {
                Text("CABALLEROS SOLARES", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onSelectFaction("Corrupción") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFaction == "Corrupción") TealAccent else Color.Gray
                )
            ) {
                Text("CORRUPCIÓN", color = Color.Black, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = { onSelectFaction("Todas") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (currentFaction == "Todas") TealAccent else Color.Gray
                )
            ) {
                Text("TODAS", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}