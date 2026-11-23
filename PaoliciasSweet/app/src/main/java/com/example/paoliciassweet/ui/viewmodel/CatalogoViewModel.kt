package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.model.catalogoProductos
import kotlinx.coroutines.launch

class CatalogoViewModel(
    private val repositorio: ProductoRepository = ProductoRepository()
) : ViewModel() {
    val categorias = listOf("Todos", "Tortas", "Pastelería", "Catering")

    // empieza con la lista local y se reemplaza por la del microservicio cuando responde
    private var catalogo by mutableStateOf(catalogoProductos)

    var busqueda by mutableStateOf("")
        private set
    var categoria by mutableStateOf("Todos")
        private set

    // se recalcula solo cuando cambia la búsqueda o la categoría
    val productos: List<ProductoModel>
        get() {
            val texto = busqueda.trim()
            return catalogo.filter {
                (categoria == "Todos" || it.categoria == categoria) &&
                    (it.nombre.contains(texto, true) ||
                        it.descripcion.contains(texto, true) ||
                        it.categoria.contains(texto, true))
            }
        }

    fun cargarProductos() {
        viewModelScope.launch { catalogo = repositorio.listar() }
    }

    fun onBusquedaChanged(texto: String) { busqueda = texto }
    fun onCategoriaChanged(nueva: String) { categoria = nueva }
}
