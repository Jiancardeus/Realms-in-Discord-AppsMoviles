package com.example.realmsindiscord.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.realmsindiscord.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE email = :email)")
    suspend fun isEmailTaken(email: String): Boolean

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: User): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("UPDATE users SET level = :level, experience = :experience, wins = :wins, losses = :losses, draws = :draws WHERE id = :id")
    suspend fun updateUserStats(id: Int, level: Int, experience: Int, wins: Int, losses: Int, draws: Int)

    // ✅ NUEVOS MÉTODOS NECESARIOS:
    @Query("UPDATE users SET username = :newUsername WHERE id = :userId")
    suspend fun updateUsername(userId: Int, newUsername: String)

    @Query("DELETE FROM users WHERE id = :userId")
    suspend fun deleteUser(userId: Int)

    @Query("SELECT * FROM users WHERE id = :userId")
    suspend fun getUserById(userId: Int): User?
}