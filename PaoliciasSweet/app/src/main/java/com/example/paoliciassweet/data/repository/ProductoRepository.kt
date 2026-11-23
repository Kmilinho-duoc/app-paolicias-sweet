package com.example.paoliciassweet.data.repository

import com.example.paoliciassweet.data.remote.RetrofitClient
import com.example.paoliciassweet.data.remote.api.ProductosApi
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.model.catalogoProductos
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

// Pide los productos al microservicio; si no responde, usa la lista local para que la app siga funcionando
class ProductoRepository(private val api: ProductosApi = RetrofitClient.productos) {

    suspend fun listar(): List<ProductoModel> =
        runCatching { api.listar().map { it.aModelo() } }.getOrNull()
            ?.takeIf { it.isNotEmpty() } ?: catalogoProductos

    suspend fun obtener(id: Int): ProductoModel? =
        runCatching { api.obtener(id).aModelo() }.getOrNull()
            ?: catalogoProductos.firstOrNull { it.id == id }

    // ---- Administración (rol Administrador): aquí no hay lista local de respaldo, se ve el error real ----

    suspend fun listarRemoto(): Result<List<ProductoDto>> = runCatching { api.listar() }

    suspend fun obtenerRemoto(id: Int): Result<ProductoDto> = runCatching { api.obtener(id) }

    suspend fun crear(producto: ProductoDto): Result<ProductoDto> = runCatching { api.crear(producto) }

    suspend fun actualizar(id: Int, producto: ProductoDto): Result<ProductoDto> =
        runCatching { api.actualizar(id, producto) }

    suspend fun eliminar(id: Int): Result<Unit> =
        runCatching { check(api.eliminar(id).isSuccessful) { "No se pudo eliminar" } }

    suspend fun subirImagen(id: Int, jpeg: ByteArray): Result<ProductoDto> = runCatching {
        val cuerpo = jpeg.toRequestBody("image/jpeg".toMediaType())
        api.subirImagen(id, MultipartBody.Part.createFormData("archivo", "producto.jpg", cuerpo))
    }
}
