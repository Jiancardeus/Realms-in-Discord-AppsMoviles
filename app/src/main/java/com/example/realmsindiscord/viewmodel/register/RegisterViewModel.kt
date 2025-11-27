package com.example.realmsindiscord.viewmodel.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

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

        val newUser = User(
            username = username,
            email = email,
            passwordHash = password
        )

        viewModelScope.launch {
            val result = registerUseCase(newUser)

            _uiState.value = when (result) {
                RegisterUseCase.RegisterResult.Success -> {
                    _uiState.value.copy(
                        registrationResult = result,
                        isLoading = false,
                        error = null
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
                is RegisterUseCase.RegisterResult.Error -> {
                    _uiState.value.copy(
                        error = result.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun resetState() {
        _uiState.value = RegisterUiState()
    }
}