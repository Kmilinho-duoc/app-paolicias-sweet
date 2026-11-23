package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.database.AppDatabase
import com.example.paoliciassweet.data.local.entity.UsuarioEntity
import kotlinx.coroutines.launch

// Rol Administrador: administra las cuentas guardadas en SQLite (login y registro)
class AdminUsuariosViewModel : ViewModel() {
    var usuarios by mutableStateOf<List<UsuarioEntity>>(emptyList())
        private set

    // el administrador principal y la cuenta que está en uso no se pueden modificar
    fun esProtegido(usuario: UsuarioEntity, emailActual: String): Boolean =
        usuario.email == "admin@paolicias.cl" || usuario.email == emailActual

    fun cargar(context: Context) {
        viewModelScope.launch {
            usuarios = AppDatabase.getDatabase(context).usuarioDao().getAll()
        }
    }

    // Actualizar (UPDATE): alterna entre Cliente y Administrador
    fun cambiarRol(context: Context, usuario: UsuarioEntity) {
        viewModelScope.launch {
            val nuevoRol = if (usuario.rol == "Administrador") "Cliente" else "Administrador"
            AppDatabase.getDatabase(context).usuarioDao().updateRol(usuario.id, nuevoRol)
            cargar(context)
        }
    }

    // Eliminar (DELETE)
    fun eliminar(context: Context, usuario: UsuarioEntity) {
        viewModelScope.launch {
            AppDatabase.getDatabase(context).usuarioDao().deleteById(usuario.id)
            cargar(context)
        }
    }
}
