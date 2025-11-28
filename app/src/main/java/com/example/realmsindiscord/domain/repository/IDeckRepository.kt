// En domain/repository/IDeckRepository.kt
package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckValidationResult
import kotlinx.coroutines.flow.Flow

interface IDeckRepository {
    suspend fun getUserDecks(userId: Int): List<Deck>
    suspend fun getDeckById(deckId: Int): Deck?
    suspend fun createDeck(deck: Deck): Boolean
    suspend fun updateDeck(deck: Deck): Boolean
    suspend fun deleteDeck(deckId: Int): Boolean
    suspend fun validateDeck(deck: Deck): DeckValidationResult
}