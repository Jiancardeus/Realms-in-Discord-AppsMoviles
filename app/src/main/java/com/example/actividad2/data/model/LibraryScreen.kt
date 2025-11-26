package com.example.actividad2.data.model

import android.content.Context
import androidx.compose.material3.Card
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.data.local.getInitialCards
import com.example.actividad2.domain.repository.ICardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Clase de estado para la Biblioteca
data class LibraryUiState(
    val cards: List<Card> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val currentFactionFilter: String = "Todas"
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        loadCards()
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val filter = _uiState.value.currentFactionFilter

            // USAR CARTAS LOCALES - MODIFICADO
            try {
                val allCards = getInitialCards()

                // Aplicar filtro
                val loadedCards = if (filter == "Todas") {
                    allCards
                } else {
                    allCards.filter { it.faction == filter }
                }

                _uiState.update { it.copy(cards = loadedCards, isLoading = false, error = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Error al cargar cartas: ${e.message}", isLoading = false) }
            }
        }
    }

    fun setFactionFilter(faction: String) {
        _uiState.update { it.copy(currentFactionFilter = faction) }
        loadCards()
    }
}