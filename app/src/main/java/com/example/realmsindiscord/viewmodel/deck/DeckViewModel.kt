package com.example.realmsindiscord.viewmodel.deck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class DeckUiState(
    val decks: List<Deck> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class Deck(
    val id: String,
    val name: String,
    val faction: String,
    val cardCount: Int
)

class DeckViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(DeckUiState())
    val uiState: StateFlow<DeckUiState> = _uiState

    // Por ahora vacío, lo implementaremos después
    fun loadDecks() {
        // Cargar mazos del usuario
    }
}