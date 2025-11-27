package com.example.realmsindiscord.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.data.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<Card>)

    @Query("SELECT * FROM cards ORDER BY cost ASC, name ASC")
    fun getAllCards(): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): Card?

    @Query("SELECT * FROM cards WHERE id IN (:cardIds)")
    suspend fun getCardsByIds(cardIds: List<String>): List<Card>

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUsername(username: String): User?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User): Long

    @Query("UPDATE users SET level = :level, experience = :experience, wins = :wins, losses = :losses, draws = :draws WHERE id = :id")
    suspend fun updateUserStats(id: Int, level: Int, experience: Int, wins: Int, losses: Int, draws: Int)
}
