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

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: IUserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<User?>(null)
    val userState: StateFlow<User?> = _userState.asStateFlow()

    private val _isProfileExpanded = MutableStateFlow(false)
    val isProfileExpanded: StateFlow<Boolean> = _isProfileExpanded.asStateFlow()

    init {
        loadCurrentUser()
    }

    fun loadCurrentUser() {
        viewModelScope.launch {
            // Primero obtener el usuario por username desde SessionManager
            val user = userRepository.getCurrentUser()
            if (user != null) {
                // Luego obtener el usuario completo por ID para tener todos los datos
                val fullUser = userRepository.getUserById(user.id)
                _userState.value = fullUser ?: user // Fallback al usuario básico
            } else {
                _userState.value = null
            }
        }
    }

    fun toggleProfileExpanded() {
        _isProfileExpanded.value = !_isProfileExpanded.value
    }

    fun calculateLevelProgress(): Float {
        val user = _userState.value ?: return 0f
        val expForNextLevel = user.level * 1000
        return user.experience.toFloat() / expForNextLevel
    }
}