package com.example.realmsindiscord.data.repository

import android.content.Context
import com.example.realmsindiscord.data.local.CardDao
import com.example.realmsindiscord.data.mapper.CardMapper
import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import kotlinx.coroutines.flow.first
import retrofit2.Response
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
            val response: Response<List<CardModel>> = cardApiService.getCards()

            if (response.isSuccessful) {
                val remoteCards = response.body() ?: emptyList()

                if (remoteCards.isNotEmpty()) {
                    println("DEBUG: Cartas obtenidas de API: ${remoteCards.size}")

                    // Guardar en base de datos local
                    val localCards = remoteCards.map { cardModel ->
                        CardMapper.toLocalCard(cardModel)
                    }
                    cardDao.insertAll(localCards)
                    remoteCards
                } else {
                    // Si la API responde OK pero una lista vacía, usamos fallback
                    println("DEBUG: API devolvió lista vacía, usando fallback")
                    getFallbackCards()
                }
            } else {
                println("DEBUG: API respondió con error: ${response.code()}")
                getFallbackCards()
            }
        } catch (e: Exception) {
            println("DEBUG: Error API, usando fallback: ${e.message}")
            getFallbackCards()
        }
    }

    private suspend fun getFallbackCards(): List<CardModel> {
        return try {
            // 3. Intentar leer de la BD local
            val localCardsEntities = cardDao.getAllCards().first()

            if (localCardsEntities.isNotEmpty()) {
                println("DEBUG: Usando cartas de BD local (${localCardsEntities.size})")
                localCardsEntities.map { localCard ->
                    CardMapper.toCardModel(localCard)
                }
            } else {
                // 4. SI LA BD ESTÁ VACÍA (Caso Reinstalación) -> CARGAR DATOS DE EJEMPLO
                println("DEBUG: BD local vacía. Cargando datos de ejemplo (Hardcoded)...")
                val sampleCards = getSampleCardsWithRealFactions()

                // Opcional: Guardar estos datos de ejemplo en la BD para que persistan
                val localSamples = sampleCards.map { CardMapper.toLocalCard(it) }
                cardDao.insertAll(localSamples)

                sampleCards
            }
        } catch (dbError: Exception) {
            println("DEBUG: Error crítico en BD local: ${dbError.message}")
            getSampleCardsWithRealFactions()
        }
    }

    private fun getSampleCardsWithRealFactions(): List<CardModel> {
        return listOf(
            // --- CABALLEROS SOLARES ---
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
                health = 40,
                faction = "Caballeros Solares",
                imageUrl = "porta_estandarte"
            ),
            CardModel(
                mongoId = "CS005",
                name = "Héroe Solar",
                type = "Líder",
                description = "Unidad de ataque balanceada.",
                cost = 0,
                attack = 4,
                defense = 0,
                health = 40,
                faction = "Caballeros Solares",
                imageUrl = "heroe_solar"
            ),

            // --- CORRUPCIÓN ---
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
                mongoId = "CC009",
                name = "Gran Cáncer",
                type = "Líder",
                description = "Por cada carta aliada muerta genera un sin luz.",
                cost = 0,
                attack = 3,
                defense = 0,
                health = 30,
                faction = "Corrupción",
                imageUrl = "gran_cancer"
            ),
            CardModel(
                mongoId = "CC012",
                name = "Campo de Corrupción",
                type = "Orden",
                description = "Roba 5 cartas del mazo y escoge una.",
                cost = 2,
                attack = 0,
                defense = 0,
                health = 0,
                faction = "Corrupción",
                imageUrl = "campo_de_corrupcion"
            )
        )
    }
}