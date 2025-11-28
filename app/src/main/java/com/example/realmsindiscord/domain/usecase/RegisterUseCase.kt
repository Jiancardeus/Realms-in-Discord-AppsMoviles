package com.example.realmsindiscord.domain.usecase

import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.repository.UserRepositoryImpl
import javax.inject.Inject
import retrofit2.HttpException

class RegisterUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val userRepository: UserRepositoryImpl
) {
    suspend operator fun invoke(username: String, email: String, password: String): RegisterResult {
        return try {
            // Crear objeto User para el registro
            val user = User(
                username = username,
                email = email,
                passwordHash = password, // En producción, usar hash seguro
                level = 1,
                experience = 0,
                wins = 0,
                losses = 0,
                draws = 0
            )

            // Usar el UserRepository que ahora conecta con el microservicio
            val success = userRepository.registerUser(user)

            if (success) {
                RegisterResult.Success
            } else {
                RegisterResult.Error("Error en el registro")
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