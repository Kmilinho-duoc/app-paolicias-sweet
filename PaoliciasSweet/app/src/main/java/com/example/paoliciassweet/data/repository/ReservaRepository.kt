package com.example.paoliciassweet.data.repository

import com.example.paoliciassweet.data.remote.RetrofitClient
import com.example.paoliciassweet.data.remote.api.ReservasApi
import com.example.paoliciassweet.data.remote.dto.EstadoDto
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

// Único lugar que habla con el microservicio de reservas. Cada función devuelve Result: éxito o error.
class ReservaRepository(private val api: ReservasApi = RetrofitClient.reservas) {

    suspend fun crear(reserva: ReservaDto): Result<ReservaDto> =
        runCatching { api.crear(reserva) }

    suspend fun listarPorCliente(email: String): Result<List<ReservaDto>> =
        runCatching { api.listar(email, null) }

    suspend fun listarTodas(): Result<List<ReservaDto>> =
        runCatching { api.listar(null, null) }

    suspend fun obtener(id: Int): Result<ReservaDto> =
        runCatching { api.obtener(id) }

    suspend fun cambiarEstado(id: Int, estado: String): Result<ReservaDto> =
        runCatching { api.cambiarEstado(id, EstadoDto(estado)) }

    suspend fun eliminar(id: Int): Result<Unit> =
        runCatching { check(api.eliminar(id).isSuccessful) { "No se pudo eliminar" } }

    suspend fun subirComprobante(id: Int, metodoPago: String, imagenJpeg: ByteArray): Result<ReservaDto> =
        runCatching {
            val cuerpo = imagenJpeg.toRequestBody("image/jpeg".toMediaType())
            val archivo = MultipartBody.Part.createFormData("archivo", "comprobante.jpg", cuerpo)
            api.subirComprobante(id, metodoPago, archivo)
        }

    suspend fun descargarComprobante(id: Int): Result<ByteArray> =
        runCatching { withContext(Dispatchers.IO) { api.comprobante(id).bytes() } }
}
