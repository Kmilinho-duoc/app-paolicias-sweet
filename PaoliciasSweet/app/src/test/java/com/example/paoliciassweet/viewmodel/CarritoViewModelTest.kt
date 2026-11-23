package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.ui.viewmodel.CarritoViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

class CarritoViewModelTest {
    private val torta = ProductoModel(1, "Torta", "desc", "Tortas", 28000, false, "🎂", 0xFFFFFFFF)
    private val coffee = ProductoModel(4, "Coffee", "desc", "Catering", 6500, true, "☕", 0xFFFFFFFF)

    @Test
    fun agregaOrdenes() {
        val vm = CarritoViewModel()
        vm.agregar(torta, 1)
        vm.agregar(coffee, 50)
        assertEquals(2, vm.uiState.cantidadItems)
        assertEquals(28000 + 325000, vm.uiState.total)
    }

    @Test
    fun agregarElMismoProductoReemplazaLaCantidad() {
        val vm = CarritoViewModel()
        vm.agregar(torta, 1)
        vm.agregar(torta, 3)
        assertEquals(1, vm.uiState.cantidadItems)
        assertEquals(3, vm.uiState.items.first().cantidad)
    }

    @Test
    fun quitaUnaOrden() {
        val vm = CarritoViewModel()
        vm.agregar(torta, 1)
        vm.agregar(coffee, 10)
        vm.quitar(1)
        assertEquals(1, vm.uiState.cantidadItems)
        assertEquals(4, vm.uiState.items.first().producto.id)
    }

    @Test
    fun vaciaElCarrito() {
        val vm = CarritoViewModel()
        vm.agregar(torta, 1)
        vm.vaciar()
        assertEquals(0, vm.uiState.cantidadItems)
    }
}
