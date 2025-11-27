package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.data.model.User

interface IUserRepository {
    suspend fun getUserByUsername(username: String): User?
    suspend fun registerUser(user: User): Boolean
    suspend fun isEmailTaken(email: String): Boolean
    suspend fun getCurrentUser(): User?
    suspend fun saveUserLocally(user: User)
    suspend fun updateUserStats(user: User)
    suspend fun updateUsername(userId: Int, newUsername: String): Boolean
    suspend fun deleteUser(userId: Int): Boolean
    suspend fun getUserById(userId: Int): User?
}