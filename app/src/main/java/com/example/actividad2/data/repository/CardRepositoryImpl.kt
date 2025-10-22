package com.example.actividad2.data.repository

import com.example.actividad2.data.remote.CardApiService
import com.example.actividad2.data.remote.model.CardModel
import com.example.actividad2.domain.repository.ICardRepository
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
            // Llama a la API en el hilo de IO (entrada/salida)
            val cards = apiService.getCards()
            Result.success(cards)
        } catch (e: HttpException) {
            // Error HTTP (ej: 404, 500)
            Result.failure(e)
        } catch (e: IOException) {
            // Error de red (sin conexión a Internet)
            Result.failure(e)
        } catch (e: Exception) {
            // Otros errores
            Result.failure(e)
        }
    }
}