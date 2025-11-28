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

    private const val MAIN_BASE_URL = "http://10.0.2.2:5000/"
    private const val USER_BASE_URL = "http://192.168.1.15:5001/"

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
    @Named("mainRetrofit")
    fun provideMainRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MAIN_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // NUEVO: Retrofit para el microservicio
    @Provides
    @Singleton
    @Named("userRetrofit")
    fun provideUserRetrofit(okHttpClient: OkHttpClient): Retrofit {
        println("DEBUG: Creando Retrofit para microservicio en: $USER_BASE_URL")
        return Retrofit.Builder()
            .baseUrl(USER_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Servicios del servidor principal (EXISTENTES - NO TOCAR)
    @Provides
    @Singleton
    fun provideAuthApiService(@Named("mainRetrofit") mainRetrofit: Retrofit): AuthApiService {
        return mainRetrofit.create(AuthApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideCardApiService(@Named("mainRetrofit") mainRetrofit: Retrofit): CardApiService {
        return mainRetrofit.create(CardApiService::class.java)
    }

    // NUEVO: Servicio idéntico pero apuntando al microservicio
    @Provides
    @Singleton
    @Named("userMicroservice")
    fun provideUserMicroserviceApi(@Named("userRetrofit") userRetrofit: Retrofit): AuthApiService {
        return userRetrofit.create(AuthApiService::class.java)
    }
}