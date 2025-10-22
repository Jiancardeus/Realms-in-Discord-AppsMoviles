package com.example.actividad2.ui.library

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.actividad2.data.model.Card // Asegúrate de que esta ruta sea correcta

@Composable
fun CardItem(card: Card) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .border(2.dp, Color.Yellow, RoundedCornerShape(10.dp)),
        shape = RoundedCornerShape(10.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Sección de Costo y Nombre
            Text(text = card.name, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)

            // Imagen de la Carta (Usando el ID del recurso)
            Image(
                painter = painterResource(id = card.imageResId),
                contentDescription = card.name,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .padding(vertical = 4.dp)
            )

            // Ataque y Vida
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "ATK: ${card.attack}", color = Color.Red, fontWeight = FontWeight.Bold)
                Text(text = "HP: ${card.health}", color = Color.Green, fontWeight = FontWeight.Bold)
            }
        }
    }
}