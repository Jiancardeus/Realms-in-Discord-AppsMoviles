package com.example.actividad2.di

import com.example.actividad2.data.remote.AuthApiService
import com.example.actividad2.data.remote.CardApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // URL BASE de tu servidor Express
    // 10.0.2.2 es el alias especial para 'localhost' en el emulador de Android
    private const val BASE_URL = "http://10.0.2.2:5000/"

    // 1. Provee el Interceptor para logging (ver peticiones en Logcat)
    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            // Muestra el cuerpo completo de la petición y la respuesta
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    // 2. Provee el Cliente OkHttpClient (para manejar las peticiones)
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    // 3. Provee la instancia de Retrofit
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create()) // Conversión automática de JSON a Kotlin
            .build()
    }

    // 4. Provee el servicio de autenticación (Login/Register)
    @Provides
    @Singleton
    fun provideAuthApiService(retrofit: Retrofit): AuthApiService {
        return retrofit.create(AuthApiService::class.java)
    }

    // 5. Provee el servicio de cartas (Biblioteca)
    @Provides
    @Singleton
    fun provideCardApiService(retrofit: Retrofit): CardApiService {
        return retrofit.create(CardApiService::class.java)
    }
}