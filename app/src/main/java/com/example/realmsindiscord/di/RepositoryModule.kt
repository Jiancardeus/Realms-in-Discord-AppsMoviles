package com.example.realmsindiscord.di

import com.example.realmsindiscord.data.local.UserDao
import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.api.CardApiService
import com.example.realmsindiscord.data.repository.CardRepositoryImpl
import com.example.realmsindiscord.data.repository.DeckRepositoryImpl
import com.example.realmsindiscord.data.repository.UserRepositoryImpl
import com.example.realmsindiscord.domain.repository.ICardRepository
import com.example.realmsindiscord.domain.repository.IDeckRepository
import com.example.realmsindiscord.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        apiService: AuthApiService,                          // Del mainRetrofit
        @Named("userMicroservice") microserviceApi: AuthApiService, // Del userRetrofit
        userDao: UserDao,                                    // Del DatabaseModule
        sessionManager: com.example.realmsindiscord.data.local.SessionManager // Del DatabaseModule
    ): IUserRepository {
        return UserRepositoryImpl(apiService, sessionManager, microserviceApi, userDao)
    }

    @Provides
    @Singleton
    fun provideCardRepository(
        cardApiService: CardApiService // Del mainRetrofit
    ): ICardRepository {
        return CardRepositoryImpl(cardApiService)
    }

    @Provides
    @Singleton
    fun provideDeckRepository(): IDeckRepository {
        return DeckRepositoryImpl()
    }
}