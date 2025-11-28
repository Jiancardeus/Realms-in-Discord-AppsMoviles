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
    @Named("userMicroservice") private val microserviceApi: AuthApiService,
    private val sessionManager: SessionManager,
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
            println("🚀 [UserRepository] START updateUsername")

            // Obtener usuario actual para saber su username
            val currentUser = userDao.getUserById(userId)
            if (currentUser == null) {
                println("❌ [UserRepository] Current user not found with id: $userId")
                return false
            }

            val currentUsername = currentUser.username
            println("📱 [UserRepository] currentUsername: '$currentUsername', newUsername: '$newUsername'")

            val request = UpdateUsernameRequest(
                currentUsername = currentUsername, // ✅ Usar username actual
                newUsername = newUsername
            )

            println("📤 [UserRepository] Sending request to microservice: $request")

            val response = microserviceApi.updateUsername(request)

            println("📥 [UserRepository] Microservice response received")
            println("✅ [UserRepository] Response - success: ${response.success}, message: ${response.message}")

            if (response.success) {
                println("💾 [UserRepository] Updating local database...")
                userDao.updateUsername(userId, newUsername)
                sessionManager.saveCurrentUser(newUsername)
                println("🎉 [UserRepository] Username updated successfully in both DBs")
                true
            } else {
                println("❌ [UserRepository] Microservice returned false: ${response.message}")
                false
            }
        } catch (e: Exception) {
            println("💥 [UserRepository] ERROR updating username: ${e.javaClass.simpleName} - ${e.message}")
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteUser(userId: Int): Boolean {
        return try {
            println("🚀 [UserRepository] START deleteUser")

            // Obtener usuario para saber su username
            val currentUser = userDao.getUserById(userId)
            if (currentUser == null) {
                println("❌ [UserRepository] Current user not found with id: $userId")
                return false
            }

            val username = currentUser.username
            println("📱 [UserRepository] Deleting user with username: '$username'")

            println("📤 [UserRepository] Sending DELETE request for username: $username")

            // ✅ CAMBIADO: Enviar username como query parameter
            val response = microserviceApi.deleteUser(username)

            println("📥 [UserRepository] Delete response received")
            println("✅ [UserRepository] Delete response - success: ${response.success}, message: ${response.message}")

            if (response.success) {
                println("💾 [UserRepository] Deleting from local database...")
                userDao.deleteUser(userId)
                sessionManager.clearSession()
                println("🎉 [UserRepository] User deleted successfully from both DBs")
                true
            } else {
                println("❌ [UserRepository] Microservice returned false for delete: ${response.message}")
                false
            }
        } catch (e: Exception) {
            println("💥 [UserRepository] ERROR deleting user: ${e.javaClass.simpleName} - ${e.message}")
            e.printStackTrace()
            false
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