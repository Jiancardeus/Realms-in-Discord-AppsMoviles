package com.example.realmsindiscord.domain.usecase

import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import javax.inject.Inject
import retrofit2.HttpException

class RegisterUseCase @Inject constructor(
    private val apiService: AuthApiService
) {
    suspend operator fun invoke(username: String, email: String, password: String): RegisterResult {
        return try {
            val request = RegisterRequest(username, email, password)
            val response = apiService.register(request)

            if (response.message.contains("registrado")) {
                RegisterResult.Success
            } else {
                RegisterResult.Error("Error desconocido: ${response.message}")
            }
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            when {
                errorBody?.contains("username") == true -> RegisterResult.UsernameTaken
                errorBody?.contains("email") == true -> RegisterResult.EmailTaken
                else -> RegisterResult.Error("Error de servidor: ${e.code()}")
            }
        } catch (e: Exception) {
            RegisterResult.Error("Error de conexión: ${e.message}")
        }
    }

    sealed class RegisterResult {
        object Success : RegisterResult()
        object UsernameTaken : RegisterResult()
        object EmailTaken : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }
}