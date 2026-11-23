package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.data.repository.ProductoRepository
import kotlinx.coroutines.launch

// Rol Administrador: lista de productos del microservicio, con opción de eliminar
class AdminProductosViewModel(
    private val repositorio: ProductoRepository = ProductoRepository()
) : ViewModel() {
    var lista by mutableStateOf<List<ProductoDto>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun cargar() {
        viewModelScope.launch {
            repositorio.listarRemoto()
                .onSuccess {
                    lista = it.sortedBy { producto -> producto.nombre }
                    mensaje = null
                }
                .onFailure { mensaje = "No se pudo conectar con el servidor." }
        }
    }

    // Eliminar (DELETE)
    fun eliminar(id: Int) {
        viewModelScope.launch {
            repositorio.eliminar(id)
                .onSuccess { cargar() }
                .onFailure { mensaje = "No se pudo eliminar el producto." }
        }
    }
}
