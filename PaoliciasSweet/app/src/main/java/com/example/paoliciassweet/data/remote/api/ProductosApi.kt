package com.example.paoliciassweet.data.remote.api

import com.example.paoliciassweet.data.remote.dto.ProductoDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.Part

// Microservicio de productos
interface ProductosApi {
    @GET("api/productos")
    suspend fun listar(): List<ProductoDto>

    @GET("api/productos/{id}")
    suspend fun obtener(@Path("id") id: Int): ProductoDto

    // administración de productos (rol Administrador)
    @POST("api/productos")
    suspend fun crear(@Body producto: ProductoDto): ProductoDto

    @PUT("api/productos/{id}")
    suspend fun actualizar(@Path("id") id: Int, @Body producto: ProductoDto): ProductoDto

    @DELETE("api/productos/{id}")
    suspend fun eliminar(@Path("id") id: Int): Response<Unit>

    @Multipart
    @POST("api/productos/{id}/imagen")
    suspend fun subirImagen(@Path("id") id: Int, @Part archivo: MultipartBody.Part): ProductoDto
}
