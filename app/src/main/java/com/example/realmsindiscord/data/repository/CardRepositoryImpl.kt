package com.example.realmsindiscord.data.repository

import android.content.Context
import com.example.realmsindiscord.data.local.getInitialCards
import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CardRepositoryImpl @Inject constructor(
    private val apiService: CardApiService,
    @ApplicationContext private val context: Context  // ← AÑADIR @ApplicationContext
) : ICardRepository {

    override suspend fun getAllCards(): Result<List<CardModel>> = withContext(Dispatchers.IO) {
        try {
            // Intenta obtener cartas de la API
            val cards = apiService.getCards()
            println("✅ Cartas obtenidas de API: ${cards.size}")

            // Validar que las cartas tengan faction
            val validCards = cards.filter {
                it.faction != null && it.faction.isNotBlank()
            }

            if (validCards.size < cards.size) {
                println("⚠️ ${cards.size - validCards.size} cartas sin facción filtradas")
            }

            if (validCards.isEmpty()) {
                println("⚠️ No hay cartas válidas de la API, usando fallback local")
                return@withContext getLocalCardsAsResult()
            }

            Result.success(validCards)

        } catch (e: HttpException) {
            // Error HTTP - usar datos locales
            println("❌ Error HTTP (${e.code()}), usando datos locales: ${e.message()}")
            getLocalCardsAsResult()
        } catch (e: IOException) {
            // Error de red - usar datos locales
            println("❌ Error de red, usando datos locales: ${e.message}")
            getLocalCardsAsResult()
        } catch (e: Exception) {
            // Cualquier otro error - usar datos locales
            println("❌ Error inesperado, usando datos locales: ${e.message}")
            getLocalCardsAsResult()
        }
    }

    // Método para convertir cartas locales a CardModel
    private fun getLocalCardsAsResult(): Result<List<CardModel>> {
        return try {
            val localCards = getLocalCards(context)
            val cardModels = localCards.map { localCard ->
                CardModel(
                    mongoId = localCard.id,
                    name = localCard.name,
                    type = localCard.type,
                    description = localCard.description,
                    attack = localCard.attack,
                    defense = localCard.health, // ← Mapear health local a defense del modelo
                    imageUrl = "local_${localCard.id}",
                    cost = localCard.cost,
                    health = localCard.health, // ← También asignar al campo health
                    faction = localCard.faction ?: "Neutral"
                )
            }
            println("📚 Usando ${cardModels.size} cartas locales con health correcto")
            Result.success(cardModels)
        } catch (e: Exception) {
            println("❌ Error al cargar cartas locales: ${e.message}")
            Result.failure(e)
        }
    }

    // Mantener el método original para compatibilidad
    fun getLocalCards(context: Context): List<com.example.realmsindiscord.data.model.Card> {
        return getInitialCards(context)
    }
}