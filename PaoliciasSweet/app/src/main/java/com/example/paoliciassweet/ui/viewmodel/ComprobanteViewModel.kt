package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.data.repository.ReservaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class ComprobanteViewModel(
    private val reservas: ReservaRepository = ReservaRepository()
) : ViewModel() {
    var reserva by mutableStateOf<ReservaDto?>(null)
        private set

    // Garantía del 50%: "Transferencia" (captura desde la galería) o "Depósito" (foto con la cámara)
    var metodoPago by mutableStateOf("Transferencia")
        private set

    // Variable para la imagen del comprobante
    var foto by mutableStateOf<Bitmap?>(null)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    // recupera la reserva desde el microservicio
    fun cargarReserva(id: Int) {
        viewModelScope.launch {
            reservas.obtener(id)
                .onSuccess { reserva = it }
                .onFailure { error = "No se pudo cargar la reserva. Revisa tu conexión." }
        }
    }

    // al cambiar de método se descarta la imagen anterior
    fun onMetodoChanged(metodo: String) {
        if (metodo != metodoPago) {
            metodoPago = metodo
            foto = null
        }
    }

    // Recurso nativo cámara: foto tomada
    fun onFotoTomada(bitmap: Bitmap?) { foto = bitmap }

    // Recurso nativo galería: se lee la imagen elegida y se reduce de tamaño
    fun onImagenElegida(context: Context, uri: Uri) {
        viewModelScope.launch {
            foto = withContext(Dispatchers.IO) {
                val opciones = BitmapFactory.Options().apply { inSampleSize = 4 }
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, opciones)
                }
            }
        }
    }

    // sube la imagen al microservicio: guarda el método de pago y deja la reserva en "Enviado"
    fun onEnviarClick(onSuccess: () -> Unit) {
        val actual = reserva ?: return
        val imagen = foto ?: return
        viewModelScope.launch {
            reservas.subirComprobante(actual.id, metodoPago, aJpeg(imagen))
                .onSuccess {
                    reserva = it
                    error = null
                    onSuccess()
                }
                .onFailure { error = "No se pudo enviar el comprobante. Revisa tu conexión." }
        }
    }

    private fun aJpeg(bitmap: Bitmap): ByteArray {
        val salida = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, salida)
        return salida.toByteArray()
    }
}
