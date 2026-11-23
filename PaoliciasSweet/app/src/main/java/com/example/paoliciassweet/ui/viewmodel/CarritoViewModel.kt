package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paoliciassweet.model.CarritoModel
import com.example.paoliciassweet.model.ItemCarritoModel
import com.example.paoliciassweet.model.ProductoModel

// Se comparte entre pantallas (catálogo, detalle y reserva): lo crea AppNavigation una sola vez
class CarritoViewModel : ViewModel() {
    var uiState by mutableStateOf(CarritoModel())
        private set

    // si el producto ya estaba en el carrito, se reemplaza por la nueva cantidad
    fun agregar(producto: ProductoModel, cantidad: Int) {
        val resto = uiState.items.filter { it.producto.id != producto.id }
        uiState = uiState.copy(items = resto + ItemCarritoModel(producto, cantidad))
    }

    fun quitar(productoId: Int) {
        uiState = uiState.copy(items = uiState.items.filter { it.producto.id != productoId })
    }

    fun vaciar() {
        uiState = CarritoModel()
    }
}
