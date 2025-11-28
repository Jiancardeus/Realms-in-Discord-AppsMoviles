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

    private val _microserviceStatus = MutableStateFlow<String?>(null)
    val microserviceStatus: StateFlow<String?> = _microserviceStatus.asStateFlow()

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


    fun testMicroserviceConnection() {
        viewModelScope.launch {
            _microserviceStatus.value = "🔍 Probando login en microservicio..."
            try {
                val repository = userRepository as com.example.realmsindiscord.data.repository.UserRepositoryImpl
                val isWorking = repository.testMicroservice()

                if (isWorking) {
                    _microserviceStatus.value = "✅ Microservicio funcionando correctamente"
                } else {
                    _microserviceStatus.value = "⚠️ Conexión OK pero login falló"
                }
            } catch (e: ClassCastException) {
                _microserviceStatus.value = "❌ Error: No se pudo acceder al método de prueba"
            } catch (e: Exception) {
                _microserviceStatus.value = "❌ Error de conexión: ${e.message}"
            }
        }
    }

    // Método para limpiar el mensaje de estado
    fun clearMicroserviceStatus() {
        _microserviceStatus.value = null
    }
}