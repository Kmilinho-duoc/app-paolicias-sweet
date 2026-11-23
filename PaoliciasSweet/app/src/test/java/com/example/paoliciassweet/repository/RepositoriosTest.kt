package com.example.paoliciassweet.repository

import com.example.paoliciassweet.data.remote.api.ClimaApi
import com.example.paoliciassweet.data.remote.api.ProductosApi
import com.example.paoliciassweet.data.remote.api.ReservasApi
import com.example.paoliciassweet.data.remote.dto.ClimaDiarioDto
import com.example.paoliciassweet.data.remote.dto.ClimaDto
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ClimaRepository
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.data.repository.ReservaRepository
import com.example.paoliciassweet.model.catalogoProductos
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response
import java.io.IOException

class RepositoriosTest {

    // ---- Productos: si el microservicio falla, usa la lista local ----

    @Test
    fun productosUsaLaListaLocalSiElMicroservicioFalla() = runTest {
        val api = mockk<ProductosApi>()
        coEvery { api.listar() } throws IOException("sin red")
        assertEquals(catalogoProductos, ProductoRepository(api).listar())
    }

    @Test
    fun productosUsaLaListaLocalSiElMicroservicioDevuelveVacio() = runTest {
        val api = mockk<ProductosApi>()
        coEvery { api.listar() } returns emptyList()
        assertEquals(catalogoProductos, ProductoRepository(api).listar())
    }

    @Test
    fun productosConvierteLoQueEntregaElMicroservicio() = runTest {
        val api = mockk<ProductosApi>()
        coEvery { api.listar() } returns listOf(ProductoDto(id = 9, nombre = "Brownie", precio = 1500))
        val lista = ProductoRepository(api).listar()
        assertEquals(1, lista.size)
        assertEquals("Brownie", lista.first().nombre)
    }

    @Test
    fun obtenerUnProductoCaeALaListaLocalPorId() = runTest {
        val api = mockk<ProductosApi>()
        coEvery { api.obtener(1) } throws IOException("sin red")
        assertEquals(catalogoProductos.first { it.id == 1 }, ProductoRepository(api).obtener(1))
    }

    // ---- Reservas: los errores de red llegan como Result.failure ----

    @Test
    fun reservasDevuelveFalloSiNoHayConexion() = runTest {
        val api = mockk<ReservasApi>()
        coEvery { api.crear(any()) } throws IOException("sin red")
        assertTrue(ReservaRepository(api).crear(ReservaDto()).isFailure)
    }

    @Test
    fun reservasDevuelveExitoConLaRespuestaDelServicio() = runTest {
        val api = mockk<ReservasApi>()
        coEvery { api.crear(any()) } returns ReservaDto(id = 5)
        assertEquals(5, ReservaRepository(api).crear(ReservaDto()).getOrNull()?.id)
    }

    @Test
    fun eliminarEsExitosoConRespuesta200YFallaConError() = runTest {
        val api = mockk<ReservasApi>()
        coEvery { api.eliminar(1) } returns Response.success(Unit)
        coEvery { api.eliminar(2) } returns Response.error<Unit>(404, "".toResponseBody())
        val repo = ReservaRepository(api)
        assertTrue(repo.eliminar(1).isSuccess)
        assertTrue(repo.eliminar(2).isFailure)
    }

    // ---- Clima (API externa) ----

    @Test
    fun climaArmaElTextoDelPronostico() = runTest {
        val api = mockk<ClimaApi>()
        coEvery { api.pronostico(any(), any(), any(), any(), any(), any()) } returns
            ClimaDto(ClimaDiarioDto(max = listOf(30.4), min = listOf(12.6), lluvia = listOf(10)))
        assertEquals("Pronóstico en Vallenar: 30° / 13° · lluvia 10%", ClimaRepository(api).pronostico("28/11/2026"))
    }

    @Test
    fun climaDevuelveNullSiLaApiFallaOVieneVacia() = runTest {
        val api = mockk<ClimaApi>()
        coEvery { api.pronostico(any(), any(), any(), any(), any(), any()) } throws IOException("sin red")
        assertNull(ClimaRepository(api).pronostico("28/11/2026"))

        coEvery { api.pronostico(any(), any(), any(), any(), any(), any()) } returns ClimaDto(null)
        assertNull(ClimaRepository(api).pronostico("28/11/2026"))
    }
}
