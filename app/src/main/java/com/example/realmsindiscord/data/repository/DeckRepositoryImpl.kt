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
            println("DEBUG: Mazo creado - ID: ${newDeck.id}, Cartas: ${newDeck.cards.size}")
            true
        } catch (e: Exception) {
            println("DEBUG: Error creando mazo: ${e.message}")
            false
        }
    }

    override suspend fun updateDeck(deck: Deck): Boolean {
        return try {
            val index = userDecks.indexOfFirst { it.id == deck.id }
            if (index != -1) {
                userDecks[index] = deck
                println("DEBUG: Mazo actualizado - ID: ${deck.id}, Cartas: ${deck.cards.size}")
                true
            } else {
                false
            }
        } catch (e: Exception) {
            println("DEBUG: Error actualizando mazo: ${e.message}")
            false
        }
    }

    override suspend fun deleteDeck(deckId: Int): Boolean {
        return try {
            val removed = userDecks.removeAll { it.id == deckId }
            println("DEBUG: Mazo eliminado - ID: $deckId, Éxito: $removed")
            removed
        } catch (e: Exception) {
            println("DEBUG: Error eliminando mazo: ${e.message}")
            false
        }
    }

    override suspend fun validateDeck(deck: Deck): DeckValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Calcular total de cartas
        val totalCards = deck.cards.sumOf { it.count }
        println("DEBUG: Validando mazo - Total cartas: $totalCards, Cartas únicas: ${deck.cards.size}")

        // 1. Validar tamaño del mazo
        if (totalCards > 0) {
            if (totalCards < DeckRules.MIN_DECK_SIZE) {
                warnings.add("El mazo debe tener al menos ${DeckRules.MIN_DECK_SIZE} cartas (actual: $totalCards)")
            }
            if (totalCards > DeckRules.MAX_DECK_SIZE) {
                errors.add("El mazo no puede exceder ${DeckRules.MAX_DECK_SIZE} cartas (actual: $totalCards)")
            }
        } else {
            warnings.add("El mazo está vacío. Agrega al menos ${DeckRules.MIN_DECK_SIZE} cartas")
        }

        // 2. Validar copias por carta
        deck.cards.forEach { deckCard ->
            if (deckCard.count > DeckRules.MAX_COPIES_PER_CARD) {
                errors.add("Máximo ${DeckRules.MAX_COPIES_PER_CARD} copias de cada carta permitidas")
            }
        }

        // 3. Validar nombre del mazo
        if (deck.name.isBlank() || deck.name.startsWith("Nuevo Mazo")) {
            warnings.add("Asigna un nombre personalizado a tu mazo")
        }

        // 4. Validar facción
        if (deck.faction.isBlank() || deck.faction == "Neutral") {
            warnings.add("Selecciona una facción para tu mazo")
        }

        // 5. NUEVA VALIDACIÓN: Verificar si tenemos los detalles de las cartas
        // (Esta validación se completará cuando tengamos acceso a los detalles de las cartas)
        println("DEBUG: Validación básica completada - Errores: ${errors.size}, Advertencias: ${warnings.size}")

        return DeckValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }

    override suspend fun validateDeckWithCardDetails(
        deck: Deck,
        availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel>
    ): DeckValidationResult {
        val errors = mutableListOf<String>()
        val warnings = mutableListOf<String>()

        // Calcular total de cartas
        val totalCards = deck.cards.sumOf { it.count }
        println("DEBUG: Validación avanzada - Total cartas: $totalCards")

        // 1. Validar tamaño del mazo
        if (totalCards > 0) {
            if (totalCards < DeckRules.MIN_DECK_SIZE) {
                warnings.add("El mazo debe tener al menos ${DeckRules.MIN_DECK_SIZE} cartas (actual: $totalCards)")
            }
            if (totalCards > DeckRules.MAX_DECK_SIZE) {
                errors.add("El mazo no puede exceder ${DeckRules.MAX_DECK_SIZE} cartas (actual: $totalCards)")
            }
        }

        // 2. Validar copias por carta y obtener detalles de las cartas
        val cardDetailsMap = mutableMapOf<String, com.example.realmsindiscord.data.remote.model.CardModel>()
        val factionCards = mutableListOf<String>()
        val heroCards = mutableListOf<String>()

        deck.cards.forEach { deckCard ->
            // Buscar detalles de la carta
            val cardDetail = availableCards.find { it.mongoId == deckCard.cardId }
            if (cardDetail != null) {
                cardDetailsMap[deckCard.cardId] = cardDetail
                factionCards.add(cardDetail.faction)

                // Identificar héroes
                if (cardDetail.type == "Líder" || cardDetail.name.contains("Héroe") ||
                    cardDetail.name == "Gran Cáncer") {
                    heroCards.add(cardDetail.name)
                }
            }

            // Validar copias
            if (deckCard.count > DeckRules.MAX_COPIES_PER_CARD) {
                val cardName = cardDetail?.name ?: "Carta desconocida"
                errors.add("Máximo ${DeckRules.MAX_COPIES_PER_CARD} copias de '$cardName' permitidas")
            }
        }

        // 3. NUEVA VALIDACIÓN: No mezclar facciones rivales
        val uniqueFactions = factionCards.distinct()
        if (uniqueFactions.size > 1) {
            // Verificar si hay facciones rivales
            val hasRivalFactions = DeckRules.RIVAL_FACTIONS.any { rivalSet ->
                uniqueFactions.count { it in rivalSet } > 1
            }

            if (hasRivalFactions) {
                errors.add("No puedes mezclar cartas de facciones rivales: ${uniqueFactions.joinToString(", ")}")
            }
        }

        // 4. NUEVA VALIDACIÓN: Verificar héroe de facción
        if (deck.faction != "Neutral" && deck.faction.isNotBlank()) {
            val requiredHero = DeckRules.FACTION_HEROES[deck.faction]
            val hasRequiredHero = heroCards.any { heroName ->
                when (deck.faction) {
                    "Caballeros Solares" -> heroName == "Héroe Solar"
                    "Corrupción" -> heroName == "Gran Cáncer"
                    else -> false
                }
            }

            if (!hasRequiredHero && requiredHero != null) {
                errors.add("El mazo de $deck.faction debe incluir al héroe: $requiredHero")
            }
        }

        // 5. NUEVA VALIDACIÓN: Máximo 1 copia de héroe
        deck.cards.forEach { deckCard ->
            val cardDetail = cardDetailsMap[deckCard.cardId]
            if (cardDetail != null &&
                (cardDetail.type == "Líder" || cardDetail.name.contains("Héroe") ||
                        cardDetail.name == "Gran Cáncer")) {
                if (deckCard.count > DeckRules.MAX_HERO_COPIES) {
                    errors.add("Solo puedes tener 1 copia del héroe: ${cardDetail.name}")
                }
            }
        }

        // 6. Validación de nombre
        if (deck.name.isBlank() || deck.name.startsWith("Nuevo Mazo")) {
            warnings.add("Asigna un nombre personalizado a tu mazo")
        }

        println("DEBUG: Validación avanzada completada - Errores: ${errors.size}, Advertencias: ${warnings.size}")

        return DeckValidationResult(
            isValid = errors.isEmpty(),
            errors = errors,
            warnings = warnings
        )
    }
}