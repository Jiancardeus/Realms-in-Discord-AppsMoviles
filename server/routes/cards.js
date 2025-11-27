const express = require('express');
const router = express.Router();
const Card = require('../models/Card');

// Obtener todas las cartas
router.get('/', async (req, res) => {
    try {
        const cards = await Card.find();
        console.log(`📊 Enviando ${cards.length} cartas al cliente`);
        return res.status(200).json(cards);
    } catch (error) {
        console.error("❌ Error al obtener cartas:", error);
        return res.status(500).json({ message: 'Error interno del servidor al obtener cartas.' });
    }
});

// Endpoint para debug - verificar cartas problemáticas
router.get('/debug', async (req, res) => {
    try {
        const cards = await Card.find();

        // Encontrar cartas con faction null
        const problematicCards = cards.filter(card => !card.faction);
        const cardsWithFaction = cards.filter(card => card.faction);

        console.log(`📊 Total cartas: ${cards.length}`);
        console.log(`✅ Cartas CON faction: ${cardsWithFaction.length}`);
        console.log(`❌ Cartas SIN faction: ${problematicCards.length}`);

        if (problematicCards.length > 0) {
            console.log("🚨 Cartas problemáticas:", problematicCards.map(c => ({ id: c.id, name: c.name })));
        }

        res.json({
            totalCards: cards.length,
            cardsWithFaction: cardsWithFaction.length,
            cardsWithoutFaction: problematicCards.length,
            problematicCards: problematicCards.map(c => ({ id: c.id, name: c.name, faction: c.faction }))
        });
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
});

module.exports = router;