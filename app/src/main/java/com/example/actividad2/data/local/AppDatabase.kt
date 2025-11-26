package com.example.actividad2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.actividad2.data.model.User
import com.example.actividad2.data.model.Card // AÑADIR ESTA IMPORTACIÓN

@Database(
    entities = [User::class, Card::class], // AÑADIR Card::class aquí
    version = 2, // Incrementar versión por el cambio
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao // AÑADIR este método
}