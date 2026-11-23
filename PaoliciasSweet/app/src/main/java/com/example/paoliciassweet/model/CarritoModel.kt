package com.example.paoliciassweet.model

// Carrito: todas las órdenes que irán en una sola reserva
data class CarritoModel(
    val items: List<ItemCarritoModel> = emptyList()
) {
    val total: Int
        get() = items.sumOf { it.subtotal }

    // el abono que bloquea la fecha es el 50 %
    val abono: Int
        get() = total / 2

    val cantidadItems: Int
        get() = items.size
}
