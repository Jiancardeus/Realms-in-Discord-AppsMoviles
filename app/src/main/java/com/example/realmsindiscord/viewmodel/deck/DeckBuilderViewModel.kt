// En viewmodel/deck/DeckBuilderViewModel.kt - VERSIÓN CORREGIDA
package com.example.realmsindiscord.viewmodel.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

@HiltViewModel
class DeckBuilderViewModel @Inject constructor(
    private val deckRepository: IDeckRepository,
    private val cardRepository: ICardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DeckBuilderState())
    val state: StateFlow<DeckBuilderState> = _state.asStateFlow()

    private val currentUserId = 1 // Temporal

    init {
        loadAvailableCards()
        createNewDeck()
    }

    fun loadAvailableCards() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val cards = cardRepository.getCards()
                _state.value = _state.value.copy(
                    availableCards = cards,
                    filteredCards = cards,
                    isLoading = false
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = "Error al cargar cartas: ${e.message}",
                    isLoading = false
                )
            }
        }
    }

    fun createNewDeck() {
        val newDeck = Deck(
            name = "Nuevo Mazo",
            userId = currentUserId,
            faction = _state.value.selectedFaction ?: "Neutral"
        )
        _state.value = _state.value.copy(currentDeck = newDeck)
    }

    fun addCardToDeck(card: com.example.realmsindiscord.data.remote.model.CardModel) {
        val currentDeck = _state.value.currentDeck ?: return

        val updatedCards = currentDeck.cards.toMutableList()
        // USAR mongoId EN LUGAR DE id
        val existingCardIndex = updatedCards.indexOfFirst { it.cardId == card.mongoId }

        if (existingCardIndex >= 0) {
            val existingCard = updatedCards[existingCardIndex]
            if (existingCard.count < 2) {
                updatedCards[existingCardIndex] = existingCard.copy(count = existingCard.count + 1)
            } else {
                _state.value = _state.value.copy(error = "Máximo 2 copias por carta")
                return
            }
        } else {
            // USAR mongoId EN LUGAR DE id
            updatedCards.add(DeckCard(cardId = card.mongoId))
        }

        val updatedDeck = currentDeck.copy(cards = updatedCards)
        _state.value = _state.value.copy(currentDeck = updatedDeck)
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
        }

        val updatedDeck = currentDeck.copy(cards = updatedCards)
        _state.value = _state.value.copy(currentDeck = updatedDeck)
        validateDeck()
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
        }
    }

    fun setSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        filterCards()
    }

    private fun filterCards() {
        val cards = _state.value.availableCards
        var filtered = cards

        _state.value.selectedFaction?.let { faction ->
            if (faction != "Todas") {
                filtered = filtered.filter { it.faction == faction }
            }
        }

        if (_state.value.searchQuery.isNotBlank()) {
            filtered = filtered.filter { card ->
                card.name.contains(_state.value.searchQuery, ignoreCase = true) ||
                        card.description.contains(_state.value.searchQuery, ignoreCase = true)
            }
        }

        _state.value = _state.value.copy(filteredCards = filtered)
    }

    private fun validateDeck() {
        val currentDeck = _state.value.currentDeck ?: return
        viewModelScope.launch {
            val result = deckRepository.validateDeck(currentDeck)
            _state.value = _state.value.copy(validationResult = result)
        }
    }

    fun saveDeck() {
        val currentDeck = _state.value.currentDeck ?: return

        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val success = if (currentDeck.id == 0) {
                    deckRepository.createDeck(currentDeck)
                } else {
                    deckRepository.updateDeck(currentDeck)
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    successMessage = if (success) "Mazo guardado correctamente" else "Error al guardar mazo",
                    error = if (!success) "Error al guardar mazo" else null
                )
            } catch (e: Exception) {
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
}