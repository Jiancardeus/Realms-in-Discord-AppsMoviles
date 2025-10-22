const mongoose = require('mongoose');

const CardSchema = new mongoose.Schema({
    // Utilizamos el nombre de la carta como identificador unico
    id: {
        type: String,
        required: true,
        unique: true
    },
    name: {
        type: String,
        required: true,
    },
    type: { // Héroe, Tropa
        type: String,
        required: true,
    },
    description: {
        type: String,
    },
    attack: {
        type: Number,
        default: 0
    },
    defense: {
        type: Number,
        default: 0
    },
    imageUrl: { // Nombre del archivo de imagen (ej: "heroe_1_1.png")
        type: String,
        required: true,
    }
});

module.exports = mongoose.model('Card', CardSchema);