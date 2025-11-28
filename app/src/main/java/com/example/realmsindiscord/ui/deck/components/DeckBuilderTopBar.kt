package com.example.realmsindiscord.ui.deck.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.realmsindiscord.R
import com.example.realmsindiscord.ui.theme.TealAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderTopBar(
    onBack: () -> Unit,
    onNewDeck: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean
) {
    println("DEBUG: TopBar - isSaveEnabled: $isSaveEnabled") // Agrega este log

    TopAppBar(
        title = {
            Text(
                "Constructor de Mazos",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }
        },
        actions = {
            Row {
                Button(
                    onClick = onNewDeck,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50) // Verde
                    )
                ) {
                    Text("Nuevo", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSave,
                    enabled = isSaveEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveEnabled) TealAccent else Color.Gray
                    )
                ) {
                    Text(
                        "Guardar Mazo",
                        color = if (isSaveEnabled) Color.Black else Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}