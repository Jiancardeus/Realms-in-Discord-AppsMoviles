package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.data.model.User

interface IUserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun registerUser(user: User): Boolean
    suspend fun isEmailTaken(email: String): Boolean
}