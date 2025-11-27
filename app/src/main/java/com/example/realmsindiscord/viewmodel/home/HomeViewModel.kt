package com.example.realmsindiscord.viewmodel.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

data class HomeUiState(
    val username: String = "",
    val wins: Int = 0,
    val level: Int = 1
)

class HomeViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState

    // Podemos agregar funciones para cargar datos del usuario
    fun loadUserData(username: String) {
        // Aquí cargaríamos datos del usuario desde la base de datos
        _uiState.value = _uiState.value.copy(username = username)
    }
}