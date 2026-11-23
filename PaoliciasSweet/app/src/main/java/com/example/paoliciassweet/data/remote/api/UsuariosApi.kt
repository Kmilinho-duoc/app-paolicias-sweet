package com.example.paoliciassweet.data.remote.api

import com.example.paoliciassweet.data.remote.dto.UsuarioDto
import retrofit2.http.Body
import retrofit2.http.POST

// Microservicio de usuarios
interface UsuariosApi {
    @POST("api/usuarios")
    suspend fun registrar(@Body usuario: UsuarioDto): UsuarioDto
}
