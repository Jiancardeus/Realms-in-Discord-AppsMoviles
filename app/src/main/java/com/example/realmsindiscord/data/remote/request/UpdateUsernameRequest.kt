package com.example.realmsindiscord.data.remote.request

data class UpdateUsernameRequest(
    val currentUsername: String,
    val newUsername: String
)