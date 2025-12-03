package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.data.local.SessionManager
import com.example.realmsindiscord.data.remote.api.DeckApiService
import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckRules
import com.example.realmsindiscord.domain.models.DeckValidationResult
import com.example.realmsindiscord.domain.repository.IDeckRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepositoryImpl @Inject constructor(
    private val apiService: DeckApiService,
    private val sessionManager: SessionManager
) : IDeckRepository {

    override suspend fun getUserDecks(userId: Int): List<Deck> {
        val username = sessionManager.getCurrentUsername() ?: return emptyList()
        return try {
            val response = apiService.getUserDecks(username)
            if (response.isSuccessful) response.body() ?: emptyList() else emptyList()
        } catch (e: Exception) {
            println("ERROR API Decks: ${e.message}")
            emptyList()
        }
    }

    override suspend fun getDeckById(deckId: String): Deck? {
        return null
    }

    override suspend fun createDeck(deck: Deck): Boolean {
        return saveOrUpdateDeck(deck)
    }

    override suspend fun updateDeck(deck: Deck): Boolean {
        return saveOrUpdateDeck(deck)
    }

    private suspend fun saveOrUpdateDeck(deck: Deck): Boolean {
        val username = sessionManager.getCurrentUsername() ?: return false
        val deckToSend = deck.copy(username = username)

        return try {
            val response = apiService.saveDeck(deckToSend)
            // Aquí verás el log de Retrofit --> POST si funciona
            response.isSuccessful
        } catch (e: Exception) {
            println("ERROR Guardando Deck: ${e.message}")
            false
        }
    }

    override suspend fun deleteDeck(deckId: String): Boolean {
        return try {
            val response = apiService.deleteDeck(deckId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    // --- VALIDACIONES (Copia tu lógica de validación aquí si la tenías separada) ---
    override suspend fun validateDeck(deck: Deck): DeckValidationResult {
        return validateDeckWithCardDetails(deck, emptyList())
    }

    override suspend fun validateDeckWithCardDetails(
        deck: Deck,
        availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>
    ): DeckValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()
        val totalCards = deck.cards.sumOf { it.count }

        if (totalCards > 0) {
            if (totalCards < DeckRules.MIN_DECK_SIZE) warnings.add("Mazo incompleto (${DeckRules.MIN_DECK_SIZE} cartas)")
            if (totalCards > DeckRules.MAX_DECK_SIZE) errors.add("Exceso de cartas (Max ${DeckRules.MAX_DECK_SIZE})")
        }

        val heroCards = availableCards.filter {
            it.type == "Líder" || it.name.contains("Héroe") || it.name == "Gran Cáncer"
        }.map { it.mongoId }

        deck.cards.forEach { deckCard ->
            if (deckCard.count > DeckRules.MAX_COPIES_PER_CARD) errors.add("Max 3 copias por carta")
            if (deckCard.cardId in heroCards && deckCard.count > 1) errors.add("Solo 1 Héroe permitido")
        }

        if (deck.name.isBlank()) warnings.add("Ponle nombre al mazo")

        return DeckValidationResult(errors.isEmpty(), errors, warnings)
    }
}