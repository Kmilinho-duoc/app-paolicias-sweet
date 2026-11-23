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
    val tarifario: Map<Int, Int> = emptyMap(), // asistentes -> precio por persona
    val tieneFoto: Boolean = false,

    // Información para la pantalla de detalle
    val imagen: String? = null, // nombre del archivo en res/drawable, ej: "torta_tres_leches" (si es null se muestra el emoji)
    val ingredientes: List<String> = emptyList(),
    val rinde: String = "",
    val alergenos: String = "",
    val anticipacion: String = ""
) {
    // Reglas de cantidad: personas (de 10 en 10) o unidades (de 1 en 1)
    val cantidadMinima: Int get() = if (porPersona) 10 else 1
    val cantidadMaxima: Int get() = if (porPersona) 100 else 10
    val paso: Int get() = if (porPersona) 10 else 1
    val cantidadInicial: Int get() = if (porPersona) 50 else 1
}

// Lista de productos fija, no permite los cambios "en vivo"
// (contenido de ejemplo: editar ingredientes, porciones, etc. según el negocio)
val catalogoProductos = listOf(
    ProductoModel(1, "Torta Tres Leches", "Bizcocho húmedo con crema", "Tortas", 28000, false, "🎂", 0xFFFFD9E2,
        ingredientes = listOf("Huevos", "Harina", "Azúcar", "Leche entera", "Leche condensada", "Crema de leche", "Esencia de vainilla"),
        imagen = "torta_tres_leches",
        rinde = "12 porciones",
        alergenos = "Gluten, huevo y lácteos",
        anticipacion = "3 días antes del evento"
    ),
    ProductoModel(2, "Torta Selva Negra", "Chocolate, cerezas y crema", "Tortas", 32000, false, "🍫", 0xFFE8DEF8,
        ingredientes = listOf("Cacao amargo", "Harina", "Huevos", "Azúcar", "Crema chantilly", "Cerezas en almíbar", "Chocolate rallado"),
        imagen = "torta_selva_negra",
        rinde = "12 porciones",
        alergenos = "Gluten, huevo y lácteos",
        anticipacion = "3 días antes del evento"
    ),
    ProductoModel(3, "Pastelería fina", "Mini tartaletas y petit fours", "Pastelería", 18000, false, "🧁", 0xFFFFF0C2,
        ingredientes = listOf("Masa quebrada", "Crema pastelera", "Frutas de temporada", "Ganache de chocolate", "Almendras"),
        imagen = "pasteleria_fina",
        rinde = "30 piezas surtidas",
        alergenos = "Gluten, huevo, lácteos y frutos secos",
        anticipacion = "5 días antes del evento"
    ),
    ProductoModel(4, "Coffee break corporativo", "Para 10 a 100 personas", "Catering", 6500, true, "☕", 0xFFD5F0DC,
        tarifario = mapOf(
            10 to 6500, 20 to 6400, 30 to 6300, 40 to 6200, 50 to 6100,
            60 to 6000, 70 to 5900, 80 to 5800, 90 to 5700, 100 to 5600
        ),
        ingredientes = listOf("Café de grano y té", "Mini sándwiches surtidos", "Mini kuchenes", "Galletas artesanales", "Jugo natural"),
        imagen = "coffee_break",
        rinde = "De 10 a 100 personas",
        alergenos = "Gluten y lácteos. Consultar opciones vegetarianas, sin gluten o aptas para hipertensos",
        anticipacion = "7 días antes del evento"
    ),
    ProductoModel(5, "Cheesecake de frutilla", "Base crocante y frutos rojos", "Pastelería", 24000, false, "🍰", 0xFFFFD9E2,
        ingredientes = listOf("Galleta molida", "Mantequilla", "Queso crema", "Crema de leche", "Frutillas", "Azúcar"),
        imagen = "cheese_frutilla",
        rinde = "10 porciones",
        alergenos = "Gluten y lácteos",
        anticipacion = "3 días antes del evento"
    ),
)

// Función para mostrar formato de precios correcto (325000 -> "$325.000")
fun Int.aPesos(): String =
    "\$" + NumberFormat.getIntegerInstance(Locale.forLanguageTag("es-CL")).format(this)
