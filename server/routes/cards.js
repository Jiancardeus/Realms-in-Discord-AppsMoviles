const express = require('express');
const router = express.Router();
const Card = require('../models/Card');

// RUTA GET /api/cards
// Obtiene todas las cartas de la coleccion
router.get('/', async (req, res) => {
    try {
        const cards = await Card.find();
        return res.status(200).json(cards);
    } catch (error) {
        console.error("Error al obtener cartas:", error);
        return res.status(500).json({ message: 'Error interno del servidor al obtener cartas.' });
    }
});

module.exports = router;