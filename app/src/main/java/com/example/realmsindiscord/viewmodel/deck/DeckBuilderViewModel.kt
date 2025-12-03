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

    private var currentUsername: String = ""

    init {
        loadCurrentUser()
        loadAvailableCards()
    }

    private fun loadCurrentUser() {
        val username = sessionManager.getCurrentUsername()
        if (username != null) {
            currentUsername = username
        }
    }

    fun loadAvailableCards() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, error = null)
            try {
                val cards = cardRepository.getCards()

                _state.value = _state.value.copy(
                    availableCards = cards,
                    filteredCards = cards,
                    isLoading = false
                )

                // Si no hay mazo actual, creamos uno nuevo por defecto
                if (_state.value.currentDeck == null) {
                    createNewDeck()
                } else {
                    validateDeck()
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar cartas: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun createNewDeck() {
        // Aseguramos tener el usuario actualizado
        loadCurrentUser()

        val newDeck = Deck(
            id = "", // ID vacío para indicar que es nuevo (MongoDB lo generará)
            name = "Nuevo Mazo",
            username = currentUsername, // Usamos el username real
            faction = "Neutral",
            cards = emptyList()
        )
        _state.value = _state.value.copy(
            currentDeck = newDeck,
            error = null,
            successMessage = "✅ Nuevo mazo creado"
        )
        validateDeck()
    }

    fun addCardToDeck(card: com.example.realmsindiscord.data.remote.model.CardModel) {
        val currentDeck = _state.value.currentDeck ?: return

        // Validar si es héroe
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
            val maxCopies = if (isHero) 1 else 3

            if (existingCard.count < maxCopies) {
                updatedCards[existingCardIndex] = existingCard.copy(count = existingCard.count + 1)
            } else {
                _state.value = _state.value.copy(error = "Máximo $maxCopies copias por carta permitidas")
                return
            }
        } else {
            updatedCards.add(DeckCard(cardId = card.mongoId, count = 1))
        }

        val updatedDeck = currentDeck.copy(cards = updatedCards)
        _state.value = _state.value.copy(
            currentDeck = updatedDeck,
            error = null
        )

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
        var filtered = cards

        // Filtrar por facción
        _state.value.selectedFaction?.let { faction ->
            if (faction != "Todas") {
                filtered = filtered.filter { it.faction == faction }
            }
        }

        // Filtrar por búsqueda
        if (_state.value.searchQuery.isNotBlank()) {
            filtered = filtered.filter { card ->
                card.name.contains(_state.value.searchQuery, ignoreCase = true) ||
                        card.description.contains(_state.value.searchQuery, ignoreCase = true) ||
                        card.type.contains(_state.value.searchQuery, ignoreCase = true)
            }
        }

        _state.value = _state.value.copy(filteredCards = filtered)
    }

    private fun validateDeck() {
        val currentDeck = _state.value.currentDeck ?: return
        viewModelScope.launch {
            try {
                // Usar la validación con detalles de cartas
                val result = deckRepository.validateDeckWithCardDetails(
                    currentDeck,
                    _state.value.availableCards
                )
                _state.value = _state.value.copy(validationResult = result)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error validando mazo: ${e.message}"
                )
            }
        }
    }

    fun saveDeck() {
        val currentDeck = _state.value.currentDeck ?: return

        // Actualizar username por si acaso
        loadCurrentUser()
        val deckToSave = currentDeck.copy(username = currentUsername)

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val success = if (deckToSave.id.isEmpty()) {
                    deckRepository.createDeck(deckToSave)
                } else {
                    deckRepository.updateDeck(deckToSave)
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = if (success) "✅ Mazo guardado correctamente" else "❌ Error al guardar mazo",
                    error = if (!success) "Error al guardar en el servidor" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error de conexión: ${e.message}"
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
        return _state.value.availableCards.find { it.mongoId == cardId }?.name ?: "Cargando..."
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