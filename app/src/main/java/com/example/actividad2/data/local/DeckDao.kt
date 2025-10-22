package com.example.actividad2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.actividad2.data.model.Card
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    /**
     * Inserta una lista de cartas en la base de datos.
     * Esto se usará para precargar la biblioteca de cartas del juego.
     * Se usa REPLACE para sobrescribir cartas si ya existen (útil para actualizaciones).
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cards: List<Card>)

    /**
     * Obtiene todas las cartas de la base de datos.
     * Se usa Flow para obtener actualizaciones en tiempo real (reactividad).
     */
    @Query("SELECT * FROM cards ORDER BY cost ASC, name ASC")
    fun getAllCards(): Flow<List<Card>>

    /**
     * Obtiene una carta específica por su ID.
     */
    @Query("SELECT * FROM cards WHERE id = :cardId")
    suspend fun getCardById(cardId: String): Card?

    /**
     * Obtiene una lista de cartas basadas en una lista de IDs.
     * Útil para construir un mazo a partir de una lista de IDs guardados.
     */
    @Query("SELECT * FROM cards WHERE id IN (:cardIds)")
    suspend fun getCardsByIds(cardIds: List<String>): List<Card>
}