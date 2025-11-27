package com.example.realmsindiscord.data.remote.api

import com.example.realmsindiscord.data.remote.model.CardModel
import retrofit2.http.GET

interface CardApiService {
    @GET("api/cards")
    suspend fun getCards(): List<CardModel>
}