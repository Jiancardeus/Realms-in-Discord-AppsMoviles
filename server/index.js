const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

// --- Importar Rutas ---
const authRoutes = require('./routes/auth');
const cardRoutes = require('./routes/cards');

const app = express();
const PORT = process.env.PORT || 5000;

// --- Middlewares ---
app.use(cors());
app.use(express.json());

// --- Conexión a MongoDB Atlas ---
// Esta es tu URL de conexión real, la dejamos aquí.
const MONGODB_URI = process.env.MONGODB_URI || 'mongodb+srv://admin_jian:jiandios@app.w280lxg.mongodb.net/?appName=app';

console.log("🔗 Conectando a MongoDB...");

mongoose.connect(MONGODB_URI)
  .then(() => {
    console.log('✅ Conectado a MongoDB Atlas');

    // Verificar conexión listando bases de datos
    mongoose.connection.db.admin().listDatabases((err, result) => {
      if (err) {
        console.log('❌ Error listando databases:', err);
      } else {
        console.log('📊 Bases de datos disponibles:', result.databases.map(db => db.name));
      }
    });
  })
  .catch(err => {
    console.error('❌ Error de conexión a MongoDB:', err);
    console.error('🔍 Detalles:', err.message);
  });

// --- Integración de Rutas Modulares ---

// 1. Rutas de Autenticación (Login y Registro)
app.use('/api/auth', authRoutes);

// 2. Rutas de Cartas (Biblioteca)
app.use('/api/cards', cardRoutes);

// --- Endpoint de Prueba ---
app.get('/', (req, res) => {
    res.status(200).json({ message: 'API de Realms in Discord TCG está activa.' });
});

// --- Inicio del Servidor ---
app.listen(PORT, () => {
  console.log(`🚀 Servidor de backend corriendo en http://localhost:${PORT}`);
});