package com.example.realmsindiscord.ui.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.ui.deck.components.DeckBuilderTopBar
import com.example.realmsindiscord.ui.deck.components.DeckPanel
import com.example.realmsindiscord.ui.deck.components.CardLibraryPanelReal
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.deck.DeckBuilderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val viewModel: DeckBuilderViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value

    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Calcular total de cartas una vez
    val totalCards = state.currentDeck?.cards?.sumOf { it.count } ?: 0
    val hasErrors = state.validationResult?.errors?.isNotEmpty() == true
    val canSave = !hasErrors && totalCards > 0

    println("DEBUG: Screen - totalCards: $totalCards, hasErrors: $hasErrors, canSave: $canSave")
    println("DEBUG: Screen - currentDeck: ${state.currentDeck?.name}")
    println("DEBUG: Screen - validationResult isValid: ${state.validationResult?.isValid}")

    // Efecto para DEBUG: Verificar cuando cambia el estado de guardado
    LaunchedEffect(canSave, state.isLoading, state.successMessage) {
        println("DEBUG: Estado actualizado - canSave: $canSave, isLoading: ${state.isLoading}, successMessage: ${state.successMessage}")
    }

    // Manejar mensajes
    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let { message ->
            println("DEBUG: Mostrando success message: $message")
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = "OK",
                    duration = SnackbarDuration.Short
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.clearMessages()
                }
            }
        }

        state.error?.let { error ->
            println("DEBUG: Mostrando error: $error")
            coroutineScope.launch {
                val result = snackbarHostState.showSnackbar(
                    message = error,
                    actionLabel = "Reintentar",
                    duration = SnackbarDuration.Long
                )
                if (result == SnackbarResult.ActionPerformed) {
                    viewModel.clearMessages()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            DeckBuilderTopBar(
                onBack = onBack,
                onNewDeck = {
                    println("DEBUG: Botón Nuevo presionado")
                    viewModel.createNewDeck()
                },
                onSave = {
                    println("DEBUG: Botón Guardar presionado - canSave: $canSave")
                    println("DEBUG: currentDeck: ${state.currentDeck}")
                    println("DEBUG: validationResult: ${state.validationResult}")

                    if (canSave) {
                        println("DEBUG: Llamando a saveDeck()")
                        viewModel.saveDeck()
                    } else {
                        val errorMessage = when {
                            totalCards == 0 -> "Agrega cartas al mazo antes de guardar"
                            hasErrors -> state.validationResult?.errors?.first() ?: "Error en el mazo"
                            else -> "No se puede guardar el mazo"
                        }
                        println("DEBUG: Mostrando error de guardado: $errorMessage")
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar(
                                message = errorMessage,
                                actionLabel = "Entendido",
                                duration = SnackbarDuration.Short
                            )
                        }
                    }
                },
                isSaveEnabled = canSave
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        // ... (el resto del código del layout igual)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading && state.availableCards.isEmpty()) {
                // Loading...
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black)
                ) {
                    // PANEL IZQUIERDO: MAZO ACTUAL
                    Box(modifier = Modifier.weight(1f)) {
                        DeckPanel(
                            deck = state.currentDeck,
                            availableCards = state.availableCards,
                            validationResult = state.validationResult,
                            onRemoveCard = { cardId ->
                                viewModel.removeCardFromDeck(cardId)
                            },
                            onAddCardCopy = { cardId ->
                                val card = state.availableCards.find { it.mongoId == cardId }
                                card?.let { viewModel.addCardToDeck(it) }
                            },
                            onDeckNameChange = { name ->
                                viewModel.setDeckName(name)
                            }
                        )
                    }

                    // DIVIDER
                    HorizontalDivider(
                        color = TealAccent.copy(alpha = 0.5f),
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                    )

                    // PANEL DERECHO: BIBLIOTECA
                    Box(modifier = Modifier.weight(1.2f)) {
                        CardLibraryPanelReal(
                            cards = state.filteredCards,
                            selectedFaction = state.selectedFaction,
                            searchQuery = state.searchQuery,
                            onCardClick = { card ->
                                viewModel.addCardToDeck(card)
                            },
                            onFactionSelect = { faction ->
                                viewModel.setSelectedFaction(faction)
                            },
                            onSearchQueryChange = { query ->
                                viewModel.setSearchQuery(query)
                            }
                        )
                    }
                }
            }
        }
    }
}