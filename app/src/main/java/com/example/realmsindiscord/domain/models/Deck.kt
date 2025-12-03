package com.example.realmsindiscord.domain.models

data class Deck(
    val id: String = "",
    val name: String,
    val username: String,
    val faction: String = "Neutral",
    val cards: List<DeckCard> = emptyList(),
    val isActive: Boolean = true
)

data class DeckCard(
    val cardId: String,
    val count: Int = 1
) {

    fun isValidCount(): Boolean = count in 1..3
}

// Las reglas se mantienen igual
object DeckRules {
    const val MIN_DECK_SIZE = 22
    const val MAX_DECK_SIZE = 22
    const val MAX_COPIES_PER_CARD = 3
    const val MAX_HERO_COPIES = 1

    val FACTION_HEROES = mapOf(
        "Caballeros Solares" to "Héroe Solar",
        "Corrupción" to "Gran Cáncer"
    )

    val RIVAL_FACTIONS = listOf(
        setOf("Caballeros Solares", "Corrupción")
    )
}

data class DeckValidationResult(
    val isValid: Boolean,
    val errors: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)