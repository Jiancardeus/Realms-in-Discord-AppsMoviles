package com.example.realmsindiscord.data.repository

import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.model.User
import com.example.realmsindiscord.domain.repository.IUserRepository
import javax.inject.Inject
import retrofit2.HttpException

class UserRepository @Inject constructor(
    private val apiService: AuthApiService
) : IUserRepository {

    override suspend fun getUserByUsername(username: String): User? {
        return null
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
        return false
    }
}