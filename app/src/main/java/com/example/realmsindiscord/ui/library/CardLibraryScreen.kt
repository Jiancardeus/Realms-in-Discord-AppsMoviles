package com.example.realmsindiscord.ui.library

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.realmsindiscord.ui.common.CardItem
import com.example.realmsindiscord.ui.theme.TealAccent
import com.example.realmsindiscord.viewmodel.library.LibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardLibraryScreen(
    onBack: () -> Unit
) {
    val viewModel: LibraryViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Biblioteca de Cartas",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = onBack,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text("← Volver", color = TealAccent)
                    }
                },
                modifier = Modifier.background(Color.Black.copy(alpha = 0.8f))
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // BARRA DE BÚSQUEDA
                OutlinedTextField(
                    value = state.searchQuery,
                    onValueChange = { query -> viewModel.setSearchQuery(query) },
                    label = { Text("Buscar cartas...", color = Color.White) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = androidx.compose.ui.text.TextStyle(color = Color.White),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealAccent,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = TealAccent,
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = TealAccent
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // FILTROS DE FACCIONES
                Text(
                    "Filtrar por Facción",
                    color = TealAccent,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                FactionFilterRow(
                    selectedFaction = state.selectedFaction,
                    onFactionSelect = { faction -> viewModel.filterCards(faction) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // CONTADOR DE CARTAS
                Text(
                    "Cartas mostradas: ${state.filteredCards.size}",
                    color = Color.White,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // LISTA DE CARTAS - GRID MEJORADO
                if (state.isLoading) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            CircularProgressIndicator(color = TealAccent)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                "Cargando cartas...",
                                color = TealAccent,
                                fontSize = 14.sp
                            )
                        }
                    }
                } else if (state.error != null) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = state.error ?: "Error desconocido",
                                color = Color.Red,
                                fontSize = 16.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = { viewModel.loadCards() },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = TealAccent
                                )
                            ) {
                                Text("Reintentar", color = Color.Black)
                            }
                        }
                    }
                } else if (state.filteredCards.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (state.selectedFaction != null) {
                                "No hay cartas de la facción ${state.selectedFaction}"
                            } else if (state.searchQuery.isNotBlank()) {
                                "No se encontraron cartas para '${state.searchQuery}'"
                            } else {
                                "No hay cartas disponibles"
                            },
                            color = Color.Gray,
                            fontSize = 16.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    // GRID DE CARTAS - DISEÑO MEJORADO
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(state.filteredCards, key = { it.mongoId }) { card ->
                            CardItem(
                                card = card,
                                onClick = {
                                    // Puedes agregar funcionalidad de click aquí
                                    // Por ejemplo: ver detalles de la carta
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FactionFilterRow(
    selectedFaction: String?,
    onFactionSelect: (String?) -> Unit
) {
    val factions = listOf("Todas", "Fire", "Water", "Earth", "Air", "Neutral")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        factions.forEach { faction ->
            Card(
                modifier = Modifier
                    .weight(1f)
                    .height(40.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (selectedFaction == faction) TealAccent
                    else Color.Gray.copy(alpha = 0.3f)
                ),
                onClick = {
                    onFactionSelect(if (selectedFaction == faction) null else faction)
                }
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = faction,
                        color = if (selectedFaction == faction) Color.Black else Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}