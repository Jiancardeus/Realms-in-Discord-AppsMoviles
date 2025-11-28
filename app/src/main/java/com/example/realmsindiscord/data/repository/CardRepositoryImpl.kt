// En data/repository/CardRepositoryImpl.kt
package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApiService: CardApiService
) : ICardRepository {

    override suspend fun getCards(): List<CardModel> {
        return try {
            cardApiService.getCards()
        } catch (e: Exception) {
            // Fallback: datos de ejemplo si la API falla
            getSampleCards()
        }
    }

    private fun getSampleCards(): List<CardModel> {
        return listOf(
            CardModel(
                mongoId = "fire_warrior_1",
                name = "Guerrero de Fuego",
                type = "Creature",
                description = "Un guerrero elemental de fuego",
                cost = 3,
                attack = 4,
                defense = 3,
                health = 3,
                faction = "Fire",
                imageUrl = ""
            ),
            CardModel(
                mongoId = "water_mage_1",
                name = "Mago del Agua",
                type = "Creature",
                description = "Controla el poder del agua",
                cost = 4,
                attack = 2,
                defense = 5,
                health = 5,
                faction = "Water",
                imageUrl = ""
            ),
            CardModel(
                mongoId = "earth_guardian_1",
                name = "Guardián de Tierra",
                type = "Creature",
                description = "Defensor de la naturaleza",
                cost = 5,
                attack = 3,
                defense = 6,
                health = 6,
                faction = "Earth",
                imageUrl = ""
            ),
            CardModel(
                mongoId = "air_assassin_1",
                name = "Asesino del Aire",
                type = "Creature",
                description = "Veloz y letal",
                cost = 2,
                attack = 3,
                defense = 2,
                health = 2,
                faction = "Air",
                imageUrl = ""
            ),
            CardModel(
                mongoId = "neutral_healer_1",
                name = "Sanador Neutral",
                type = "Creature",
                description = "Cura a tus aliados",
                cost = 3,
                attack = 1,
                defense = 4,
                health = 4,
                faction = "Neutral",
                imageUrl = ""
            )
        )
    }
}