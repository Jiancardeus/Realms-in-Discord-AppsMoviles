package com.example.realmsindiscord.domain.usecase

import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import javax.inject.Inject
import retrofit2.HttpException

class RegisterUseCase @Inject constructor(
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