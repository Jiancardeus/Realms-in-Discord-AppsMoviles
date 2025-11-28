const mongoose = require('mongoose');
const CardSchema = new mongoose.Schema({
    id: {
        type: String,
        required: true,
        unique: true
    },
    name: {
        type: String,
        required: true,
    },
    cost: {
        type: Number,
        required: true,
        default: 0
    },
    attack: {
        type: Number,
        default: 0
    },
    defense: {  // ← ESTE es el campo que usamos como "health"
        type: Number,
        default: 0
    },
    health: {   // ← Este campo podría no existir o ser diferente
        type: Number,
        default: 0
    },
    type: {
        type: String,
        required: true,
    },
    faction: {
        type: String,
        required: true,
        default: "Neutral"
    },
    description: {
        type: String,
        default: "Sin descripción"
    },
    imageUrl: {
        type: String,
        required: true,
    }
});
module.exports = mongoose.model('Card', CardSchema);