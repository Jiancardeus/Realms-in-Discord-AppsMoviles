package com.example.realmsindiscord.di

import com.example.realmsindiscord.data.remote.api.AuthApiService
import com.example.realmsindiscord.data.remote.api.CardApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {



    private const val BASE_URL = "http://52.91.119.27:5001/"

    @Provides
    @Singleton
    @Named("userMicroservice")
    fun provideUserMicroserviceApi(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        println("DEBUG: ✅ Retrofit configurado en: $BASE_URL")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)  // ← MISMA URL PARA TODO
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideCardApiService(retrofit: Retrofit): CardApiService {
        return retrofit.create(CardApiService::class.java)
    }


    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideDeckApiService(retrofit: Retrofit): com.example.realmsindiscord.data.remote.api.DeckApiService {
        return retrofit.create(com.example.realmsindiscord.data.remote.api.DeckApiService::class.java)
    }
}
