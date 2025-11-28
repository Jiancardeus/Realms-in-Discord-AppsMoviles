package com.example.realmsindiscord.viewmodel.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.domain.models.Resource
import com.example.realmsindiscord.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class LoginUiState(
    val loginResource: Resource<Unit> = Resource.Idle,
    val username: String = "",
    val password: String = ""
)

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    fun login(username: String, password: String) {
        if (_uiState.value.loginResource.isLoading) return

        _uiState.update {
            it.copy(
                loginResource = Resource.Loading,
                username = username,
                password = password
            )
        }

        viewModelScope.launch {
            val result = loginUseCase(username, password)

            _uiState.update {
                it.copy(
                    loginResource = when (result) {
                        is LoginUseCase.LoginResult.Success -> Resource.Success(Unit)
                        is LoginUseCase.LoginResult.Error -> Resource.Error(result.message)
                    }
                )
            }
        }
    }

    fun resetState() {
        _uiState.update { LoginUiState() }
    }

    fun updateUsername(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    fun updatePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }
}