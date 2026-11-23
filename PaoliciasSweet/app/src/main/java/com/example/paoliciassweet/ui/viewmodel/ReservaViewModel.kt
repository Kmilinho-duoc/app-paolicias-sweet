package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ItemReservaDto
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ClimaRepository
import com.example.paoliciassweet.data.repository.ReservaRepository
import com.example.paoliciassweet.model.CarritoModel
import com.example.paoliciassweet.model.ReservaModel
import com.example.paoliciassweet.model.Validaciones
import kotlinx.coroutines.launch

class ReservaViewModel(
    private val reservas: ReservaRepository = ReservaRepository(),
    private val clima: ClimaRepository = ClimaRepository()
) : ViewModel() {
    var uiState by mutableStateOf(ReservaModel())
        private set

    // al completar una fecha válida, consulta la API externa del clima
    fun onFechaChanged(fecha: String) {
        uiState = uiState.copy(fecha = fecha, fechaError = null, clima = null, errorEnvio = null)
        if (Validaciones.fechaValida(fecha)) {
            viewModelScope.launch {
                val pronostico = clima.pronostico(fecha.trim())
                if (uiState.fecha.trim() == fecha.trim()) uiState = uiState.copy(clima = pronostico)
            }
        }
    }

    fun onHoraChanged(hora: String) { uiState = uiState.copy(hora = hora, horaError = null, errorEnvio = null) }
    fun onObservacionesChanged(texto: String) { uiState = uiState.copy(observaciones = texto) }

    // valida el evento y envía la reserva con todas las órdenes del carrito al microservicio
    fun onContinuarClick(email: String, carrito: CarritoModel, onSuccess: (Int) -> Unit) {
        val state = uiState
        if (carrito.items.isEmpty()) return

        val fechaError = if (!Validaciones.fechaValida(state.fecha)) "Usa el formato dd/mm/aaaa." else null
        val horaError = if (!Validaciones.horaValida(state.hora)) "Usa el formato hh:mm." else null

        if (fechaError != null || horaError != null) {
            uiState = state.copy(fechaError = fechaError, horaError = horaError)
            return
        }

        val reserva = ReservaDto(
            clienteEmail = email,
            fecha = state.fecha.trim(),
            hora = state.hora.trim(),
            observaciones = state.observaciones.trim(),
            total = carrito.total,
            abono = carrito.abono,
            estado = "Pendiente",
            items = carrito.items.map {
                ItemReservaDto(
                    producto = it.producto.nombre,
                    cantidad = it.cantidad,
                    detalle = it.detalle,
                    subtotal = it.subtotal
                )
            }
        )

        viewModelScope.launch {
            reservas.crear(reserva)
                .onSuccess { onSuccess(it.id) }
                .onFailure {
                    uiState = uiState.copy(errorEnvio = "No se pudo enviar la reserva. Revisa tu conexión e inténtalo de nuevo.")
                }
        }
    }
}
