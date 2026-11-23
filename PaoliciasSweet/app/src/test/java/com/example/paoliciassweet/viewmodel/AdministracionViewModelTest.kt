package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.MainDispatcherRule
import com.example.paoliciassweet.data.local.entity.UsuarioEntity
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.ui.viewmodel.AdminProductosViewModel
import com.example.paoliciassweet.ui.viewmodel.AdminUsuariosViewModel
import com.example.paoliciassweet.ui.viewmodel.ProductoFormViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

class AdministracionViewModelTest {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo = mockk<ProductoRepository>()

    // ---- Lista de productos ----

    @Test
    fun listaDeProductosSeOrdenaPorNombre() {
        coEvery { repo.listarRemoto() } returns Result.success(
            listOf(ProductoDto(id = 2, nombre = "Torta"), ProductoDto(id = 1, nombre = "Brownie"))
        )
        val vm = AdminProductosViewModel(repo)
        vm.cargar()
        assertEquals(listOf("Brownie", "Torta"), vm.lista.map { it.nombre })
        assertNull(vm.mensaje)
    }

    @Test
    fun siElServidorNoRespondeMuestraMensaje() {
        coEvery { repo.listarRemoto() } returns Result.failure(RuntimeException("sin red"))
        val vm = AdminProductosViewModel(repo)
        vm.cargar()
        assertNotNull(vm.mensaje)
    }

    @Test
    fun eliminarUnProductoRecargaLaLista() {
        coEvery { repo.eliminar(1) } returns Result.success(Unit)
        coEvery { repo.listarRemoto() } returns Result.success(listOf(ProductoDto(id = 2, nombre = "Torta")))
        val vm = AdminProductosViewModel(repo)
        vm.eliminar(1)
        coVerify { repo.eliminar(1) }
        assertEquals(listOf(2), vm.lista.map { it.id })
    }

    @Test
    fun siNoSePuedeEliminarMuestraMensaje() {
        coEvery { repo.eliminar(1) } returns Result.failure(RuntimeException("error"))
        val vm = AdminProductosViewModel(repo)
        vm.eliminar(1)
        assertNotNull(vm.mensaje)
    }

    // ---- Formulario de producto ----

    @Test
    fun formularioInvalidoMuestraErroresYNoEnvia() {
        val vm = ProductoFormViewModel(repo)
        vm.onNombreChanged("ab")
        vm.onPrecioChanged("0")
        var guardado = false
        vm.guardar { guardado = true }
        assertNotNull(vm.uiState.nombreError)
        assertNotNull(vm.uiState.descripcionError)
        assertNotNull(vm.uiState.precioError)
        assertFalse(guardado)
        coVerify(exactly = 0) { repo.crear(any()) }
    }

    @Test
    fun elPrecioSoloAceptaDigitos() {
        val vm = ProductoFormViewModel(repo)
        vm.onPrecioChanged("12a.5")
        assertEquals("125", vm.uiState.precio)
    }

    @Test
    fun productoNuevoSeCreaYSeSeparanLosIngredientes() {
        coEvery { repo.crear(any()) } returns Result.success(ProductoDto(id = 10))
        val vm = ProductoFormViewModel(repo)
        vm.cargar(0)
        vm.onNombreChanged("Brownie")
        vm.onDescripcionChanged("Chocolate")
        vm.onPrecioChanged("1500")
        vm.onIngredientesChanged("Cacao, Harina , ,Huevos")
        var guardado = false
        vm.guardar { guardado = true }

        assertTrue(guardado)
        coVerify {
            repo.crear(match {
                it.nombre == "Brownie" && it.precio == 1500 && it.ingredientes == listOf("Cacao", "Harina", "Huevos")
            })
        }
    }

    @Test
    fun editarActualizaYConservaElTarifario() {
        coEvery { repo.obtenerRemoto(5) } returns Result.success(
            ProductoDto(id = 5, nombre = "Coffee", descripcion = "Para 10", categoria = "Catering",
                precio = 6500, porPersona = true, tarifario = mapOf("10" to 6500))
        )
        coEvery { repo.actualizar(5, any()) } returns Result.success(ProductoDto(id = 5))
        val vm = ProductoFormViewModel(repo)
        vm.cargar(5)
        assertEquals("Coffee", vm.uiState.nombre)
        assertEquals("6500", vm.uiState.precio)

        vm.onPrecioChanged("7000")
        var guardado = false
        vm.guardar { guardado = true }

        assertTrue(guardado)
        coVerify { repo.actualizar(5, match { it.precio == 7000 && it.tarifario == mapOf("10" to 6500) }) }
    }

    @Test
    fun siNoSePuedeGuardarMuestraMensaje() {
        coEvery { repo.crear(any()) } returns Result.failure(RuntimeException("sin red"))
        val vm = ProductoFormViewModel(repo)
        vm.onNombreChanged("Brownie")
        vm.onDescripcionChanged("Chocolate")
        vm.onPrecioChanged("1500")
        var guardado = false
        vm.guardar { guardado = true }
        assertFalse(guardado)
        assertNotNull(vm.uiState.mensaje)
    }

    // ---- Usuarios ----

    @Test
    fun elAdministradorPrincipalYLaCuentaEnUsoEstanProtegidos() {
        val vm = AdminUsuariosViewModel()
        val principal = UsuarioEntity(1, "Administrador", "admin@paolicias.cl", "9", "x", "Administrador")
        val propia = UsuarioEntity(2, "Luis", "luis@correo.cl", "9", "x", "Administrador")
        val otra = UsuarioEntity(3, "Ana", "ana@correo.cl", "9", "x", "Cliente")
        assertTrue(vm.esProtegido(principal, "luis@correo.cl"))
        assertTrue(vm.esProtegido(propia, "luis@correo.cl"))
        assertFalse(vm.esProtegido(otra, "luis@correo.cl"))
    }
}
