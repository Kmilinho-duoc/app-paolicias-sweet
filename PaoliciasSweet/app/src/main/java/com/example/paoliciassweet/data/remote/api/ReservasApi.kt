package com.example.paoliciassweet.data.remote.api

import com.example.paoliciassweet.data.remote.dto.EstadoDto
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

// Microservicio de reservas: Crear (POST), Leer (GET), Actualizar (PATCH) y Eliminar (DELETE)
interface ReservasApi {
    @POST("api/reservas")
    suspend fun crear(@Body reserva: ReservaDto): ReservaDto

    // email = reservas de un cliente; estado = filtro del administrador (si van nulos, trae todas)
    @GET("api/reservas")
    suspend fun listar(@Query("email") email: String?, @Query("estado") estado: String?): List<ReservaDto>

    @GET("api/reservas/{id}")
    suspend fun obtener(@Path("id") id: Int): ReservaDto

    @PATCH("api/reservas/{id}/estado")
    suspend fun cambiarEstado(@Path("id") id: Int, @Body cambio: EstadoDto): ReservaDto

    @DELETE("api/reservas/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>

    // Sube la imagen del comprobante; el servicio guarda el método y deja la reserva en "Enviado"
    @Multipart
    @POST("api/reservas/{id}/comprobante")
    suspend fun subirComprobante(
        @Path("id") id: Int,
        @Query("metodoPago") metodoPago: String,
        @Part archivo: MultipartBody.Part
    ): ReservaDto

    @GET("api/reservas/{id}/comprobante")
    suspend fun comprobante(@Path("id") id: Int): ResponseBody
}
