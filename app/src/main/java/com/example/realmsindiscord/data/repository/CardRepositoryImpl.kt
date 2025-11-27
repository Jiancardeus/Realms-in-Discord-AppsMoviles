package com.example.realmsindiscord.data.repository

import android.content.Context
import com.example.realmsindiscord.data.local.getInitialCards
import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.domain.repository.ICardRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CardRepositoryImpl @Inject constructor(
    private val apiService: CardApiService
) : ICardRepository {

    override suspend fun getAllCards(): Result<List<CardModel>> = withContext(Dispatchers.IO) {
        try {
            // Intenta obtener cartas de la API
            val cards = apiService.getCards()
            Result.success(cards)
        } catch (e: HttpException) {
            // Error HTTP
            Result.failure(e)
        } catch (e: IOException) {
            // Error de red
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Método para obtener cartas locales (fallback)
    fun getLocalCards(context: Context): List<com.example.realmsindiscord.data.model.Card> {
        return getInitialCards(context)
    }
}