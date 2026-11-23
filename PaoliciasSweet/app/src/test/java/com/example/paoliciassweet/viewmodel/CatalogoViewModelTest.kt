package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.MainDispatcherRule
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.model.catalogoProductos
import com.example.paoliciassweet.ui.viewmodel.CatalogoViewModel
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class CatalogoViewModelTest {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo = mockk<ProductoRepository>()

    @Test
    fun empiezaConLaListaLocalCompleta() {
        val vm = CatalogoViewModel(repo)
        assertEquals(catalogoProductos.size, vm.productos.size)
    }

    @Test
    fun filtraPorCategoria() {
        val vm = CatalogoViewModel(repo)
        vm.onCategoriaChanged("Tortas")
        assertTrue(vm.productos.isNotEmpty())
        assertTrue(vm.productos.all { it.categoria == "Tortas" })
    }

    @Test
    fun filtraPorTextoSinDistinguirMayusculas() {
        val vm = CatalogoViewModel(repo)
        vm.onBusquedaChanged("COFFEE")
        assertEquals(1, vm.productos.size)
        assertEquals("Coffee break corporativo", vm.productos.first().nombre)
    }

    @Test
    fun busquedaSinResultadosDevuelveListaVacia() {
        val vm = CatalogoViewModel(repo)
        vm.onBusquedaChanged("zzzz")
        assertTrue(vm.productos.isEmpty())
    }

    @Test
    fun reemplazaElCatalogoConLoQueEntregaElMicroservicio() {
        val remoto = ProductoModel(50, "Brownie", "Chocolate", "Pastelería", 1500, false, "🍫", 0xFFFFFFFF)
        coEvery { repo.listar() } returns listOf(remoto)
        val vm = CatalogoViewModel(repo)
        vm.cargarProductos()
        assertEquals(listOf(remoto), vm.productos)
    }
}
