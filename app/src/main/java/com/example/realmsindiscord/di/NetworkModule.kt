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

    private const val MAIN_BASE_URL = "http://10.0.2.2:5000/"  // Servidor principal existente
    private const val USER_BASE_URL = "http://10.0.2.2:5001/"  // Nuevo microservicio usuarios

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

    // Retrofit para el servidor principal (EXISTENTE)
    @Provides
    @Singleton
    fun provideMainRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAIN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Retrofit para el microservicio
    @Provides
    @Singleton
    fun provideUserRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicios del servidor principal
    @Provides
    @Singleton
    fun provideAuthApiService(mainRetrofit: Retrofit): AuthApiService {
        return mainRetrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCardApiService(mainRetrofit: Retrofit): CardApiService {
        return mainRetrofit.create(CardApiService::class.java)
    }


    @Provides
    @Singleton
    @Named("userMicroservice")
    fun provideUserMicroserviceApi(userRetrofit: Retrofit): AuthApiService {
        return userRetrofit.create(AuthApiService::class.java)
    }
}