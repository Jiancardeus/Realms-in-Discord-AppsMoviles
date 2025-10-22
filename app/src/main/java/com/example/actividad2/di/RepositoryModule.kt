package com.example.actividad2.di

import com.example.actividad2.data.repository.CardRepositoryImpl
import com.example.actividad2.data.repository.UserRepository
import com.example.actividad2.domain.repository.ICardRepository
import com.example.actividad2.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // 1. Vinculación del Repositorio de Usuarios
    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    // 2. Vinculación del Repositorio de Cartas

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        cardRepositoryImpl: CardRepositoryImpl // <-- Usar la clase de implementación real
    ): ICardRepository // <-- Retornar la interfaz
}