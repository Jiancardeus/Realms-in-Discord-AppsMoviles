package com.example.realmsindiscord.viewmodel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.domain.models.Resource
import com.example.realmsindiscord.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RegisterUiState(
    val registerResource: Resource<Unit> = Resource.Idle,
    val username: String = "",
    val email: String = "",
    val password: String = ""
)

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState

    fun register(username: String, email: String, password: String) {
        if (_uiState.value.registerResource.isLoading) return

        _uiState.update {
            it.copy(
                registerResource = Resource.Loading,
                username = username,
                email = email,
                password = password
            )
        }

        viewModelScope.launch {
            val result = registerUseCase(username, email, password)

            _uiState.update {
                it.copy(
                    registerResource = when (result) {
                        RegisterUseCase.RegisterResult.Success -> Resource.Success(Unit)
                        RegisterUseCase.RegisterResult.UsernameTaken -> Resource.Error("El nombre de usuario ya está en uso")
                        RegisterUseCase.RegisterResult.EmailTaken -> Resource.Error("El email ya está registrado")
                        is RegisterUseCase.RegisterResult.Error -> Resource.Error(result.message)
                    }
                )
            }
        }
    }

    fun resetState() {
        _uiState.update { RegisterUiState() }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updateEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }
}