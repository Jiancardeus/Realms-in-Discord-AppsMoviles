package com.example.actividad2.ui.library

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
import com.example.actividad2.data.model.LibraryViewModel
import com.example.actividad2.ui.theme.DarkerBackground

@Composable
fun CardLibraryScreen(
    navController: NavController,
    viewModel: LibraryViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // El filtro actual se obtiene del ViewModel
    val selectedFaction = uiState.currentFactionFilter
    val isFilterActive = selectedFaction != "Todas"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkerBackground) // Fondo principal
            .padding(top = 60.dp) // Espacio para la Navbar, si no es flotante
    ) {

        // --- 1. Selector de Facciones ---
        // Asumo que tu FactionSelector está aquí
        FactionSelectorScreen(
            currentFaction = selectedFaction,
            onSelectFaction = { faction -> viewModel.setFactionFilter(faction) },
            // Permite volver al selector de facciones si se selecciona "Todas" inicialmente.
            onGoBack = { viewModel.setFactionFilter("Todas") }
        )

        // --- 2. Contenedor de la Lista y Scroll ---
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f) // Ocupa el resto del espacio
        ) {
            when {
                // A. Muestra el estado de CARGA
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // B. Muestra el estado de ERROR
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // C. Muestra el estado VACÍO
                uiState.cards.isEmpty() && !uiState.isLoading -> {
                    Text(
                        text = "No se encontraron cartas de $selectedFaction.",
                        color = Color.LightGray,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // D. Muestra el GRID de Cartas (El caso de éxito)
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