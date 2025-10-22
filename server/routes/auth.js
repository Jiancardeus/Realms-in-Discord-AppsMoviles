const express = require('express');
const router = express.Router();
const User = require('../models/User'); // Importamos el modelo de usuario

// Registro (POST /api/auth/register)
router.post('/register', async (req, res) => {
  try {
    const { username, password, email } = req.body;
    const newUser = new User({ username, password, email });
    await newUser.save();
    res.status(201).json({ message: 'Usuario registrado exitosamente', username: newUser.username });
  } catch (err) {
    if (err.code === 11000) {
        return res.status(409).json({ message: 'El usuario o email ya existe.' });
    }
    res.status(500).json({ message: 'Error interno del servidor.' });
  }
});

// Login (POST /api/auth/login)
router.post('/login', async (req, res) => {
  try {
    const { username, password } = req.body;
    const user = await User.findOne({ username });

    if (!user || user.password !== password) {
      return res.status(401).json({ message: 'Usuario o contraseña incorrecta.' });
    }

    res.status(200).json({ message: 'Login exitoso', username: user.username });
  } catch (err) {
    res.status(500).json({ message: 'Error de servidor.' });
  }
});

module.exports = router;