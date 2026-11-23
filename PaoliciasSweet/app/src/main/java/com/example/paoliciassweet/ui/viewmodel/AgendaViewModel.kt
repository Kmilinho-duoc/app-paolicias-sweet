package com.example.paoliciassweet.ui.viewmodel

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ReservaRepository
import com.example.paoliciassweet.model.Validaciones
import kotlinx.coroutines.launch

// Rol Administrador: agenda con las reservas de todos los clientes
class AgendaViewModel(
    private val reservas: ReservaRepository = ReservaRepository()
) : ViewModel() {
    val filtros = listOf("Todas", "Pendiente", "Enviado", "Pagado 50%")

    var lista by mutableStateOf<List<ReservaDto>>(emptyList())
        private set
    var filtro by mutableStateOf("Todas")
        private set
    var mensaje by mutableStateOf<String?>(null)
        private set
    var comprobante by mutableStateOf<Bitmap?>(null)
        private set

    // reservas visibles según el filtro elegido
    val visibles: List<ReservaDto>
        get() = if (filtro == "Todas") lista else lista.filter { it.estado == filtro }

    // reservas con comprobante enviado que esperan validación
    val porValidar: Int
        get() = lista.count { it.estado == "Enviado" }

    fun onFiltroChanged(nuevo: String) { filtro = nuevo }

    fun cargar() {
        viewModelScope.launch {
            reservas.listarTodas()
                .onSuccess {
                    // orden cronológico por fecha y hora del evento
                    lista = it.sortedBy { r -> Validaciones.fechaIso(r.fecha) + r.hora }
                    mensaje = null
                }
                .onFailure { mensaje = "No se pudo conectar con el servidor." }
        }
    }

    // Actualizar (PATCH): validar el pago o rechazarlo (vuelve a "Pendiente")
    fun validar(id: Int) = cambiarEstado(id, "Pagado 50%")
    fun rechazar(id: Int) = cambiarEstado(id, "Pendiente")

    private fun cambiarEstado(id: Int, estado: String) {
        viewModelScope.launch {
            reservas.cambiarEstado(id, estado)
                .onSuccess { cargar() }
                .onFailure { mensaje = "No se pudo actualizar la reserva." }
        }
    }

    fun verComprobante(id: Int) {
        viewModelScope.launch {
            reservas.descargarComprobante(id)
                .onSuccess { comprobante = BitmapFactory.decodeByteArray(it, 0, it.size) }
                .onFailure { mensaje = "No se pudo cargar el comprobante." }
        }
    }

    fun cerrarComprobante() { comprobante = null }
}
