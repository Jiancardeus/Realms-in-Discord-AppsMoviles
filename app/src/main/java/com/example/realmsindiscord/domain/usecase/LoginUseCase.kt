package com.example.realmsindiscord.domain.usecase

import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.LoginRequest
import com.example.realmsindiscord.data.repository.UserRepositoryImpl
import javax.inject.Inject
import retrofit2.HttpException

class LoginUseCase @Inject constructor(
    private val apiService: AuthApiService,
    private val userRepositoryImpl: UserRepositoryImpl
) {
    sealed class LoginResult {
        data class Success(val user: User) : LoginResult()
        data class Error(val message: String) : LoginResult()
    }

    suspend operator fun invoke(username: String, password: String): LoginResult {
        return try {
            // PRIMERO intentar con el microservicio
            val microserviceSuccess = userRepositoryImpl.loginWithMicroservice(username, password)

            if (microserviceSuccess) {
                // Éxito con microservicio, obtener usuario local
                val user = userRepositoryImpl.getUserByUsername(username)
                if (user != null) {
                    LoginResult.Success(user)
                } else {
                    // Crear usuario local si no existe
                    val newUser = User(
                        username = username,
                        email = "",
                        passwordHash = password,
                        level = 1,
                        experience = 0,
                        wins = 0,
                        losses = 0,
                        draws = 0
                    )
                    userRepositoryImpl.saveUserLocally(newUser)
                    LoginResult.Success(newUser)
                }
            } else {
                // Fallback: intentar con servidor principal
                val request = LoginRequest(username, password)
                val response = apiService.login(request)

                if (response.username != null) {
                    val user = User(
                        username = response.username,
                        email = "",
                        passwordHash = password,
                        level = 1,
                        experience = 0,
                        wins = 0,
                        losses = 0,
                        draws = 0
                    )
                    userRepositoryImpl.saveUserLocally(user)
                    LoginResult.Success(user)
                } else {
                    LoginResult.Error("Usuario o contraseña incorrectos.")
                }
            }
        } catch (e: HttpException) {
            LoginResult.Error("Credenciales inválidas.")
        } catch (e: Exception) {
            LoginResult.Error("Error de conexión al servidor.")
        }
    }
}