package com.example.realmsindiscord.di

import com.example.realmsindiscord.data.repository.CardRepositoryImpl
import com.example.realmsindiscord.data.repository.UserRepository
import com.example.realmsindiscord.domain.repository.ICardRepository
import com.example.realmsindiscord.domain.repository.IUserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepository: UserRepository
    ): IUserRepository

    @Binds
    @Singleton
    abstract fun bindCardRepository(
        cardRepositoryImpl: CardRepositoryImpl  // Hilt manejará la inyección automática
    ): ICardRepository
}