package com.example.realmsindiscord.data.mapper

import com.example.realmsindiscord.data.model.Card
import com.example.realmsindiscord.data.remote.model.CardModel
import com.example.realmsindiscord.R

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
            imageResId = mapImageUrlToResId(cardModel.imageUrl)
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
            imageUrl = mapResIdToImageUrl(card.imageResId)
        )
    }

    // Función para mapear imageUrl a imageResId (String -> Int)
    private fun mapImageUrlToResId(imageUrl: String): Int {
        return when {
            imageUrl.contains("espadachin_solar", ignoreCase = true) -> R.drawable.espadachin_solar
            imageUrl.contains("sacerdote_solar", ignoreCase = true) -> R.drawable.sacerdote_solar
            imageUrl.contains("porta_estandarte", ignoreCase = true) -> R.drawable.porta_estandarte
            imageUrl.contains("alabardero_real", ignoreCase = true) -> R.drawable.alabardero_real
            imageUrl.contains("heroe_solar", ignoreCase = true) -> R.drawable.heroe_solar
            imageUrl.contains("rey_paladin", ignoreCase = true) -> R.drawable.rey_paladin
            imageUrl.contains("mago_de_batalla", ignoreCase = true) -> R.drawable.mago_de_batalla
            imageUrl.contains("avatar_de_la_luz", ignoreCase = true) -> R.drawable.avatar_de_la_luz

            // Cartas de Corrupción
            imageUrl.contains("acolito_putrido", ignoreCase = true) -> R.drawable.acolito_putrido
            imageUrl.contains("huevo_de_la_podredumbre", ignoreCase = true) -> R.drawable.huevo_de_la_podredumbre
            imageUrl.contains("sin_luz", ignoreCase = true) -> R.drawable.sin_luz
            imageUrl.contains("amalgama_de_putrefaccion", ignoreCase = true) -> R.drawable.amalgama_de_putrefaccion
            imageUrl.contains("neuro_amalgama", ignoreCase = true) -> R.drawable.neuro_amalgama
            imageUrl.contains("corruptores", ignoreCase = true) -> R.drawable.corruptores
            imageUrl.contains("gran_devorador", ignoreCase = true) -> R.drawable.gran_devorador
            imageUrl.contains("senor_de_la_podredumbre", ignoreCase = true) -> R.drawable.senor_de_la_podredumbre
            imageUrl.contains("gran_cancer", ignoreCase = true) -> R.drawable.gran_cancer
            imageUrl.contains("miasma_toxica", ignoreCase = true) -> R.drawable.miasma_toxica
            imageUrl.contains("quistes", ignoreCase = true) -> R.drawable.quistes
            imageUrl.contains("campo_de_corrupcion", ignoreCase = true) -> R.drawable.campo_de_corrupcion

            else -> 0
        }
    }

    // Función inversa (Int -> String) para mapear imageResId a imageUrl
    private fun mapResIdToImageUrl(imageResId: Int): String {
        return when (imageResId) {
            R.drawable.espadachin_solar -> "espadachin_solar"
            R.drawable.sacerdote_solar -> "sacerdote_solar"
            R.drawable.porta_estandarte -> "porta_estandarte"
            R.drawable.alabardero_real -> "alabardero_real"
            R.drawable.heroe_solar -> "heroe_solar"
            R.drawable.rey_paladin -> "rey_paladin"
            R.drawable.mago_de_batalla -> "mago_de_batalla"
            R.drawable.avatar_de_la_luz -> "avatar_de_la_luz"

            R.drawable.acolito_putrido -> "acolito_putrido"
            R.drawable.huevo_de_la_podredumbre -> "huevo_de_la_podredumbre"
            R.drawable.sin_luz -> "sin_luz"
            R.drawable.amalgama_de_putrefaccion -> "amalgama_de_putrefaccion"
            R.drawable.neuro_amalgama -> "neuro_amalgama"
            R.drawable.corruptores -> "corruptores"
            R.drawable.gran_devorador -> "gran_devorador"
            R.drawable.senor_de_la_podredumbre -> "senor_de_la_podredumbre"
            R.drawable.gran_cancer -> "gran_cancer"
            R.drawable.miasma_toxica -> "miasma_toxica"
            R.drawable.quistes -> "quistes"
            R.drawable.campo_de_corrupcion -> "campo_de_corrupcion"
            else -> "unknown"
        }
    }
}