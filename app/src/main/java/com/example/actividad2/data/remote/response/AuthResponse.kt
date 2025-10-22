package com.example.actividad2.data.remote.response

// Modela la respuesta JSON que viene de tu servidor Express
data class AuthResponse(
    // Mensaje de éxito o error (ej: "Login exitoso", "Usuario no encontrado")
    val message: String,

    // El nombre de usuario que el servidor te devuelve si el login fue exitoso.
    // Es opcional, ya que puede estar ausente en una respuesta de error.
    val username: String? = null
)