package com.example.actividad2.data.local

import com.example.actividad2.R
import com.example.actividad2.data.model.Card

// Lista estatica de cartas usando IDs de recursos (R.drawable)
fun getInitialCards(): List<Card> {
    return listOf(
        // CARTAS DE LA FACCION: CABALLEROS SOLARES
        Card(
            id = "CS001",
            name = "Espadachín Solar",
            cost = 2, attack = 3, health = 2,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Tropa Comun",
            imageResId = R.drawable.espadachin_solar
        ),
        Card(
            id = "CS002",
            name = "Sacerdote Solar",
            cost = 3, attack = 1, health = 4,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Al final de cada turno, helea +1 PS a la tropa más cercana",
            imageResId = R.drawable.sacerdote_solar
        ),
        Card(
            id = "CS003",
            name = "Porta Estandarte",
            cost = 4, attack = 3, health = 4,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Tropas cercanas obtienen +1 AD",
            imageResId = R.drawable.porta_estandarte
        ),
        Card(
            id = "CS004",
            name = "Alabardero Real",
            cost = 5, attack = 5, health = 4,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Puede atacar a múltiples objetivos",
            imageResId = R.drawable.alabardero_real
        ),
        Card(
            id = "CS005",
            name = "Héroe Solar",
            cost = 4, attack = 4, health = 5,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Unidad de ataque balanceada.",
            imageResId = R.drawable.heroe_solar
        ),
        Card(
            id = "CS006",
            name = "Rey Paladín",
            cost = 7, attack = 5, health = 7,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "En toda su fila genera estado de 'SEGURIDAD' + 1 AR/MR",
            imageResId = R.drawable.rey_paladin
        ),
        Card(
            id = "CS007",
            name = "Mago de Batalla",
            cost = 3, attack = 3, health = 3,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Al aparecer en el mazo quita -1PS a la carta que esté en casilla espejo",
            imageResId = R.drawable.mago_de_batalla
        ),
        Card(
            id = "CS008",
            name = "Avatar de la Luz",
            cost = 10, attack = 8, health = 8,
            type = "Tropa",
            faction = "Caballeros Solares",
            description = "Estado permanente de 'INMUNE', no puede atacar en primer turno, no recibe daño de magias",
            imageResId = R.drawable.avatar_de_la_luz
        ),

        // CARTAS DE LA FACCIÓN: CORRUPCIÓN

        Card(
            id = "CC001",
            name = "Acólito Pútrido",
            cost = 1, attack = 2, health = 1,
            type = "Tropa",
            faction = "Corrupción",
            description = "Al morir, inflige Silencio a una carta del rival.",
            imageResId = R.drawable.acolito_putrido
        ),
        Card(
            id = "CC002",
            name = "Huevo de la Podredumbre",
            cost = 1, attack = 0, health = 5,
            type = "Tropa",
            faction = "Corrupción",
            description = "Un contador de 2 turnos que al llegar a 0 esta carta se destruye y agrega 1 maná máximo.",
            imageResId = R.drawable.huevo_de_la_podredumbre
        ),
        Card(
            id = "CC003",
            name = "Sin Luz",
            cost = 2, attack = 3, health = 1,
            type = "Tropa",
            faction = "Corrupción",
            description = "Una criatura vacía, un heraldo del fin.",
            imageResId = R.drawable.sin_luz
        ),
        Card(
            id ="CC004",
            name = "Amalgama de Putrefacción",
            cost = 3, attack = 1, health = 5,
            type = "Tropa",
            faction = "Corrupción",
            description = "Habilidad: Vigilancia",
            imageResId = R.drawable.amalgama_de_putrefaccion
        ),
        Card(
            id = "CC005",
            name = "Neuro Amalgama",
            cost = 2, attack = 1, health = 2,
            type = "Tropa",
            faction = "Corrupción",
            description = "Cura 1 a todos los aliados a su alrededor en diagonal, menos al líder",
            imageResId = R.drawable.neuro_amalgama
        ),
        Card(
            id = "CC006",
            name = "Corruptores",
            cost =2, attack = 1, health = 1,
            type = "Tropa",
            faction = "Corrupción",
            description = "Puedes seleccionar una carta aliada y destruirla, añade 3 al ataque de esta carta",
            imageResId = R.drawable.corruptores
        ),
        Card(
            id = "CC007",
            name = "Gran Devorador",
            cost = 5, attack = 7, health = 5,
            type = "Tropa",
            faction = "Corrupción",
            description = "Cada vez que esta carta es dañada aumenta en +1 el daño",
            imageResId = R.drawable.gran_devorador
        ),
        Card(
            id = "CC008",
            name = "Señor de la Podredumbre",
            cost = 7, attack = 5, health = 5,
            type = "Tropa",
            faction = "Corrupción",
            description = "Trae a la mano todas las cartas que han muerto en tu mazo, estas quedan con el rasgo 'efímero', disminuye el coste en 2",
            imageResId = R.drawable.senor_de_la_podredumbre
        ),
        Card(
            id = "CC009",
            name = "Gran Cáncer",
            cost = 0, attack = 3, health = 30,
            type = "Líder",
            faction = "Corrupción",
            description = "Por cada carta aliada muerta en combate, genera un sin luz. Evoluciona: si han muerto 10+ aliados. Habilidad evolucionada: todas tus cartas en mano tienen 2 menos al coste de maná.",
            imageResId = R.drawable.gran_cancer
        ),
        Card(
            id = "CC010",
            name = "Miasma Tóxica",
            cost =3, attack = 0, health = 0,
            type = "Hechizo",
            faction = "Corrupción",
            description = "Oculta tus cartas del rival durante 2 turnos, durante estos turnos el rival al atacar recibe daño adicional de 2",
            imageResId = R.drawable.miasma_toxica
        ),
        Card(
            id = "CC011",
            name = "Quistes",
            cost = 3, attack = 0, health = 0,
            type = "Hechizo",
            faction = "Corrupción",
            description = "Causa 1 de daño a 3 cartas aleatorias de tu mesa",
            imageResId = R.drawable.quistes
        ),
        Card(
            id ="CC012",
            name = "Campo de Corrupción",
            cost = 2, attack = 0, health = 0,
            type =  "Orden",
            faction = "Corrupción",
            description = "Roba 5 cartas del mazo y escoge una para dejarla en tu mano, si en las 5 cartas hay 'huevo de la podredumbre' inmediatamente se coloca en el campo en la zona de ataque",
            imageResId = R.drawable.campo_de_corrupcion
        ),
    )
}