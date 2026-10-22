package com.example.paoliciassweet.model

data class ReservaModel(
    val producto: ProductoModel? = null,
    val fecha: String = "",
    val hora: String = "",
    val asistentes: Int = 50,
    val observaciones: String = "",

    // Errores únicos
    val fechaError: String? = null,
    val horaError: String? = null
) {
    val total: Int
        get() = producto?.let { if (it.porPersona) it.precio * asistentes else it.precio } ?: 0

    val abono: Int
        get() = total / 2

    val detalle: String
        get() = producto?.let { if (it.porPersona) "${it.nombre} × $asistentes personas" else it.nombre } ?: ""
}
