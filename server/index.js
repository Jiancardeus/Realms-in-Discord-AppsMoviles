const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

// --- Importar Rutas ---
// Nota: Eliminamos la importación del modelo 'User' aquí, ya que se usa dentro de las rutas.
const authRoutes = require('./routes/auth');
const cardRoutes = require('./routes/cards');

const app = express();
const PORT = process.env.PORT || 5000;

// --- Middlewares ---
app.use(cors());
app.use(express.json());

// --- Conexión a MongoDB Atlas ---
// Esta es tu URL de conexión real, la dejamos aquí.
const MONGODB_URI = 'mongodb+srv://<app_admin>:<app123>@app.w280lxg.mongodb.net/?appName=app';

mongoose.connect(MONGODB_URI)
  .then(() => console.log('✅ Conectado a MongoDB Atlas'))
  .catch(err => console.error('❌ Error de conexión a MongoDB:', err));


// --- Integración de Rutas Modulares ---

// 1. Rutas de Autenticación (Login y Registro)
app.use('/api/auth', authRoutes);

// 2. Rutas de Cartas (Biblioteca)
app.use('/api/cards', cardRoutes);


// --- Endpoint de Prueba ---
// Endpoint simple para verificar que el servidor está activo
app.get('/', (req, res) => {
    res.status(200).json({ message: 'API de Realms in Discord TCG está activa.' });
});


// --- Inicio del Servidor ---
app.listen(PORT, () => {
  console.log(`Servidor de backend corriendo en http://localhost:${PORT}`);
});