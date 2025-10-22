package com.example.actividad2.data.remote

import com.example.actividad2.data.remote.request.LoginRequest
import com.example.actividad2.data.remote.request.RegisterRequest
import com.example.actividad2.data.remote.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    // Endpoint de Login
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    // Endpoint de Registro
    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}