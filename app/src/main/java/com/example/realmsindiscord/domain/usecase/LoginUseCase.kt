package com.example.realmsindiscord.domain.usecase

import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.LoginRequest
import javax.inject.Inject
import retrofit2.HttpException

class LoginUseCase @Inject constructor(
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
                val user = User(username = response.username, email = "", passwordHash = "")
                LoginResult.Success(user)
            } else {
                LoginResult.Error("Usuario o contraseña incorrectos.")
            }
        } catch (e: HttpException) {
            LoginResult.Error("Credenciales inválidas.")
        } catch (e: Exception) {
            LoginResult.Error("Error de conexión al servidor.")
        }
    }
}