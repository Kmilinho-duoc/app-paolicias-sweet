package com.example.paoliciassweet.model

import com.example.paoliciassweet.data.remote.dto.ProductoDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ModelosTest {
    private val torta = ProductoModel(1, "Torta", "desc", "Tortas", 28000, false, "🎂", 0xFFFFFFFF)
    private val coffee = ProductoModel(
        4, "Coffee break", "desc", "Catering", 6500, true, "☕", 0xFFFFFFFF,
        tarifario = mapOf(10 to 7000, 50 to 6000)
    )

    @Test
    fun subtotalPorUnidadMultiplicaPrecioPorCantidad() {
        assertEquals(56000, ItemCarritoModel(torta, 2).subtotal)
    }

    @Test
    fun subtotalPorPersonaUsaElTarifario() {
        assertEquals(300000, ItemCarritoModel(coffee, 50).subtotal) // 6000 x 50
    }

    @Test
    fun subtotalPorPersonaSinRangoUsaPrecioBase() {
        assertEquals(130000, ItemCarritoModel(coffee, 20).subtotal) // 6500 x 20
    }

    @Test
    fun detalleDeLaOrden() {
        assertEquals("50 personas", ItemCarritoModel(coffee, 50).detalle)
        assertEquals("1 unidad", ItemCarritoModel(torta, 1).detalle)
        assertEquals("3 unidades", ItemCarritoModel(torta, 3).detalle)
    }

    @Test
    fun carritoCalculaTotalAbonoYCantidad() {
        val carrito = CarritoModel(listOf(ItemCarritoModel(torta, 1), ItemCarritoModel(coffee, 10)))
        assertEquals(98000, carrito.total) // 28000 + 70000
        assertEquals(49000, carrito.abono)
        assertEquals(2, carrito.cantidadItems)
    }

    @Test
    fun carritoVacioEsCero() {
        val carrito = CarritoModel()
        assertEquals(0, carrito.total)
        assertEquals(0, carrito.abono)
        assertEquals(0, carrito.cantidadItems)
    }

    @Test
    fun detalleSinProductoTieneSubtotalCero() {
        assertEquals(0, DetalleModel().subtotal)
        assertEquals(56000, DetalleModel(torta, 2).subtotal)
    }

    @Test
    fun reglasDeCantidadSegunTipoDeProducto() {
        assertEquals(10, coffee.cantidadMinima)
        assertEquals(100, coffee.cantidadMaxima)
        assertEquals(10, coffee.paso)
        assertEquals(50, coffee.cantidadInicial)
        assertEquals(1, torta.cantidadMinima)
        assertEquals(10, torta.cantidadMaxima)
        assertEquals(1, torta.paso)
        assertEquals(1, torta.cantidadInicial)
    }

    @Test
    fun productoDtoConvierteElTarifarioAEnteros() {
        val modelo = ProductoDto(id = 4, nombre = "Coffee", tarifario = mapOf("10" to 7000, "50" to 6000)).aModelo()
        assertEquals(7000, modelo.tarifario[10])
        assertEquals(6000, modelo.tarifario[50])
        assertEquals("Coffee", modelo.nombre)
    }

    @Test
    fun validaCorreos() {
        assertTrue(Validaciones.correoValido("ana@correo.cl"))
        assertTrue(Validaciones.correoValido("  admin@paolicias.cl "))
        assertFalse(Validaciones.correoValido("ana@correo"))
        assertFalse(Validaciones.correoValido("anacorreo.cl"))
        assertFalse(Validaciones.correoValido(""))
    }

    @Test
    fun validaFechasYHoras() {
        assertTrue(Validaciones.fechaValida("28/11/2026"))
        assertFalse(Validaciones.fechaValida("31/13/2026"))
        assertFalse(Validaciones.fechaValida("2026-11-28"))
        assertTrue(Validaciones.horaValida("18:30"))
        assertFalse(Validaciones.horaValida("24:00"))
        assertFalse(Validaciones.horaValida("9:30"))
    }

    @Test
    fun conviertoFechaAFormatoIso() {
        assertEquals("2026-11-28", Validaciones.fechaIso("28/11/2026"))
        assertEquals("texto", Validaciones.fechaIso("texto"))
    }
}
