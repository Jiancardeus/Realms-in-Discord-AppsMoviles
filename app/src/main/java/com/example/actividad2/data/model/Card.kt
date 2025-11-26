package com.example.actividad2.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey
    val id: String,
    val name: String,
    val cost: Int,
    val attack: Int,
    val health: Int,
    val type: String,
    val faction: String,
    val description: String,
    val imageResId: Int
)