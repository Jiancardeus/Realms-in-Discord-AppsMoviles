package com.example.realmsindiscord.domain.repository

import com.example.realmsindiscord.data.remote.model.CardModel

interface ICardRepository {
    suspend fun getCards(): List<CardModel>

}