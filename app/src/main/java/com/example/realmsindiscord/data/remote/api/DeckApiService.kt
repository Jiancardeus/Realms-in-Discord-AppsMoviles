package com.example.realmsindiscord.data.remote.api

import com.example.realmsindiscord.domain.models.Deck
import retrofit2.Response
import retrofit2.http.*

interface DeckApiService {

    @GET("api/decks/user/{username}")
    suspend fun getUserDecks(@Path("username") username: String): Response<List<Deck>>

    @POST("api/decks")
    suspend fun saveDeck(@Body deck: Deck): Response<Deck>

    @DELETE("api/decks/{id}")
    suspend fun deleteDeck(@Path("id") id: String): Response<Void>
}