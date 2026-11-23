package com.example.paoliciassweet.model

// Una orden dentro del carrito: un producto y su cantidad (personas o unidades)
data class ItemCarritoModel(
    val producto: ProductoModel,
    val cantidad: Int
) {
    val subtotal: Int
        get() = if (producto.porPersona) (producto.tarifario[cantidad] ?: producto.precio) * cantidad
        else producto.precio * cantidad

    val detalle: String
        get() = if (producto.porPersona) "$cantidad personas"
        else if (cantidad == 1) "1 unidad" else "$cantidad unidades"
}
