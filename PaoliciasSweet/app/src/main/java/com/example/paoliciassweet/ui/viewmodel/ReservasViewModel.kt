package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ReservaRepository
import kotlinx.coroutines.launch

// Pestaña "Reservas" del cliente: lista sus reservas y permite cancelarlas
class ReservasViewModel(
    private val reservas: ReservaRepository = ReservaRepository()
) : ViewModel() {
    var lista by mutableStateOf<List<ReservaDto>>(emptyList())
        private set

    var mensaje by mutableStateOf<String?>(null)
        private set

    fun cargar(email: String) {
        viewModelScope.launch {
            reservas.listarPorCliente(email)
                .onSuccess {
                    lista = it.sortedByDescending { reserva -> reserva.id }
                    mensaje = null
                }
                .onFailure { mensaje = "No se pudieron cargar tus reservas. Revisa tu conexión." }
        }
    }

    // Eliminar (DELETE): cancela una reserva pendiente
    fun cancelar(email: String, id: Int) {
        viewModelScope.launch {
            reservas.eliminar(id)
                .onSuccess { cargar(email) }
                .onFailure { mensaje = "No se pudo cancelar la reserva." }
        }
    }
}
