package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.database.AppDatabase
import com.example.paoliciassweet.data.local.entity.ReservaEntity
import com.example.paoliciassweet.model.ReservaModel
import com.example.paoliciassweet.model.catalogoProductos
import kotlinx.coroutines.launch

class ReservaViewModel : ViewModel() {
    var uiState by mutableStateOf(ReservaModel())
        private set

    private val regexFecha = Regex("^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$")
    private val regexHora = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

    fun cargarProducto(id: Int) {
        uiState = uiState.copy(producto = catalogoProductos.firstOrNull { it.id == id })
    }

    fun onFechaChanged(fecha: String) { uiState = uiState.copy(fecha = fecha, fechaError = null) }
    fun onHoraChanged(hora: String) { uiState = uiState.copy(hora = hora, horaError = null) }
    fun onObservacionesChanged(texto: String) { uiState = uiState.copy(observaciones = texto) }

    // de 10 en 10, entre 10 y 100
    fun onMasAsistentes() { uiState = uiState.copy(asistentes = minOf(100, uiState.asistentes + 10)) }
    fun onMenosAsistentes() { uiState = uiState.copy(asistentes = maxOf(10, uiState.asistentes - 10)) }

    fun onContinuarClick(context: Context, email: String, onSuccess: (Int) -> Unit) {
        val state = uiState
        val producto = state.producto ?: return

        val fechaError = if (!regexFecha.matches(state.fecha.trim())) "Usa el formato dd/mm/aaaa." else null
        val horaError = if (!regexHora.matches(state.hora.trim())) "Usa el formato hh:mm." else null

        if (fechaError != null || horaError != null) {
            uiState = state.copy(fechaError = fechaError, horaError = horaError)
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val id = db.reservaDao().insertarReserva(
                ReservaEntity(
                    usuarioEmail = email,
                    producto = producto.nombre,
                    fecha = state.fecha.trim(),
                    hora = state.hora.trim(),
                    asistentes = state.asistentes,
                    observaciones = state.observaciones.trim(),
                    total = state.total,
                    abono = state.abono,
                    estado = "Pendiente"
                )
            )
            onSuccess(id.toInt())
        }
    }
}
