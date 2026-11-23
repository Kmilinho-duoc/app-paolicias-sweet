package com.example.paoliciassweet.model

// Estado de la pantalla de detalle: producto visto y cantidad elegida
data class DetalleModel(
    val producto: ProductoModel? = null,
    val cantidad: Int = 1
) {
    val subtotal: Int
        get() = producto?.let { ItemCarritoModel(it, cantidad).subtotal } ?: 0
}
