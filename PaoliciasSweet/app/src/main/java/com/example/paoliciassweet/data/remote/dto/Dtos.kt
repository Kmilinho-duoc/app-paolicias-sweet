package com.example.paoliciassweet.data.remote.dto

import com.example.paoliciassweet.model.ProductoModel
import com.google.gson.annotations.SerializedName


data class ProductoDto(
    val id: Int = 0,
    val nombre: String = "",
    val descripcion: String = "",
    val categoria: String = "",
    val precio: Int = 0,
    val porPersona: Boolean = false,
    val emoji: String = "🍰",
    val colorFondo: Long = 0xFFE8DEF8,
    val tarifario: Map<String, Int> = emptyMap(), // {"10": 6500, "20": 6400, ...}
    val imagen: String? = null,                   // nombre del archivo en res/drawable
    val ingredientes: List<String> = emptyList(),
    val rinde: String = "",
    val alergenos: String = "",
    val anticipacion: String = "",
    val tieneFoto: Boolean = false
) {
    fun aModelo() = ProductoModel(
        id = id, nombre = nombre, descripcion = descripcion, categoria = categoria,
        precio = precio, porPersona = porPersona, emoji = emoji, colorFondo = colorFondo,
        tarifario = tarifario.mapNotNull { (cantidad, precio) -> cantidad.toIntOrNull()?.let { it to precio } }.toMap(),
        imagen = imagen, ingredientes = ingredientes, rinde = rinde,
        alergenos = alergenos, anticipacion = anticipacion,
        tieneFoto = tieneFoto
    )
}

data class ItemReservaDto(
    val producto: String = "",
    val cantidad: Int = 0,
    val detalle: String = "", // ej: "50 personas"
    val subtotal: Int = 0
)

data class ReservaDto(
    val id: Int = 0,
    val clienteEmail: String = "",
    val fecha: String = "",
    val hora: String = "",
    val observaciones: String = "",
    val total: Int = 0,
    val abono: Int = 0,
    val estado: String = "Pendiente", // Pendiente -> Enviado -> Pagado 50%
    val metodoPago: String = "",      // "Transferencia" o "Depósito"
    val items: List<ItemReservaDto> = emptyList()
)

data class EstadoDto(val estado: String)

data class UsuarioDto(
    val nombre: String = "",
    val email: String = "",
    val telefono: String = ""
)

// Respuesta de la API externa del clima (Open-Meteo)
data class ClimaDto(val daily: ClimaDiarioDto? = null)

data class ClimaDiarioDto(
    @SerializedName("temperature_2m_max") val max: List<Double?> = emptyList(),
    @SerializedName("temperature_2m_min") val min: List<Double?> = emptyList(),
    @SerializedName("precipitation_probability_max") val lluvia: List<Int?> = emptyList()
)
