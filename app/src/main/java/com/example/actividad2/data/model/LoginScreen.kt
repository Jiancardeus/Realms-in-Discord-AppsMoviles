package com.example.actividad2.data.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.domain.useCase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Clase de estado para la UI (Maneja lo que la vista debe mostrar)
data class LoginUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    // 1. DECLARACIÓN DEL ESTADO INTERNO (MutableStateFlow)
    private val _uiState = MutableStateFlow(LoginUiState())

    // 2. DECLARACIÓN DEL ESTADO PÚBLICO (StateFlow - Solo lectura)
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        // Ahora _uiState está definido y puede usarse.
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            when (val result = loginUseCase(username, password)) {
                is LoginUseCase.LoginResult.Success -> {
                    // Login Exitoso
                    _uiState.value = _uiState.value.copy(
                        isAuthenticated = true,
                        isLoading = false
                    )
                }
                is LoginUseCase.LoginResult.Error -> {
                    // Login Fallido (manejo de errores de credenciales o conexión)
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

}

