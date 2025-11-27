package com.example.realmsindiscord.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.data.model.User

@Database(
    entities = [User::class, Card::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cardDao(): CardDao
}