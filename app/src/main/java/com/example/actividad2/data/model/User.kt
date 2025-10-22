package com.example.actividad2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    // 1. Añadimos el ID numerico y lo marcamos como clave primaria autogenerada
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    // 2. Mantenemos el username como campo normal (Room asegura unicidad por las consultas del DAO)
    val username: String,
    val email: String,
    val passwordHash: String,
    val level: Int = 1,
    val experience: Int = 0,
    val wins: Int = 0,
    val losses: Int = 0,
    val draws: Int = 0
)