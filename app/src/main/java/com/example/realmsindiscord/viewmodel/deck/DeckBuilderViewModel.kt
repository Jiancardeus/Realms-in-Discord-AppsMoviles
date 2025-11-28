package com.example.realmsindiscord.viewmodel.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.data.local.SessionManager
import com.example.realmsindiscord.domain.models.Deck
import com.example.realmsindiscord.domain.models.DeckCard
import com.example.realmsindiscord.domain.models.DeckValidationResult
import com.example.realmsindiscord.domain.repository.ICardRepository
import com.example.realmsindiscord.domain.repository.IDeckRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeckBuilderViewModel @Inject constructor(
    private val deckRepository: IDeckRepository,
    private val cardRepository: ICardRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(DeckBuilderState())
    val state: StateFlow<DeckBuilderState> = _state.asStateFlow()

    private var currentUserId: Int = 1

    init {
        println("DEBUG: Inicializando DeckBuilderViewModel")
        loadCurrentUser()
        loadAvailableCards()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val username = sessionManager.getCurrentUsername()
                currentUserId = 1
                println("DEBUG: Usuario cargado: $username, ID: $currentUserId")
            } catch (e: Exception) {
                println("DEBUG: Error cargando usuario: ${e.message}")
                currentUserId = 1
            }
        }
    }

    fun loadAvailableCards() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                println("DEBUG: Cargando cartas disponibles...")
                val cards = cardRepository.getCards()
                println("DEBUG: ✅ Cartas cargadas: ${cards.size}")
                println("DEBUG: Primeras 3 cartas: ${cards.take(3).map { it.name }}")

                _state.value = _state.value.copy(
                    availableCards = cards,
                    filteredCards = cards, // Asegurar que filteredCards tenga las mismas cartas
                    isLoading = false
                )

                // Solo crear mazo si no existe uno actual
                if (_state.value.currentDeck == null) {
                    createNewDeck()
                } else {
                    validateDeck()
                }
            } catch (e: Exception) {
                println("DEBUG: ❌ Error cargando cartas: ${e.message}")
                _state.value = _state.value.copy(
                    error = "Error al cargar cartas: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun createNewDeck() {
        val newDeck = Deck(
            name = "Nuevo Mazo ${System.currentTimeMillis() % 10000}",
            userId = currentUserId,
            faction = "Neutral",
            cards = emptyList()
        )
        _state.value = _state.value.copy(
            currentDeck = newDeck,
            error = null,
            successMessage = "✅ Nuevo mazo creado"
        )
        println("DEBUG: Nuevo mazo creado: ${newDeck.name}")
        println("DEBUG: Cartas en mazo: ${newDeck.cards.size}, Total: ${newDeck.cards.sumOf { it.count }}")
        validateDeck()
    }

    fun addCardToDeck(card: com.example.realmsindiscord.data.remote.model.CardModel) {
        val currentDeck = _state.value.currentDeck ?: return

        // NUEVA VALIDACIÓN: Verificar si es un héroe antes de agregar
        val isHero = card.type == "Líder" || card.name.contains("Héroe") || card.name == "Gran Cáncer"

        if (isHero) {
            // Verificar si ya existe un héroe en el mazo
            val existingHero = currentDeck.cards.find { deckCard ->
                val existingCard = _state.value.availableCards.find { it.mongoId == deckCard.cardId }
                existingCard != null && (
                        existingCard.type == "Líder" ||
                                existingCard.name.contains("Héroe") ||
                                existingCard.name == "Gran Cáncer"
                        )
            }

            if (existingHero != null) {
                _state.value = _state.value.copy(error = "Solo puedes tener 1 héroe por mazo")
                return
            }
        }

        val updatedCards = currentDeck.cards.toMutableList()
        val existingCardIndex = updatedCards.indexOfFirst { it.cardId == card.mongoId }

        if (existingCardIndex >= 0) {
            val existingCard = updatedCards[existingCardIndex]

            // Validar límite de copias (3 para cartas normales, 1 para héroes)
            val maxCopies = if (isHero) 1 else 3

            if (existingCard.count < maxCopies) {
                updatedCards[existingCardIndex] = existingCard.copy(count = existingCard.count + 1)
                println("DEBUG: Incrementada copia de ${card.name}, ahora: ${existingCard.count + 1}")
            } else {
                _state.value = _state.value.copy(error = "Máximo $maxCopies copias por carta permitidas")
                return
            }
        } else {
            updatedCards.add(DeckCard(cardId = card.mongoId, count = 1))
            println("DEBUG: Agregada nueva carta: ${card.name}")
        }

        val updatedDeck = currentDeck.copy(cards = updatedCards)
        _state.value = _state.value.copy(
            currentDeck = updatedDeck,
            error = null
        )

        val totalCardsAfter = updatedDeck.cards.sumOf { it.count }
        println("DEBUG: addCardToDeck - total cartas después: $totalCardsAfter")

        validateDeck()
    }

    fun removeCardFromDeck(cardId: String) {
        val currentDeck = _state.value.currentDeck ?: return

        val updatedCards = currentDeck.cards.toMutableList()
        val existingCardIndex = updatedCards.indexOfFirst { it.cardId == cardId }

        if (existingCardIndex >= 0) {
            val existingCard = updatedCards[existingCardIndex]
            if (existingCard.count > 1) {
                updatedCards[existingCardIndex] = existingCard.copy(count = existingCard.count - 1)
            } else {
                updatedCards.removeAt(existingCardIndex)
            }

            val updatedDeck = currentDeck.copy(cards = updatedCards)
            _state.value = _state.value.copy(
                currentDeck = updatedDeck,
                error = null
            )
            validateDeck()
        }
    }

    fun setDeckName(name: String) {
        val currentDeck = _state.value.currentDeck ?: return
        _state.value = _state.value.copy(
            currentDeck = currentDeck.copy(name = name)
        )
        validateDeck()
    }

    fun setSelectedFaction(faction: String?) {
        _state.value = _state.value.copy(selectedFaction = faction)
        filterCards()

        val currentDeck = _state.value.currentDeck
        if (currentDeck != null && faction != null) {
            _state.value = _state.value.copy(
                currentDeck = currentDeck.copy(faction = faction)
            )
            validateDeck()
        }
    }

    fun setSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        filterCards()
    }

    private fun filterCards() {
        val cards = _state.value.availableCards
        println("DEBUG: filterCards - availableCards: ${cards.size}")

        var filtered = cards

        // Filtrar por facción
        _state.value.selectedFaction?.let { faction ->
            println("DEBUG: Filtrando por facción: $faction")
            if (faction != "Todas") {
                filtered = filtered.filter { it.faction == faction }
                println("DEBUG: Después de filtrar por facción: ${filtered.size}")
            }
        }

        // Filtrar por búsqueda
        if (_state.value.searchQuery.isNotBlank()) {
            println("DEBUG: Filtrando por búsqueda: ${_state.value.searchQuery}")
            filtered = filtered.filter { card ->
                card.name.contains(_state.value.searchQuery, ignoreCase = true) ||
                        card.description.contains(_state.value.searchQuery, ignoreCase = true) ||
                        card.type.contains(_state.value.searchQuery, ignoreCase = true)
            }
            println("DEBUG: Después de filtrar por búsqueda: ${filtered.size}")
        }

        _state.value = _state.value.copy(filteredCards = filtered)
        println("DEBUG: filterCards - filteredCards final: ${filtered.size}")
    }

    private fun validateDeck() {
        val currentDeck = _state.value.currentDeck ?: return
        viewModelScope.launch {
            try {
                // Usar la validación avanzada que incluye detalles de cartas
                val result = deckRepository.validateDeckWithCardDetails(
                    currentDeck,
                    _state.value.availableCards
                )
                _state.value = _state.value.copy(validationResult = result)

                val totalCards = currentDeck.cards.sumOf { it.count }
                println("DEBUG: validateDeck - totalCards: $totalCards, errors: ${result.errors.size}, isValid: ${result.isValid}")
                println("DEBUG: validateDeck - canSave condition: ${result.errors.isEmpty() && totalCards > 0}")

            } catch (e: Exception) {
                println("DEBUG: Error validando mazo: ${e.message}")
                _state.value = _state.value.copy(
                    error = "Error validando mazo: ${e.message}"
                )
            }
        }
    }

    fun saveDeck() {
        val currentDeck = _state.value.currentDeck ?: return

        println("DEBUG: saveDeck() llamado - mazo: ${currentDeck.name}, cartas: ${currentDeck.cards.size}")

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                println("DEBUG: Intentando guardar mazo...")

                val success = if (currentDeck.id == 0) {
                    deckRepository.createDeck(currentDeck)
                } else {
                    deckRepository.updateDeck(currentDeck)
                }

                println("DEBUG: Resultado del guardado: $success")

                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = if (success) "✅ Mazo guardado correctamente" else "❌ Error al guardar mazo",
                    error = if (!success) "Error al guardar mazo" else null
                )

                if (success) {
                    println("DEBUG: Mazo guardado exitosamente, recargando cartas...")
                    loadAvailableCards()
                }
            } catch (e: Exception) {
                println("DEBUG: Excepción en saveDeck: ${e.message}")
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(
            error = null,
            successMessage = null
        )
    }



    fun getCardName(cardId: String): String {
        return _state.value.availableCards.find { it.mongoId == cardId }?.name ?: "Carta Desconocida"
    }

    fun getCardDetails(cardId: String): com.example.realmsindiscord.data.remote.model.CardModel? {
        return _state.value.availableCards.find { it.mongoId == cardId }
    }
}

data class DeckBuilderState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val currentDeck: Deck? = null,
    val availableCards: List<com.example.realmsindiscord.data.remote.model.CardModel> = emptyList(),
    val filteredCards: List<com.example.realmsindiscord.data.remote.model.CardModel> = emptyList(),
    val selectedFaction: String? = null,
    val searchQuery: String = "",
    val validationResult: DeckValidationResult? = null
)