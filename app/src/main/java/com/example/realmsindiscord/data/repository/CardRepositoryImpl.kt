package com.example.realmsindiscord.data.repository

import android.content.Context
import com.example.realmsindiscord.data.local.CardDao
import com.example.realmsindiscord.data.mapper.CardMapper
import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CardRepositoryImpl @Inject constructor(
    private val cardApiService: CardApiService,
    private val cardDao: CardDao,
    private val context: Context
) : ICardRepository {

    override suspend fun getCards(): List<CardModel> {
        return try {
            // 1. Intentar obtener de API
            println("DEBUG: Obteniendo cartas de la API...")
            val remoteCards = cardApiService.getCards()
            println("DEBUG: Cartas obtenidas de API: ${remoteCards.size}")

            // 2. Guardar en base de datos local
            val localCards = remoteCards.map { cardModel ->
                CardMapper.toLocalCard(cardModel) // ✅ LLAMAR AL MAPPER CORRECTAMENTE
            }
            cardDao.insertAll(localCards)

            remoteCards
        } catch (e: Exception) {
            println("DEBUG: Error API, usando fallback: ${e.message}")
            // 3. Fallback: obtener de base de datos local
            try {
                val localCards = cardDao.getAllCards().first() // ✅ AGREGAR .first()
                println("DEBUG: Cartas obtenidas de BD local: ${localCards.size}")
                localCards.map { localCard ->
                    CardMapper.toCardModel(localCard) // ✅ LLAMAR AL MAPPER CORRECTAMENTE
                }
            } catch (dbError: Exception) {
                println("DEBUG: Error BD local, usando datos de ejemplo")
                // 4. Último fallback: datos de ejemplo con tus facciones reales
                getSampleCardsWithRealFactions()
            }
        }
    }

    private fun getSampleCardsWithRealFactions(): List<CardModel> {
        return listOf(
            // CARTAS SOLARES REALES
            CardModel(
                mongoId = "CS001",
                name = "Espadachín Solar",
                type = "Tropa",
                description = "Tropa Comun",
                cost = 2,
                attack = 3,
                defense = 0,
                health = 2,
                faction = "Caballeros Solares",
                imageUrl = "espadachin_solar"
            ),
            CardModel(
                mongoId = "CS002",
                name = "Sacerdote Solar",
                type = "Tropa",
                description = "Al final de cada turno, helea +1 PS a la tropa más cercana",
                cost = 3,
                attack = 1,
                defense = 0,
                health = 4,
                faction = "Caballeros Solares",
                imageUrl = "sacerdote_solar"
            ),
            CardModel(
                mongoId = "CS003",
                name = "Porta Estandarte",
                type = "Tropa",
                description = "Tropas cercanas obtienen +1 AD",
                cost = 4,
                attack = 3,
                defense = 0,
                health = 4,
                faction = "Caballeros Solares",
                imageUrl = "porta_estandarte"
            ),

            // CARTAS DE CORRUPCIÓN REALES
            CardModel(
                mongoId = "CC001",
                name = "Acólito Pútrido",
                type = "Tropa",
                description = "Al morir, inflige Silencio a una carta del rival.",
                cost = 1,
                attack = 2,
                defense = 0,
                health = 1,
                faction = "Corrupción",
                imageUrl = "acolito_putrido"
            ),
            CardModel(
                mongoId = "CC002",
                name = "Huevo de la Podredumbre",
                type = "Tropa",
                description = "Un contador de 2 turnos que al llegar a 0 esta carta se destruye y agrega 1 maná máximo.",
                cost = 1,
                attack = 0,
                defense = 0,
                health = 5,
                faction = "Corrupción",
                imageUrl = "huevo_de_la_podredumbre"
            ),
            CardModel(
                mongoId = "CC003",
                name = "Sin Luz",
                type = "Tropa",
                description = "Una criatura vacía, un heraldo del fin.",
                cost = 2,
                attack = 3,
                defense = 0,
                health = 1,
                faction = "Corrupción",
                imageUrl = "sin_luz"
            )
        )
    }
}