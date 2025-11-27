package com.example.actividad2.ui.library

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.data.model.Card
import com.example.actividad2.domain.repository.ICardRepository
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
class CardLibraryViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryScreen())
    val uiState: StateFlow<LibraryScreen> = _uiState

    init {
        loadCards()
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val result = cardRepository.getAllCards()

            result.fold(
                onSuccess = { cardModels ->
                    // Convertir CardModel a Card
                    val allCards = cardModels.map { cardModel ->
                        Card(
                            id = cardModel.mongoId,
                            name = cardModel.name,
                            cost = cardModel.cost,
                            attack = cardModel.attack,
                            health = cardModel.health,
                            type = cardModel.type,
                            faction = cardModel.faction,
                            description = cardModel.description,
                            imageResId = getImageResourceId(cardModel.imageUrl)
                        )
                    }

                    val filter = _uiState.value.currentFactionFilter
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

    // Función auxiliar para convertir imageUrl a resourceId
    @SuppressLint("DiscouragedApi")
    private fun getImageResourceId(imageUrl: String): Int {
        val resourceName = imageUrl.substringBeforeLast(".")
        return context.resources.getIdentifier(
            resourceName,
            "drawable",
            context.packageName
        )
    }

    fun setFactionFilter(faction: String) {
        _uiState.update { it.copy(currentFactionFilter = faction) }
        loadCards()
    }
}