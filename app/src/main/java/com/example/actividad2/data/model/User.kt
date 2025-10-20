package com.example.actividad2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val username: String,
    val email: String,
    val password: String,
    val level: Int = 1,
    val experience: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
)