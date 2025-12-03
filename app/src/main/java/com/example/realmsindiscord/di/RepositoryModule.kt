package com.example.realmsindiscord.di

import android.content.Context
import com.example.realmsindiscord.data.local.CardDao
import com.example.realmsindiscord.data.local.UserDao
import com.example.realmsindiscord.data.local.SessionManager
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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(
        @Named("userMicroservice") microserviceApi: AuthApiService,
        sessionManager: SessionManager,
        userDao: UserDao //  Este parámetro está disponible
    ): IUserRepository {
        return UserRepositoryImpl(
            microserviceApi,
            sessionManager,
            userDao
        )
    }

    @Provides
    @Singleton
    fun provideCardRepository(
        cardApiService: CardApiService,
        cardDao: CardDao,
        @ApplicationContext context: Context
    ): ICardRepository {
        return CardRepositoryImpl(
            cardApiService,
            cardDao,
            context
        )
    }

    @Provides
    @Singleton
    fun provideDeckRepository(): IDeckRepository {
        return DeckRepositoryImpl()
    }
}