package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.database.AppDatabase
import com.example.paoliciassweet.data.local.entity.ReservaEntity
import kotlinx.coroutines.launch

class ComprobanteViewModel : ViewModel() {
    var reserva by mutableStateOf<ReservaEntity?>(null)
        private set

    // recupera la reserva guardada en SQLite
    fun cargarReserva(context: Context, id: Int) {
        viewModelScope.launch {
            reserva = AppDatabase.getDatabase(context).reservaDao().getReservaById(id)
        }
    }

    // actualiza el estado en SQLite 
    fun onEnviarClick(context: Context, onSuccess: () -> Unit) {
        val actual = reserva ?: return
        viewModelScope.launch {
            AppDatabase.getDatabase(context).reservaDao().actualizarEstado(actual.id, "Enviado")
            reserva = actual.copy(estado = "Enviado")
            onSuccess()
        }
    }

    // Variable para la foto del comprobante
    var foto by mutableStateOf<Bitmap?>(null)
        private set

    // Función para foto tomada
    fun onFotoTomada(bitmap: Bitmap?) { foto = bitmap }
}
