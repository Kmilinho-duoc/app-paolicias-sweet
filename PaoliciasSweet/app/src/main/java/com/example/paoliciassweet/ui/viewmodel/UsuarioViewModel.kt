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

// Pestaña "Usuario": datos de la cuenta que inició sesión
class UsuarioViewModel : ViewModel() {
    var usuario by mutableStateOf<UsuarioEntity?>(null)
        private set

    fun cargar(context: Context, email: String) {
        viewModelScope.launch {
            usuario = AppDatabase.getDatabase(context).usuarioDao().getUserByEmail(email)
        }
    }
}
