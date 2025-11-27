// updateCards.js - Script para actualizar las cartas existentes
const mongoose = require('mongoose');
const Card = require('./models/Card');

const MONGODB_URI = 'mongodb+srv://admin_jian:jiandios@app.w280lxg.mongodb.net/?appName=app';

async function updateCards() {
    try {
        console.log("🔗 Conectando a MongoDB...");
        await mongoose.connect(MONGODB_URI);
        console.log("✅ Conectado a MongoDB");

        // Obtener todas las cartas
        const cards = await Card.find();
        console.log(`📊 Encontradas ${cards.length} cartas para actualizar`);

        let updatedCount = 0;

        for (const card of cards) {
            // Definir facción basada en el ID de la carta
            let faction = "Neutral";
            let cost = 0;
            let health = card.defense || 0; // Migrar defense → health

            if (card.id.startsWith('CS')) {
                faction = "Caballeros Solares";
                // Asignar costos basados en el ID
                cost = parseInt(card.id.replace('CS', '')) * 1.5 || 3;
            } else if (card.id.startsWith('CC')) {
                faction = "Corrupción";
                cost = parseInt(card.id.replace('CC', '')) * 1.5 || 3;
            }

            // Actualizar la carta con los nuevos campos
            await Card.updateOne(
                { _id: card._id },
                {
                    $set: {
                        faction: faction,
                        cost: Math.max(1, Math.round(cost)),
                        health: health
                    },
                    $unset: { defense: "" } // Eliminar el campo antiguo
                }
            );

            updatedCount++;
            console.log(`✅ Actualizada: ${card.name} - Facción: ${faction}, Costo: ${cost}, Health: ${health}`);
        }

        console.log(`🎉 Actualización completada: ${updatedCount} cartas actualizadas`);
        process.exit(0);

    } catch (error) {
        console.error('❌ Error durante la actualización:', error);
        process.exit(1);
    }
}

updateCards();