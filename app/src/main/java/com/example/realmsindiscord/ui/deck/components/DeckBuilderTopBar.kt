// En ui/deck/components/DeckBuilderTopBar.kt
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
    onSave: () -> Unit,
    isSaveEnabled: Boolean
) {
    TopAppBar(
        title = {
            Text(
                "Constructor de Mazos",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        },
        navigationIcon = {
            IconButton(
                onClick = { /* TODO: Nuevo mazo */ }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "Nuevo Mazo",
                    tint = Color.White
                )
            }
        },
        actions = {
            Row {
                Button(
                    onClick = { /* TODO: Nuevo mazo */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    enabled = true
                ) {
                    Text("Nuevo", color = Color.Black)
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onSave,
                    enabled = isSaveEnabled,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSaveEnabled) TealAccent else Color.Gray
                    )
                ) {
                    Text("Guardar Mazo", color = Color.Black)
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black.copy(alpha = 0.9f)
        )
    )
}