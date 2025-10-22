package com.example.actividad2.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.actividad2.data.model.User
import com.example.actividad2.domain.userCase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Estado de la UI para el registro
sealed class RegisterState {
    object Idle : RegisterState()
    object Loading : RegisterState()
    object Success : RegisterState()
    data class Error(val message: String) : RegisterState()
}

@HiltViewModel // Anotación de Hilt para inyección de ViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    /**
     * Intenta registrar un nuevo usuario.
     */
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = RegisterState.Loading

            // Validaciones básicas antes de intentar el registro
            if (username.isBlank() || email.isBlank() || password.isBlank()) {
                _registerState.value = RegisterState.Error("Todos los campos son obligatorios.")
                return@launch
            }

            // Aquí se podría añadir validación de formato de email o fortaleza de contraseña
            // ...

            try {
                // Crea el objeto User (con valores por defecto para level, exp, etc.)
                val newUser = User(
                    username = username,
                    email = email,
                    password = password // NOTA: En un proyecto real, la contraseña debe ser HASHED antes de guardarse.
                )
                val isSuccess = registerUseCase(newUser)

                _registerState.value = if (isSuccess) {
                    RegisterState.Success
                } else {
                    // isSuccess es false si el usuario ya existe
                    RegisterState.Error("El nombre de usuario '$username' ya está en uso.")
                }
            } catch (e: Exception) {
                // Manejo de otros errores (ej. error de base de datos)
                _registerState.value = RegisterState.Error("Error al intentar registrar: ${e.message}")
            }
        }
    }

    /**
     * Reinicia el estado a Idle después de un error o éxito si es necesario.
     */
    fun resetState() {
        _registerState.value = RegisterState.Idle
    }
}