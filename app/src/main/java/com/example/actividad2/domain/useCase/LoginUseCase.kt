package com.example.actividad2.domain.useCase

import com.example.actividad2.data.model.User
import com.example.actividad2.data.remote.AuthApiService // Nuevo
import com.example.actividad2.data.remote.request.LoginRequest // Nuevo
import javax.inject.Inject
import retrofit2.HttpException

class LoginUseCase @Inject constructor(
    // Inyectamos el servicio de red, no el repositorio de Room
    private val apiService: AuthApiService
) {
    sealed class LoginResult {
        data class Success(val user: User) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    suspend operator fun invoke(username: String, password: String): LoginResult {
        return try {
            val request = LoginRequest(username, password)
            val response = apiService.login(request)

            if (response.username != null) {
                // Login exitoso, creamos el objeto User
                val user = User(username = response.username, email = "", passwordHash = "")
                LoginResult.Success(user)
            } else {
                // Servidor responde 200, pero la lógica dice que no hay usuario
                LoginResult.Error("Usuario o contraseña incorrectos.")
            }
        } catch (e: HttpException) {
            // Error HTTP (ej. 400 Bad Request)
            LoginResult.Error("Credenciales inválidas.")
        } catch (e: Exception) {
            // Error de red (IOException)
            LoginResult.Error("Error de conexión al servidor.")
        }
    }
}