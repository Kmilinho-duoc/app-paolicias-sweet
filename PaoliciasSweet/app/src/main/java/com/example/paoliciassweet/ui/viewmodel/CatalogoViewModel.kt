package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.model.catalogoProductos

class CatalogoViewModel : ViewModel() {
    val categorias = listOf("Todos", "Tortas", "Pastelería", "Catering")

    var busqueda by mutableStateOf("")
        private set
    var categoria by mutableStateOf("Todos")
        private set

    // se recalcula solo cuando cambia la búsqueda o la categoría
    val productos: List<ProductoModel>
        get() {
            val texto = busqueda.trim()
            return catalogoProductos.filter {
                (categoria == "Todos" || it.categoria == categoria) &&
                    (it.nombre.contains(texto, true) ||
                        it.descripcion.contains(texto, true) ||
                        it.categoria.contains(texto, true))
            }
        }

    fun onBusquedaChanged(texto: String) { busqueda = texto }
    fun onCategoriaChanged(nueva: String) { categoria = nueva }
}
