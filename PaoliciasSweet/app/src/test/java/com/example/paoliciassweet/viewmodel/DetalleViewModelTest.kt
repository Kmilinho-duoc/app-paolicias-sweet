package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.MainDispatcherRule
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.ui.viewmodel.DetalleViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class DetalleViewModelTest {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val coffee = ProductoModel(4, "Coffee", "desc", "Catering", 6500, true, "☕", 0xFFFFFFFF)
    private val torta = ProductoModel(1, "Torta", "desc", "Tortas", 28000, false, "🎂", 0xFFFFFFFF)

    private fun crear(vararg productos: ProductoModel): DetalleViewModel {
        val repo = mockk<ProductoRepository>()
        productos.forEach { p -> coEvery { repo.obtener(p.id) } returns p }
        coEvery { repo.obtener(99) } returns null
        return DetalleViewModel(repo)
    }

    @Test
    fun cargaElProductoConSuCantidadInicial() {
        val vm = crear(coffee, torta)
        vm.cargarProducto(4)
        assertEquals(coffee, vm.uiState.producto)
        assertEquals(50, vm.uiState.cantidad)
        vm.cargarProducto(1)
        assertEquals(1, vm.uiState.cantidad)
    }

    @Test
    fun productoInexistenteDejaElEstadoSinProducto() {
        val vm = crear()
        vm.cargarProducto(99)
        assertNull(vm.uiState.producto)
    }

    @Test
    fun cargarDosVecesElMismoProductoNoReiniciaLaCantidad() {
        val vm = crear(coffee)
        vm.cargarProducto(4)
        vm.onMas()
        vm.cargarProducto(4)
        assertEquals(60, vm.uiState.cantidad)
    }

    @Test
    fun personasSubenYBajanDeDiezEnDiezConLimites() {
        val vm = crear(coffee)
        vm.cargarProducto(4)
        repeat(10) { vm.onMas() }
        assertEquals(100, vm.uiState.cantidad)
        repeat(20) { vm.onMenos() }
        assertEquals(10, vm.uiState.cantidad)
    }

    @Test
    fun unidadesSubenDeUnoEnUnoConLimites() {
        val vm = crear(torta)
        vm.cargarProducto(1)
        vm.onMenos()
        assertEquals(1, vm.uiState.cantidad)
        repeat(15) { vm.onMas() }
        assertEquals(10, vm.uiState.cantidad)
    }

    @Test
    fun masYMenosSinProductoNoHacenNada() {
        val vm = crear()
        vm.onMas()
        vm.onMenos()
        assertEquals(1, vm.uiState.cantidad)
    }
}
