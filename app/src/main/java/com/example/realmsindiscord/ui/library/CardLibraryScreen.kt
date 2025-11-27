package com.example.realmsindiscord.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.realmsindiscord.ui.common.CardItem
import com.example.realmsindiscord.ui.theme.DarkerBackground
import com.example.realmsindiscord.viewmodel.library.LibraryViewModel

@Composable
fun CardLibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val selectedFaction = uiState.currentFactionFilter

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkerBackground)
            .padding(top = 60.dp)
    ) {
        FactionSelectorScreen(
            currentFaction = selectedFaction,
            onSelectFaction = { faction -> viewModel.setFactionFilter(faction) },
            onGoBack = { viewModel.setFactionFilter("Todas") }
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.cards.isEmpty() && !uiState.isLoading -> {
                    Text(
                        text = "No se encontraron cartas de $selectedFaction.",
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.cards, key = { it.id }) { card ->
                            CardItem(card = card)
                        }
                    }
                }
            }
        }
    }
}


