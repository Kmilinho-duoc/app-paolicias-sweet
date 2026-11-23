package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.model.DetalleModel
import com.example.paoliciassweet.model.catalogoProductos
import kotlinx.coroutines.launch

class DetalleViewModel(
    private val repositorio: ProductoRepository = ProductoRepository()
) : ViewModel() {
    var uiState by mutableStateOf(DetalleModel())
        private set

    fun cargarProducto(id: Int) {
        if (uiState.producto?.id == id) return // ya cargado, no reiniciar la cantidad
        catalogoProductos.firstOrNull { it.id == id }?.let {
            uiState = DetalleModel(producto = it, cantidad = it.cantidadInicial)
        }
        viewModelScope.launch {
            val producto = repositorio.obtener(id) ?: return@launch
            uiState = uiState.copy(producto = producto) // actualiza datos sin tocar la cantidad elegida
        }
    }

    // personas de 10 en 10 (10 a 100) o unidades de 1 en 1 (1 a 10)
    fun onMas() {
        val producto = uiState.producto ?: return
        uiState = uiState.copy(cantidad = minOf(producto.cantidadMaxima, uiState.cantidad + producto.paso))
    }

    fun onMenos() {
        val producto = uiState.producto ?: return
        uiState = uiState.copy(cantidad = maxOf(producto.cantidadMinima, uiState.cantidad - producto.paso))
    }
}
