package com.example.realmsindiscord

import com.example.realmsindiscord.data.mapper.CardMapper
import com.example.realmsindiscord.data.remote.model.CardModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CardMapperTest {

    @Test
    fun `toLocalCard maps image url to correct resource id`() {
        // Arrange: Simulamos una carta que viene de la API (Backend)
        val remoteCard = CardModel(
            mongoId = "123",
            name = "Espadachín Test",
            type = "Tropa",
            description = "Desc",
            attack = 1,
            defense = 0,
            health = 1,
            cost = 1,
            faction = "Solares",
            imageUrl = "espadachin_solar" // El string clave
        )

        // Act: La convertimos a carta local
        val localCard = CardMapper.toLocalCard(remoteCard)

        // Assert: Verificamos que el ID sea el del recurso R.drawable
        // Nota: R.drawable.espadachin_solar es un entero (Int).
        assertEquals(R.drawable.espadachin_solar, localCard.imageResId)
    }

    @Test
    fun `toLocalCard maps unknown image to 0`() {
        val remoteCard = CardModel(
            mongoId = "999", name = "Rara", type = "", description = "", health = 1, faction = "",
            imageUrl = "imagen_que_no_existe"
        )

        val localCard = CardMapper.toLocalCard(remoteCard)

        assertEquals(0, localCard.imageResId)
    }
}