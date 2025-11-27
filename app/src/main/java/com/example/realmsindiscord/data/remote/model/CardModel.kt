package com.example.realmsindiscord.data.remote.model

import com.google.gson.annotations.SerializedName

data class CardModel(
    @SerializedName("_id") val mongoId: String,
    @SerializedName("name") val name: String,
    @SerializedName("type") val type: String,
    @SerializedName("description") val description: String,
    @SerializedName("attack") val attack: Int = 0,
    @SerializedName("defense") val defense: Int = 0,
    @SerializedName("imageUrl") val imageUrl: String,
    @SerializedName("cost") val cost: Int = 0,
    @SerializedName("health") val health: Int,
    @SerializedName("faction") val faction: String
)