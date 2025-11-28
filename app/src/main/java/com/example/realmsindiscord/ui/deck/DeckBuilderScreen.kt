package com.example.realmsindiscord.ui.deck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.ui.deck.components.DeckBuilderTopBar
import com.example.realmsindiscord.ui.deck.components.DeckPanel
import com.example.realmsindiscord.ui.deck.components.CardLibraryPanel
import com.example.realmsindiscord.viewmodel.deck.DeckBuilderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeckBuilderScreen(
    onBack: () -> Unit,
    onSave: () -> Unit
) {
    val viewModel: DeckBuilderViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState().value // CORREGIDO: .value

    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar mensajes de éxito/error con Snackbar
    LaunchedEffect(state.successMessage, state.error) {
        state.successMessage?.let { message ->
            val result = snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "OK",
                duration = SnackbarDuration.Short
            )
            if (result == SnackbarResult.ActionPerformed) {
                viewModel.clearMessages()
            }
        }

        state.error?.let { error ->
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

    Scaffold(
        topBar = {
            DeckBuilderTopBar(
                onBack = onBack,
                onSave = {
                    if (state.validationResult?.isValid == true) {
                        viewModel.saveDeck()
                        onSave()
                    }
                },
                isSaveEnabled = state.validationResult?.isValid == true
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                // PANEL IZQUIERDO: MAZO ACTUAL
                DeckPanel(
                    deck = state.currentDeck,
                    availableCards = state.availableCards, // AGREGAR este parámetro
                    validationResult = state.validationResult,
                    onRemoveCard = { cardId -> viewModel.removeCardFromDeck(cardId) },
                    onDeckNameChange = { name -> viewModel.setDeckName(name) },
                    modifier = Modifier.weight(1f)
                )

                // DIVIDER
                HorizontalDivider(
                    color = com.example.realmsindiscord.ui.theme.TealAccent.copy(alpha = 0.3f),
                    modifier = Modifier
                        .fillMaxSize() // CORREGIDO: fillMaxSize en lugar de fillMaxHeight
                        .width(1.dp)
                )

                // PANEL DERECHO: BIBLIOTECA DE CARTAS
                CardLibraryPanel(
                    cards = state.filteredCards,
                    selectedFaction = state.selectedFaction,
                    searchQuery = state.searchQuery,
                    onCardClick = { card -> viewModel.addCardToDeck(card) },
                    onFactionSelect = { faction -> viewModel.setSelectedFaction(faction) },
                    onSearchQueryChange = { query -> viewModel.setSearchQuery(query) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}