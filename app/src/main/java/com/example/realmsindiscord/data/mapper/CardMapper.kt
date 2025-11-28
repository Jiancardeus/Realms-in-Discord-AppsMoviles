package com.example.realmsindiscord.data.mapper

import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.data.remote.model.CardModel

object CardMapper {

    // Mapear de CardModel (API) a Card (Base de datos local)
    fun toLocalCard(cardModel: CardModel): Card {
        return Card(
            id = cardModel.mongoId,
            name = cardModel.name,
            cost = cardModel.cost,
            attack = cardModel.attack,
            health = cardModel.health,
            type = cardModel.type,
            faction = cardModel.faction,
            description = cardModel.description,
            imageResId = mapImageUrlToResId(cardModel.imageUrl) // ✅ MAPEAR IMAGEN
        )
    }

    // Mapear de Card (Base de datos local) a CardModel (UI)
    fun toCardModel(card: Card): CardModel {
        return CardModel(
            mongoId = card.id,
            name = card.name,
            type = card.type,
            description = card.description,
            attack = card.attack,
            defense = 0,
            health = card.health,
            cost = card.cost,
            faction = card.faction ?: "Neutral",
            imageUrl = mapResIdToImageUrl(card.imageResId) // ✅ MAPEAR INVERSA
        )
    }

    // Función para mapear imageUrl a imageResId
    private fun mapImageUrlToResId(imageUrl: String): Int {
        return when {
            imageUrl.contains("espadachin_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.espadachin_solar
            imageUrl.contains("sacerdote_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.sacerdote_solar
            imageUrl.contains("porta_estandarte", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.porta_estandarte
            imageUrl.contains("alabardero_real", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.alabardero_real
            imageUrl.contains("heroe_solar", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.heroe_solar
            imageUrl.contains("rey_paladin", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.rey_paladin
            imageUrl.contains("mago_de_batalla", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.mago_de_batalla
            imageUrl.contains("avatar_de_la_luz", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.avatar_de_la_luz

            // Cartas de Corrupción
            imageUrl.contains("acolito_putrido", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.acolito_putrido
            imageUrl.contains("huevo_de_la_podredumbre", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.huevo_de_la_podredumbre
            imageUrl.contains("sin_luz", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.sin_luz
            imageUrl.contains("amalgama_de_putrefaccion", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.amalgama_de_putrefaccion
            imageUrl.contains("neuro_amalgama", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.neuro_amalgama
            imageUrl.contains("corruptores", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.corruptores
            imageUrl.contains("gran_devorador", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.gran_devorador
            imageUrl.contains("senor_de_la_podredumbre", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.senor_de_la_podredumbre
            imageUrl.contains("gran_cancer", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.gran_cancer
            imageUrl.contains("miasma_toxica", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.miasma_toxica
            imageUrl.contains("quistes", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.quistes
            imageUrl.contains("campo_de_corrupcion", ignoreCase = true) -> com.example.realmsindiscord.R.drawable.campo_de_corrupcion

            else -> 0 // Imagen por defecto o placeholder
        }
    }

    // Función inversa para debugging
    private fun mapResIdToImageUrl(imageResId: Int): String {
        // Esto es principalmente para debugging
        return when (imageResId) {
            com.example.realmsindiscord.R.drawable.espadachin_solar -> "espadachin_solar"
            com.example.realmsindiscord.R.drawable.sacerdote_solar -> "sacerdote_solar"
            // ... agregar más según necesites
            else -> "unknown"
        }
    }
}