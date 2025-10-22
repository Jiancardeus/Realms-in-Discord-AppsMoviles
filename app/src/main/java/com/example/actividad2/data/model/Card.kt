package com.example.actividad2.data.model

data class Card (
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