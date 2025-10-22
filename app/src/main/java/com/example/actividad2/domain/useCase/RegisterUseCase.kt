package com.example.actividad2.domain.useCase

import com.example.actividad2.data.model.User
import com.example.actividad2.data.remote.AuthApiService // Nuevo
import com.example.actividad2.data.remote.request.RegisterRequest // Nuevo
import javax.inject.Inject
import retrofit2.HttpException

class RegisterUseCase @Inject constructor(
    // Inyectamos el servicio de red, no el repositorio de Room
    private val apiService: AuthApiService
) {
    sealed class RegisterResult {
        object Success : RegisterResult()
        object UsernameTaken : RegisterResult()
        object EmailTaken : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }

    suspend operator fun invoke(user: User): RegisterResult {
        return try {
            val request = RegisterRequest(user.username, user.email, user.passwordHash)
            val response = apiService.register(request)

            if (response.message.contains("registrado")) {
                RegisterResult.Success
            } else {
                RegisterResult.Error("Error desconocido: ${response.message}")
            }
        } catch (e: HttpException) {
            // Si el servidor Express detectó duplicidad (por ejemplo, status 500 o 400)
            val errorBody = e.response()?.errorBody()?.string()

            if (errorBody?.contains("duplicate key error") == true || errorBody?.contains("username") == true) {
                return RegisterResult.UsernameTaken
            }
            if (errorBody?.contains("email") == true) {
                return RegisterResult.EmailTaken
            }
            RegisterResult.Error("Error de servidor: ${e.code()}")
        } catch (e: Exception) {
            RegisterResult.Error("Error de conexión: ${e.message}")
        }
    }
}