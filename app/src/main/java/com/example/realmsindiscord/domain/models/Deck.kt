// En domain/models/Deck.kt
package com.example.realmsindiscord.domain.models

data class Deck(
    val id: Int = 0,
    val name: String,
    val userId: Int,
    val faction: String = "Neutral",
    val cards: List<DeckCard> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val isActive: Boolean = true
)

data class DeckCard(
    val cardId: String,
    val count: Int = 1
) {
    fun isValidCount(): Boolean = count in 1..2 // CORREGIDO: número directo
}

// Reglas del juego
object DeckRules {
    const val MIN_DECK_SIZE = 25
    const val MAX_DECK_SIZE = 30
    const val MAX_COPIES_PER_CARD = 2
    const val MAX_SAME_FACTION_CARDS = 15
}

data class DeckValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList()
)