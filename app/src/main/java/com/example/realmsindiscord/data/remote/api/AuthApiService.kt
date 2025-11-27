package com.example.realmsindiscord.data.remote.api

import com.example.realmsindiscord.data.remote.request.LoginRequest
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.remote.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse
}