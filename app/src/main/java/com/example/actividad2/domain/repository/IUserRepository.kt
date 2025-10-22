package com.example.actividad2.domain.repository

import com.example.actividad2.data.model.User

interface IUserRepository {
    // Busca un usuario por nombre. Usado en Login.
    suspend fun getUserByUsername(username: String): User?

    // Registra un nuevo usuario.
    suspend fun registerUser(user: User): Boolean

    // Verifica si el email ya esta en uso.
    suspend fun isEmailTaken(email: String): Boolean
}