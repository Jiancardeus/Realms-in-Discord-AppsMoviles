package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckValidationResult

interface IDeckRepository {
    suspend fun getUserDecks(userId: Int): List<Deck>
    suspend fun getDeckById(deckId: Int): Deck?
    suspend fun createDeck(deck: Deck): Boolean
    suspend fun updateDeck(deck: Deck): Boolean
    suspend fun deleteDeck(deckId: Int): Boolean
    suspend fun validateDeck(deck: Deck): DeckValidationResult

    // NUEVO MÉTODO
    suspend fun validateDeckWithCardDetails(
        deck: Deck,
        availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>
    ): DeckValidationResult
}