package com.example.realmsindiscord.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val username: String,
    val email: String,
    val passwordHash: String,
    val level: Int = 1,
    val experience: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
)