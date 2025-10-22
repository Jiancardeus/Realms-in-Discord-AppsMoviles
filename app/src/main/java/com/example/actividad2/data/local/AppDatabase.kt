package com.example.actividad2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.actividad2.data.model.User

// Lista de entidades y versión
@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // Define los Data Access Objects (DAOs) que Room debe implementar
    abstract fun userDao(): UserDao
}