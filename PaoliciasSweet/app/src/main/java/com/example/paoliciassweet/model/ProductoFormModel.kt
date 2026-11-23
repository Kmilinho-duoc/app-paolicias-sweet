package com.example.paoliciassweet.model

// Datos del formulario de producto (el administrador crea o edita un producto)
data class ProductoFormModel(
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "Tortas",
    val precio: String = "",
    val porPersona: Boolean = false,
    val emoji: String = "🍰",
    val ingredientes: String = "", // separados por coma
    val rinde: String = "",
    val alergenos: String = "",
    val anticipacion: String = "",
    val imagen: String = "",       // nombre del archivo en res/drawable (opcional)

    // Errores únicos
    val nombreError: String? = null,
    val descripcionError: String? = null,
    val precioError: String? = null,
    val mensaje: String? = null    // error de conexión
)
