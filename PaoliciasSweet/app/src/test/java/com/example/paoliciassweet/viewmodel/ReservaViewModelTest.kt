package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.MainDispatcherRule
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ClimaRepository
import com.example.paoliciassweet.data.repository.ReservaRepository
import com.example.paoliciassweet.model.CarritoModel
import com.example.paoliciassweet.model.ItemCarritoModel
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.ui.viewmodel.ReservaViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ReservaViewModelTest {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val reservas = mockk<ReservaRepository>()
    private val clima = mockk<ClimaRepository>()
    private val coffee = ProductoModel(4, "Coffee", "desc", "Catering", 6500, true, "☕", 0xFFFFFFFF)
    private val carrito = CarritoModel(listOf(ItemCarritoModel(coffee, 50)))

    private fun crearViewModel(): ReservaViewModel {
        coEvery { clima.pronostico(any()) } returns "Pronóstico en Vallenar: 30° / 12° · lluvia 0%"
        return ReservaViewModel(reservas, clima)
    }

    @Test
    fun fechaYHoraInvalidasMuestranErroresYNoEnvian() {
        val vm = crearViewModel()
        vm.onFechaChanged("mañana")
        vm.onHoraChanged("99:99")
        vm.onContinuarClick("ana@correo.cl", carrito) { }
        assertNotNull(vm.uiState.fechaError)
        assertNotNull(vm.uiState.horaError)
        coVerify(exactly = 0) { reservas.crear(any()) }
    }

    @Test
    fun carritoVacioNoEnvia() {
        val vm = crearViewModel()
        vm.onFechaChanged("28/11/2026")
        vm.onHoraChanged("18:30")
        vm.onContinuarClick("ana@correo.cl", CarritoModel()) { }
        coVerify(exactly = 0) { reservas.crear(any()) }
    }

    @Test
    fun editarUnCampoLimpiaSuError() {
        val vm = crearViewModel()
        vm.onContinuarClick("ana@correo.cl", carrito) { }
        assertNotNull(vm.uiState.fechaError)
        vm.onFechaChanged("2")
        assertNull(vm.uiState.fechaError)
    }

    @Test
    fun enviaLaReservaConLosDatosDelCarrito() {
        coEvery { reservas.crear(any()) } returns Result.success(ReservaDto(id = 7))
        val vm = crearViewModel()
        vm.onFechaChanged("28/11/2026")
        vm.onHoraChanged("18:30")
        vm.onObservacionesChanged("Sin gluten")
        var idRecibido = 0
        vm.onContinuarClick("ana@correo.cl", carrito) { idRecibido = it }

        assertEquals(7, idRecibido)
        coVerify {
            reservas.crear(match {
                it.clienteEmail == "ana@correo.cl" &&
                    it.fecha == "28/11/2026" &&
                    it.hora == "18:30" &&
                    it.observaciones == "Sin gluten" &&
                    it.total == carrito.total &&
                    it.abono == carrito.abono &&
                    it.estado == "Pendiente" &&
                    it.items.size == 1
            })
        }
    }

    @Test
    fun siElMicroservicioFallaMuestraUnMensaje() {
        coEvery { reservas.crear(any()) } returns Result.failure(RuntimeException("sin red"))
        val vm = crearViewModel()
        vm.onFechaChanged("28/11/2026")
        vm.onHoraChanged("18:30")
        var exito = false
        vm.onContinuarClick("ana@correo.cl", carrito) { exito = true }
        assertEquals(false, exito)
        assertNotNull(vm.uiState.errorEnvio)
    }

    @Test
    fun alCompletarUnaFechaValidaMuestraElClimaDeLaApiExterna() {
        val vm = crearViewModel()
        vm.onFechaChanged("28/11/2026")
        assertEquals("Pronóstico en Vallenar: 30° / 12° · lluvia 0%", vm.uiState.clima)
    }

    @Test
    fun conUnaFechaIncompletaNoConsultaElClima() {
        val vm = crearViewModel()
        vm.onFechaChanged("28/11")
        assertNull(vm.uiState.clima)
        coVerify(exactly = 0) { clima.pronostico(any()) }
    }
}
