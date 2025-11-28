package com.example.realmsindiscord.viewmodel.library

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.domain.repository.ICardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LibraryState(
    val isLoading: Boolean = false,
    val cards: List<com.example.realmsindiscord.data.remote.model.CardModel> = emptyList(),
    val filteredCards: List<com.example.realmsindiscord.data.remote.model.CardModel> = emptyList(),
    val selectedFaction: String? = null,
    val searchQuery: String = "",
    val error: String? = null
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val cardRepository: ICardRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LibraryState())
    val state: StateFlow<LibraryState> = _state.asStateFlow()

    private val availableFactions = listOf("Todas", "Caballeros Solares", "Corrupción")

    init {
        loadCards()
    }

    fun loadCards() {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val cards = cardRepository.getCards()
                println("DEBUG: Cartas cargadas: ${cards.size}")
                cards.forEach { card ->
                    println("DEBUG: Carta: ${card.name} - Facción: ${card.faction}")
                }

                _state.value = _state.value.copy(
                    isLoading = false,
                    cards = cards,
                    filteredCards = cards,
                    error = null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar cartas: ${e.message}"
                )
            }
        }
    }

    fun filterCards(faction: String?) {
        _state.value = _state.value.copy(selectedFaction = faction)

        val cards = _state.value.cards
        var filtered = cards

        // Filtrar por facción
        faction?.let { selectedFaction ->
            if (selectedFaction != "Todas") {
                filtered = filtered.filter { it.faction == selectedFaction }
            }
        }

        // Filtrar por búsqueda
        val searchQuery = _state.value.searchQuery
        if (searchQuery.isNotBlank()) {
            filtered = filtered.filter { card ->
                card.name.contains(searchQuery, ignoreCase = true) ||
                        card.description.contains(searchQuery, ignoreCase = true)
            }
        }

        _state.value = _state.value.copy(filteredCards = filtered)
    }

    fun setSearchQuery(query: String) {
        _state.value = _state.value.copy(searchQuery = query)
        filterCards(_state.value.selectedFaction)
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun getAvailableFactions(): List<String> {
        return availableFactions
    }
}