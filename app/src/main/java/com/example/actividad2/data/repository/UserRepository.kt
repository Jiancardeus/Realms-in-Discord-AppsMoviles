package com.example.actividad2.data.repository

import com.example.actividad2.data.remote.AuthApiService // Necesitas importar el servicio de red
import com.example.actividad2.data.remote.request.RegisterRequest
import com.example.actividad2.data.model.User
import com.example.actividad2.domain.repository.IUserRepository
import javax.inject.Inject
import retrofit2.HttpException

// Inyectaremos el servicio de red, no el UserDao
class UserRepository @Inject constructor(
    private val apiService: AuthApiService
) : IUserRepository {

    // Los métodos 'getUserByUsername' y 'isEmailTaken' se vuelven obsoletos en la migración,
    // pero deben implementarse para satisfacer el contrato IUserRepository.
    // Usaremos la API directamente en el UseCase, por lo que estos pueden devolver null/false.

    override suspend fun getUserByUsername(username: String): User? {
        // Lógica movida al LoginUseCase para usar el servicio de red
        return null
    }

    override suspend fun registerUser(user: User): Boolean {
        // Usamos la llamada de red para registrar el usuario
        return try {
            val request = RegisterRequest(user.username, user.email, user.passwordHash)
            val response = apiService.register(request)

            response.message.contains("registrado")
        } catch (e: HttpException) {
            false
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isEmailTaken(email: String): Boolean {
        // No es necesario en este momento, ya que la API maneja la duplicidad
        return false
    }
}