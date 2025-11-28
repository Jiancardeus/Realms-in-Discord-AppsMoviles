// En data/repository/DeckRepositoryImpl.kt
package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckCard
import com.example.realmsindiscord.domain.models.DeckValidationResult
import com.example.realmsindiscord.domain.models.DeckRules
import com.example.realmsindiscord.domain.repository.IDeckRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeckRepositoryImpl @Inject constructor() : IDeckRepository {

    // Por ahora usamos memoria, luego conectaremos con Room/API
    private val userDecks = mutableListOf<Deck>()
    private var nextDeckId = 1

    override suspend fun getUserDecks(userId: Int): List<Deck> {
        return userDecks.filter { it.userId == userId }
    }

    override suspend fun getDeckById(deckId: Int): Deck? {
        return userDecks.find { it.id == deckId }
    }

    override suspend fun createDeck(deck: Deck): Boolean {
        return try {
            val newDeck = deck.copy(id = nextDeckId++)
            userDecks.add(newDeck)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun updateDeck(deck: Deck): Boolean {
        return try {
            val index = userDecks.indexOfFirst { it.id == deck.id }
            if (index != -1) {
                userDecks[index] = deck
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun deleteDeck(deckId: Int): Boolean {
        return try {
            userDecks.removeAll { it.id == deckId }
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun validateDeck(deck: Deck): DeckValidationResult {
        val errors = mutableListOf<String>()

        // Validar tamaño del mazo
        val totalCards = deck.cards.sumOf { it.count }
        if (totalCards < DeckRules.MIN_DECK_SIZE) {
            errors.add("El mazo debe tener al menos ${DeckRules.MIN_DECK_SIZE} cartas")
        }
        if (totalCards > DeckRules.MAX_DECK_SIZE) {
            errors.add("El mazo no puede exceder ${DeckRules.MAX_DECK_SIZE} cartas")
        }

        // Validar copias por carta
        deck.cards.forEach { deckCard ->
            if (deckCard.count > DeckRules.MAX_COPIES_PER_CARD) {
                errors.add("Máximo ${DeckRules.MAX_COPIES_PER_CARD} copias de cada carta")
            }
        }

        // Validar facción (implementación básica)
        val factionCards = deck.cards.size // Por ahora contamos todas
        if (factionCards > DeckRules.MAX_SAME_FACTION_CARDS) {
            errors.add("Demasiadas cartas de la misma facción")
        }

        return DeckValidationResult(
            isValid = errors.isEmpty(),
            errors = errors
        )
    }
}