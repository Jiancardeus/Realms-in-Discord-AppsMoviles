package com.example.realmsindiscord.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.realmsindiscord.domain.models.Resource
import com.example.realmsindiscord.ui.common.CardItem
import com.example.realmsindiscord.ui.common.EmptyState
import com.example.realmsindiscord.ui.common.ResourceHandler
import com.example.realmsindiscord.ui.common.DefaultLoadingState
import com.example.realmsindiscord.ui.common.DefaultErrorState
import com.example.realmsindiscord.ui.theme.DarkerBackground
import com.example.realmsindiscord.viewmodel.library.LibraryViewModel

@Composable
fun CardLibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkerBackground)
    ) {
        // Selector de facción
        FactionSelectorScreen(
            currentFaction = uiState.currentFactionFilter,
            onSelectFaction = viewModel::setFactionFilter,
            onGoBack = { viewModel.setFactionFilter("Todas") }
        )

        // Contador de cartas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            when (val resource = uiState.cardsResource) {
                is Resource.Success -> {
                    Text(
                        text = "${resource.data.size} cartas de ${uiState.currentFactionFilter}",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.align(Alignment.CenterEnd)
                    )
                }
                else -> {
                    // No mostrar contador en otros estados
                }
            }
        }

        // Contenido principal con manejo de estados
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            ResourceHandler(
                resource = uiState.cardsResource,
                onSuccess = { cards ->
                    if (cards.isEmpty()) {
                        EmptyState(
                            message = "No se encontraron cartas de ${uiState.currentFactionFilter}",
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Adaptive(minSize = 160.dp),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
                            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
                            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
                        ) {
                            items(cards, key = { it.id }) { card ->
                                CardItem(card = card)
                            }
                        }
                    }
                },
                onLoading = {
                    DefaultLoadingState(
                        message = "Cargando cartas de ${uiState.currentFactionFilter}...",
                        modifier = Modifier.fillMaxSize()
                    )
                },
                onError = { message, onRetry ->
                    DefaultErrorState(
                        message = message,
                        onRetry = viewModel::retry,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            )
        }
    }
}