package com.example.realmsindiscord.viewmodel.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.domain.repository.IUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileManagementState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val isEditingUsername: Boolean = false,
    val newUsername: String = "",
    val showDeleteConfirmation: Boolean = false
)
@HiltViewModel
class ProfileManagementViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileManagementState())
    val state: StateFlow<ProfileManagementState> = _state.asStateFlow()

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    fun loadUser(userId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                val user = userRepository.getUserById(userId)
                _userState.value = user
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = if (user == null) "Usuario no encontrado" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Error al cargar usuario: ${e.message}"
                )
            }
        }
    }

    fun startEditingUsername(currentUsername: String) {
        _state.value = _state.value.copy(
            isEditingUsername = true,
            newUsername = currentUsername,
            error = null,
            successMessage = null
        )
    }

    fun cancelEditing() {
        _state.value = _state.value.copy(
            isEditingUsername = false,
            newUsername = "",
            error = null
        )
    }

    fun updateNewUsername(username: String) {
        _state.value = _state.value.copy(newUsername = username)
    }

    fun saveUsername(userId: Int, currentUsername: String) {
        if (_state.value.newUsername.isBlank()) {
            _state.value = _state.value.copy(error = "El nombre de usuario no puede estar vacío")
            return
        }

        if (_state.value.newUsername == currentUsername) {
            _state.value = _state.value.copy(
                isEditingUsername = false,
                error = null
            )
            return
        }

        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val success = userRepository.updateUsername(userId, _state.value.newUsername)

                if (success) {
                    // Actualizar el usuario localmente
                    val updatedUser = _userState.value?.copy(username = _state.value.newUsername)
                    _userState.value = updatedUser

                    _state.value = _state.value.copy(
                        isLoading = false,
                        isEditingUsername = false,
                        successMessage = "✅ Nombre de usuario actualizado correctamente",
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "❌ Error: El nombre de usuario ya está en uso"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "❌ Error de conexión: ${e.message}"
                )
            }
        }
    }

    fun setUserDirectly(user: User) {
        _userState.value = user
        _state.value = _state.value.copy(isLoading = false, error = null)
    }

    fun showDeleteConfirmation() {
        _state.value = _state.value.copy(showDeleteConfirmation = true)
    }

    fun hideDeleteConfirmation() {
        _state.value = _state.value.copy(showDeleteConfirmation = false)
    }

    fun deleteAccount(userId: Int, onSuccess: () -> Unit) {
        _state.value = _state.value.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val success = userRepository.deleteUser(userId)

                if (success) {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        showDeleteConfirmation = false,
                        successMessage = "✅ Cuenta eliminada correctamente"
                    )
                    // Pequeño delay para mostrar el mensaje de éxito
                    kotlinx.coroutines.delay(1500)
                    onSuccess() // Navegar al login
                } else {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "❌ Error al eliminar la cuenta"
                    )
                }
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "❌ Error de conexión: ${e.message}"
                )
            }
        }
    }

    fun clearMessages() {
        _state.value = _state.value.copy(
            error = null,
            successMessage = null
        )
    }
}