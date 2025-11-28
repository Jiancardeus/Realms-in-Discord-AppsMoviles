package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.data.local.SessionManager
import com.example.realmsindiscord.data.local.UserDao
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.LoginRequest
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.remote.request.UpdateUsernameRequest
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.domain.repository.IUserRepository
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import retrofit2.HttpException

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: AuthApiService,
    private val sessionManager: SessionManager,
    @Named("userMicroservice") private val microserviceApi: AuthApiService,
    private val userDao: UserDao,
) : IUserRepository {

    override suspend fun getUserByUsername(username: String): User? {
        return userDao.getUserByUsername(username)
    }

    override suspend fun registerUser(user: User): Boolean {
        return try {
            // Usar el microservicio para registrar en MongoDB
            val request = RegisterRequest(user.username, user.email, user.passwordHash)
            val response = microserviceApi.register(request)

            // Si el registro es exitoso en el microservicio, guardar localmente
            if (response.message.contains("registrado") || response.message.contains("exitosamente")) {
                userDao.insertUser(user)
                sessionManager.saveCurrentUser(user.username)
                true
            } else {
                false
            }
        } catch (e: HttpException) {
            // Si hay error de conexión, intentar registro local como fallback
            try {
                val localUserId = userDao.registerUser(user)
                if (localUserId > 0) {
                    sessionManager.saveCurrentUser(user.username)
                    true
                } else {
                    false
                }
            } catch (localError: Exception) {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun isEmailTaken(email: String): Boolean {
        return userDao.isEmailTaken(email)
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
            // 1. Actualizar en MongoDB (microservicio)
            val request = UpdateUsernameRequest(newUsername)
            val response = microserviceApi.updateUsername(request)

            // 2. Si éxito en backend, actualizar localmente
            if (response.success) {
                userDao.updateUsername(userId, newUsername)
                sessionManager.saveCurrentUser(newUsername)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // Fallback: actualizar solo localmente
            try {
                userDao.updateUsername(userId, newUsername)
                sessionManager.saveCurrentUser(newUsername)
                true
            } catch (localError: Exception) {
                false
            }
        }
    }

    override suspend fun deleteUser(userId: Int): Boolean {
        return try {
            // 1. Eliminar en MongoDB (microservicio)
            val response = microserviceApi.deleteUser(userId.toString())

            // 2. Si éxito en backend, eliminar localmente
            if (response.success) {
                userDao.deleteUser(userId)
                sessionManager.clearSession()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            // Fallback: eliminar solo localmente
            try {
                userDao.deleteUser(userId)
                sessionManager.clearSession()
                true
            } catch (localError: Exception) {
                false
            }
        }
    }

    override suspend fun getUserById(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    // NUEVO MÉTODO: Login usando microservicio
    suspend fun loginWithMicroservice(username: String, password: String): Boolean {
        return try {
            val request = LoginRequest(username, password)
            val response = microserviceApi.login(request)

            if (response.username != null) {
                // Login exitoso, guardar usuario localmente
                val user = User(
                    username = response.username,
                    email = "", // El microservicio no devuelve email, podrías necesitar un endpoint adicional
                    passwordHash = password, // Guardar hash seguro en producción
                    level = 1,
                    experience = 0,
                    wins = 0,
                    losses = 0,
                    draws = 0
                )

                // Verificar si el usuario ya existe localmente
                val existingUser = userDao.getUserByUsername(username)
                if (existingUser == null) {
                    userDao.insertUser(user)
                }

                sessionManager.saveCurrentUser(username)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}