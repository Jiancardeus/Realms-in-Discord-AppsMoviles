package com.example.actividad2.ui.library

import android.content.Context // Necesario para acceder a los recursos (R.drawable)
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.data.model.Card // Modelo interno
import com.example.actividad2.data.remote.model.CardModel // Modelo de la red
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
    val currentFactionFilter: String = "Todas" // "Todas", "Caballeros Solares", "Corrupción"
)

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val cardRepository: ICardRepository,
    // INYECTAMOS EL CONTEXTO para poder convertir el nombre del archivo a su ID de recurso
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(LibraryUiState())
    val uiState: StateFlow<LibraryUiState> = _uiState

    init {
        // Carga las cartas al iniciar el ViewModel
        loadCards()
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val filter = _uiState.value.currentFactionFilter

            // Llamar a la función que SÍ existe en ICardRepository
            val result = cardRepository.getAllCards() // Asumimos que devuelve Result<List<CardModel>>

            result.fold(
                onSuccess = { cardModels ->
                    // 1. Mapear CardModel (de la red) a Card (modelo interno)
                    val allCards = cardModels.map { it.toDomain(context) }

                    // 2. Aplicar filtro
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
        loadCards() // Recargar cartas con el nuevo filtro
    }
}

// FUNCIÓN DE MAPEO Y CONVERSIÓN DE IMAGEN (De Modelo de Red a Modelo de Dominio)
// Esta función resuelve el error de Resource ID #0x0 (recurso no encontrado)

fun com.example.actividad2.data.remote.model.CardModel.toDomain(context: Context): Card {

    // Convertir el nombre del archivo (ej: "espadachin_solar.png") al ID de recurso de Android (R.drawable.espadachin_solar)
    val resourceName = this.imageUrl.substringBeforeLast(".") // Obtiene "espadachin_solar"

    val imageId = context.resources.getIdentifier(
        resourceName, // Nombre del archivo sin extensión
        "drawable",   // La carpeta de recursos donde está (res/drawable)
        context.packageName // El paquete de la aplicación
    )

    return Card(
        // Propiedades de la red (CardModel) a las propiedades internas (Card)
        id = this.mongoId,
        name = this.name,
        cost = this.cost,
        attack = this.attack,
        health = this.health,
        type = this.type,
        faction = this.faction,
        description = this.description,
        imageResId = imageId //
    )
}