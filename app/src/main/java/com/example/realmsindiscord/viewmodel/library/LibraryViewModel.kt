package com.example.realmsindiscord.viewmodel.library

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

            val result = cardRepository.getAllCards()

            result.fold(
                onSuccess = { cardModels ->
                    val allCards = cardModels.map { it.toDomain(context) }
                    val loadedCards = if (filter == "Todas") {
                        allCards
                    } else {
                        allCards.filter { it.faction == filter }
                    }
                    _uiState.update { it.copy(cards = loadedCards, isLoading = false, error = null) }
                },
                onFailure = { throwable ->
                    _uiState.update { it.copy(error = "Error al cargar cartas: ${throwable.message}", isLoading = false) }
                }
            )
        }
    }

    fun setFactionFilter(faction: String) {
        _uiState.update { it.copy(currentFactionFilter = faction) }
        loadCards()
    }
}

// Función de extensión para convertir CardModel a Card
fun CardModel.toDomain(context: Context): Card {
    val resourceName = this.imageUrl.substringBeforeLast(".")
    val imageId = context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )

    return Card(
        id = this.mongoId,
        name = this.name,
        cost = this.cost,
        attack = this.attack,
        health = this.health,
        type = this.type,
        faction = this.faction,
        description = this.description,
        imageResId = imageId
    )
}