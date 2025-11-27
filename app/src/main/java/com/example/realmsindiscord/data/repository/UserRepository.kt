package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.data.local.SessionManager
import com.example.realmsindiscord.data.local.UserDao
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.remote.request.UpdateUsernameRequest
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Singleton
import retrofit2.HttpException

@Singleton
class UserRepository @Inject constructor(
    private val apiService: AuthApiService,
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : IUserRepository {

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun registerUser(user: User): Boolean {
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
        return false // Por implementar
    }

    override suspend fun getCurrentUser(): User? {
        val username = sessionManager.getCurrentUsername()
        return username?.let { userDao.getUserByUsername(it) }
    }

    override suspend fun saveUserLocally(user: User) {
        userDao.insertUser(user)
        sessionManager.saveCurrentUser(user.username)
    }

    override suspend fun updateUserStats(user: User) {
        userDao.updateUserStats(
            id = user.id,
            level = user.level,
            experience = user.experience,
            wins = user.wins,
            losses = user.losses,
            draws = user.draws
        )
    }

    override suspend fun updateUsername(userId: Int, newUsername: String): Boolean {
        return try {
            // 1. Primero intentar actualizar en MongoDB (backend)
            val request = UpdateUsernameRequest(newUsername)
            val response = apiService.updateUsername(request)

            // 2. Si éxito en backend, actualizar localmente
            if (response.success) {
                userDao.updateUsername(userId, newUsername)
                sessionManager.saveCurrentUser(newUsername)
                true
            } else {
                false
            }
        } catch (e: HttpException) {
            // Si hay error HTTP, podríamos mostrar un mensaje específico
            false
        } catch (e: Exception) {
            // Si hay error de conexión, intentar solo localmente como fallback
            // O mostrar mensaje de "sin conexión"
            try {
                userDao.updateUsername(userId, newUsername)
                sessionManager.saveCurrentUser(newUsername)
                true // Devolver true pero mostrar advertencia de que es solo local
            } catch (localError: Exception) {
                false
            }
        }
    }

    override suspend fun deleteUser(userId: Int): Boolean {
        return try {
            // 1. Primero intentar eliminar en MongoDB (backend)
            val response = apiService.deleteUser(userId.toString())

            // 2. Si éxito en backend, eliminar localmente
            if (response.success) {
                userDao.deleteUser(userId)
                sessionManager.clearSession()
                true
            } else {
                false
            }
        } catch (e: HttpException) {
            false
        } catch (e: Exception) {
            // Si hay error de conexión, intentar solo localmente
            try {
                userDao.deleteUser(userId)
                sessionManager.clearSession()
                true // Devolver true pero mostrar advertencia
            } catch (localError: Exception) {
                false
            }
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }
}