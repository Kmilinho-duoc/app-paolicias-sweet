package com.example.paoliciassweet.data.repository

import com.example.paoliciassweet.data.remote.RetrofitClient
import com.example.paoliciassweet.data.remote.api.UsuariosApi
import com.example.paoliciassweet.data.remote.dto.UsuarioDto

// Envía el perfil del usuario al microservicio de usuarios
class UsuarioRepository(private val api: UsuariosApi = RetrofitClient.usuarios) {

    suspend fun registrar(usuario: UsuarioDto): Result<UsuarioDto> =
        runCatching { api.registrar(usuario) }
}
