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
        println("🔄 LibraryViewModel inicializado - Cargando cartas...")
        loadCards()
    }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            val filter = _uiState.value.currentFactionFilter
            println("🎯 Cargando cartas con filtro: $filter")

            val result = cardRepository.getAllCards()

            result.fold(
                onSuccess = { cardModels ->
                    println("✅ Cartas obtenidas del repositorio: ${cardModels.size}")

                    val allCards = cardModels.map { it.toDomain(context) }
                    println("✅ Cartas mapeadas al dominio: ${allCards.size}")

                    val loadedCards = if (filter == "Todas") {
                        allCards
                    } else {
                        allCards.filter { it.faction == filter }
                    }

                    println("🎲 Cartas después del filtro '$filter': ${loadedCards.size}")

                    _uiState.update {
                        it.copy(
                            cards = loadedCards,
                            isLoading = false,
                            error = null
                        )
                    }
                },
                onFailure = { throwable ->
                    println("❌ Error al cargar cartas: ${throwable.message}")
                    _uiState.update {
                        it.copy(
                            error = "Error al cargar cartas: ${throwable.message}",
                            isLoading = false
                        )
                    }
                }
            )
        }

    fun loadCards() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val filter = _uiState.value.currentFactionFilter

            val result = cardRepository.getAllCards()

            result.fold(
                onSuccess = { cardModels ->
                    println("✅ Cartas obtenidas de la API: ${cardModels.size}")

                    val allCards = cardModels.map { it.toDomain(context) }
                    println("✅ Cartas mapeadas: ${allCards.size}")

                    val loadedCards = if (filter == "Todas") {
                        allCards
                    } else {
                        allCards.filter { it.faction == filter }
                    }

                    _uiState.update { it.copy(cards = loadedCards, isLoading = false, error = null) }
                    println("✅ Cartas cargadas en UI: ${loadedCards.size}")
                },
                onFailure = { throwable ->
                    println("❌ Error al cargar cartas: ${throwable.message}")
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
    // Primero intentar encontrar la imagen por nombre
    val resourceName = this.imageUrl?.substringBeforeLast(".") ?: "default_card"
    var imageId = context.resources.getIdentifier(
        resourceName,
        "drawable",
        context.packageName
    )

    // Si no se encuentra, intentar con el ID local
    if (imageId == 0) {
        val localResourceName = "local_${this.mongoId ?: this.name?.lowercase()?.replace(" ", "_")}"
        imageId = context.resources.getIdentifier(
            localResourceName,
            "drawable",
            context.packageName
        )
    }

    // Si aún no se encuentra, usar una por defecto
    if (imageId == 0) {
        imageId = android.R.drawable.ic_menu_gallery
        println("⚠️ No se encontró imagen para: ${this.name} (buscado como: $resourceName)")
    }

    val safeFaction = this.faction ?: "Neutral"
    val safeName = this.name ?: "Carta sin nombre"
    val safeType = this.type ?: "Desconocido"
    val safeDescription = this.description ?: "Sin descripción"

    return Card(
        id = this.mongoId ?: "unknown_${System.currentTimeMillis()}",
        name = safeName,
        cost = this.cost ?: 0,
        attack = this.attack ?: 0,
        health = this.health ?: (this.defense ?: 0), // Usar defense como fallback para health
        type = safeType,
        faction = safeFaction,
        description = safeDescription,
        imageResId = imageId
    )
}
}