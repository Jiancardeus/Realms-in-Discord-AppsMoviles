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
            text = "FILTRAR POR FACCIÓN",
            color = TealAccent,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FactionButton(
                text = "SOLARES",
                isSelected = currentFaction == "Caballeros Solares",
                onClick = { onSelectFaction("Caballeros Solares") },
                selectedColor = Color(0xFFFFD700)
            )

            FactionButton(
                text = "CORRUPCIÓN",
                isSelected = currentFaction == "Corrupción",
                onClick = { onSelectFaction("Corrupción") },
                selectedColor = Color(0xFF9B59B6)
            )

            FactionButton(
                text = "TODAS",
                isSelected = currentFaction == "Todas",
                onClick = { onSelectFaction("Todas") },
                selectedColor = TealAccent
            )
        }
    }
}

@Composable
fun FactionButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    selectedColor: Color
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) selectedColor else Color.Gray.copy(alpha = 0.3f),
            contentColor = if (isSelected) Color.Black else Color.White
        ),
        modifier = Modifier.padding(4.dp)
    ) {
        Text(
            text = text,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        )
    }
}