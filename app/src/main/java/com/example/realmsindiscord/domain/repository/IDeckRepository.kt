package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckValidationResult

interface IDeckRepository {
    // Cambiamos Int por String para compatibilidad con MongoDB
    suspend fun getUserDecks(userId: Int): List<Deck> // Mantenemos firma por compatibilidad, aunque no use el Int
    suspend fun getDeckById(deckId: String): Deck?
    suspend fun createDeck(deck: Deck): Boolean
    suspend fun updateDeck(deck: Deck): Boolean
    suspend fun deleteDeck(deckId: String): Boolean

    suspend fun validateDeck(deck: Deck): DeckValidationResult
    suspend fun validateDeckWithCardDetails(
        deck: Deck,
        availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>
    ): DeckValidationResult
}