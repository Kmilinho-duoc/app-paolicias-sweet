package com.example.paoliciassweet.model

import java.text.NumberFormat
import java.util.Locale

data class ProductoModel(
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val categoria: String,
    val precio: Int,
    val porPersona: Boolean,
    val emoji: String,
    val colorFondo: Long,
    val tarifario: Map<Int, Int> = emptyMap() // asistentes -> precio por persona
)

// Lista de productos fija, no permite los cambios "en vivo"
val catalogoProductos = listOf(
    ProductoModel(1, "Torta Tres Leches", "Bizcocho húmedo con crema", "Tortas", 28000, false, "🎂", 0xFFFFD9E2),
    ProductoModel(2, "Torta Selva Negra", "Chocolate, cerezas y crema", "Tortas", 32000, false, "🍫", 0xFFE8DEF8),
    ProductoModel(3, "Pastelería fina", "Mini tartaletas y petit fours", "Pastelería", 18000, false, "🧁", 0xFFFFF0C2),
    ProductoModel(4, "Coffee break corporativo", "Para 10 a 100 personas", "Catering", 6500, true, "☕", 0xFFD5F0DC,
        tarifario = mapOf(
            10 to 7500, 20 to 7000, 30 to 6800, 40 to 6600, 50 to 6500,
            60 to 6300, 70 to 6200, 80 to 6100, 90 to 6000, 100 to 5900
        )
    ),
    ProductoModel(5, "Cheesecake de frutilla", "Base crocante y frutos rojos", "Pastelería", 24000, false, "🍰", 0xFFFFD9E2),
)

// Función para mostrar formato de precios correcto (325000 -> "$325.000")
fun Int.aPesos(): String =
    "\$" + NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(this)

