package com.example.actividad2.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.data.model.User
import com.example.actividad2.domain.useCase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Clase de estado de la UI
data class RegisterUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val registrationResult: RegisterUseCase.RegisterResult? = null
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(username: String, email: String, password: String) {
        if (_uiState.value.isLoading) return

        _uiState.value = _uiState.value.copy(isLoading = true, error = null, registrationResult = null)

        // Aquí podrías añadir validaciones de formato (ej: email válido, password length)

        val newUser = User(
            username = username,
            email = email,
            passwordHash = password // Usaremos la password como hash simulado por ahora
        )

        viewModelScope.launch {
            val result = registerUseCase(newUser)

            _uiState.value = when (result) {

                RegisterUseCase.RegisterResult.Success -> {
                    _uiState.value.copy(
                        registrationResult = result,
                        isLoading = false,
                        error = null // Aseguramos que no haya mensaje de error visible
                    )
                }

                RegisterUseCase.RegisterResult.UsernameTaken -> {
                    _uiState.value.copy(
                        error = "El nombre de usuario ya está en uso.",
                        isLoading = false
                    )
                }

                RegisterUseCase.RegisterResult.EmailTaken -> {
                    _uiState.value.copy(
                        error = "El email ya está registrado.",
                        isLoading = false
                    )
                }

                // Manejamos el tipo 'Error' que tiene un mensaje
                is RegisterUseCase.RegisterResult.Error -> {
                    _uiState.value.copy(
                        error = result.message, // Usamos el mensaje de error de la capa de dominio
                        isLoading = false
                    )
                }
            }
        }
    }

    // Función para resetear el estado después de una navegación exitosa
    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}