package com.example.actividad2.data.remote

import com.example.actividad2.data.remote.model.CardModel
import retrofit2.http.GET

interface CardApiService {

    // Endpoint para obtener todas las cartas
    // Corresponde a la ruta GET /api/cards en tu servidor Express
    @GET("api/cards")
    suspend fun getCards(): List<CardModel>
}