package com.example.paoliciassweet.viewmodel

import com.example.paoliciassweet.MainDispatcherRule
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ReservaRepository
import com.example.paoliciassweet.ui.viewmodel.AgendaViewModel
import com.example.paoliciassweet.ui.viewmodel.ReservasViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class ReservasYAgendaViewModelTest {
    @get:Rule
    val mainRule = MainDispatcherRule()

    private val repo = mockk<ReservaRepository>()

    private val r1 = ReservaDto(id = 1, clienteEmail = "ana@correo.cl", fecha = "05/12/2026", hora = "10:00", estado = "Pendiente")
    private val r2 = ReservaDto(id = 2, clienteEmail = "ana@correo.cl", fecha = "28/11/2026", hora = "18:30", estado = "Enviado")
    private val r3 = ReservaDto(id = 3, clienteEmail = "luis@correo.cl", fecha = "28/11/2026", hora = "09:00", estado = "Pagado 50%")

    // ---- Cliente ----

    @Test
    fun clienteVeSusReservasConLaMasNuevaPrimero() {
        coEvery { repo.listarPorCliente("ana@correo.cl") } returns Result.success(listOf(r1, r2))
        val vm = ReservasViewModel(repo)
        vm.cargar("ana@correo.cl")
        assertEquals(listOf(2, 1), vm.lista.map { it.id })
        assertNull(vm.mensaje)
    }

    @Test
    fun clienteVeUnMensajeSiNoHayConexion() {
        coEvery { repo.listarPorCliente(any()) } returns Result.failure(RuntimeException("sin red"))
        val vm = ReservasViewModel(repo)
        vm.cargar("ana@correo.cl")
        assertNotNull(vm.mensaje)
    }

    @Test
    fun clienteCancelaUnaReservaYSeRecargaLaLista() {
        coEvery { repo.eliminar(1) } returns Result.success(Unit)
        coEvery { repo.listarPorCliente("ana@correo.cl") } returns Result.success(listOf(r2))
        val vm = ReservasViewModel(repo)
        vm.cancelar("ana@correo.cl", 1)
        coVerify { repo.eliminar(1) }
        assertEquals(listOf(2), vm.lista.map { it.id })
    }

    @Test
    fun siNoSePuedeCancelarMuestraMensaje() {
        coEvery { repo.eliminar(1) } returns Result.failure(RuntimeException("error"))
        val vm = ReservasViewModel(repo)
        vm.cancelar("ana@correo.cl", 1)
        assertNotNull(vm.mensaje)
    }

    // ---- Administrador ----

    @Test
    fun agendaOrdenaPorFechaYHoraDelEvento() {
        coEvery { repo.listarTodas() } returns Result.success(listOf(r1, r2, r3))
        val vm = AgendaViewModel(repo)
        vm.cargar()
        assertEquals(listOf(3, 2, 1), vm.lista.map { it.id })
    }

    @Test
    fun agendaFiltraPorEstadoYCuentaLasPorValidar() {
        coEvery { repo.listarTodas() } returns Result.success(listOf(r1, r2, r3))
        val vm = AgendaViewModel(repo)
        vm.cargar()
        assertEquals(3, vm.visibles.size)
        assertEquals(1, vm.porValidar)
        vm.onFiltroChanged("Pendiente")
        assertEquals(listOf(1), vm.visibles.map { it.id })
        vm.onFiltroChanged("Pagado 50%")
        assertEquals(listOf(3), vm.visibles.map { it.id })
    }

    @Test
    fun validarCambiaElEstadoAPagadoYRecargaLaAgenda() {
        coEvery { repo.listarTodas() } returns Result.success(listOf(r2))
        coEvery { repo.cambiarEstado(2, "Pagado 50%") } returns Result.success(r2.copy(estado = "Pagado 50%"))
        val vm = AgendaViewModel(repo)
        vm.validar(2)
        coVerify { repo.cambiarEstado(2, "Pagado 50%") }
        coVerify { repo.listarTodas() }
    }

    @Test
    fun rechazarDejaLaReservaPendiente() {
        coEvery { repo.listarTodas() } returns Result.success(listOf(r2))
        coEvery { repo.cambiarEstado(2, "Pendiente") } returns Result.success(r2.copy(estado = "Pendiente"))
        val vm = AgendaViewModel(repo)
        vm.rechazar(2)
        coVerify { repo.cambiarEstado(2, "Pendiente") }
    }

    @Test
    fun agendaMuestraMensajeSiElServidorNoResponde() {
        coEvery { repo.listarTodas() } returns Result.failure(RuntimeException("sin red"))
        val vm = AgendaViewModel(repo)
        vm.cargar()
        assertNotNull(vm.mensaje)
    }
}
