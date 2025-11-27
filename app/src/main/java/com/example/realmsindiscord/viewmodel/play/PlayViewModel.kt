package com.example.realmsindiscord.viewmodel.play

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class PlayUiState(
    val selectedDeck: String? = null,
    val isLoading: Boolean = false
)

class PlayViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PlayUiState())
    val uiState: StateFlow<PlayUiState> = _uiState

    // Por ahora vacío, lo implementaremos después
    fun selectDeck(deckId: String) {
        _uiState.value = _uiState.value.copy(selectedDeck = deckId)
    }
}