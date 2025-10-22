package com.example.actividad2.domain.repository

import com.example.actividad2.data.remote.model.CardModel

interface ICardRepository {
    suspend fun getAllCards(): Result<List<CardModel>>
}