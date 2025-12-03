package com.example.realmsindiscord.data.remote.api

import com.example.realmsindiscord.data.remote.request.LoginRequest
import com.example.realmsindiscord.data.remote.request.RegisterRequest
import com.example.realmsindiscord.data.remote.request.UpdateUsernameRequest
import com.example.realmsindiscord.data.remote.request.response.AuthResponse
import com.example.realmsindiscord.data.remote.request.response.DeleteUserResponse
import com.example.realmsindiscord.data.remote.request.response.UpdateUsernameResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("api/auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse


    @PUT("api/auth/update-username")
    suspend fun updateUsername(@Body request: UpdateUsernameRequest): UpdateUsernameResponse


    @DELETE("api/auth/user")
    suspend fun deleteUser(@Query("username") username: String): DeleteUserResponse
}