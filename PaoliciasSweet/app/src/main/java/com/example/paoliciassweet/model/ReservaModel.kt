package com.example.paoliciassweet.model

// Datos del evento que escribe el cliente (las órdenes vienen del carrito)
data class ReservaModel(
    val fecha: String = "",
    val hora: String = "",
    val observaciones: String = "",
    val clima: String? = null,        // pronóstico de la API externa para la fecha elegida
    val errorEnvio: String? = null,   // mensaje si el microservicio no responde

    // Errores únicos
    val fechaError: String? = null,
    val horaError: String? = null
)
