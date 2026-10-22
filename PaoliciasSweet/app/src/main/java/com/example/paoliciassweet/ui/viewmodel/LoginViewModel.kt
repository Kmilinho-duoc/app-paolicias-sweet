package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.database.AppDatabase
import com.example.paoliciassweet.model.LoginModel
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    var uiState by mutableStateOf(LoginModel())
        private set

    fun onEmailChanged(email: String) {
        uiState = uiState.copy(email = email, emailError = null, errorMessage = null)
    }

    fun onPasswordChanged(password: String) {
        uiState = uiState.copy(password = password, passwordError = null, errorMessage = null)
    }

    fun onLoginClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState
        val email = state.email.trim()

        // Validación por campo
        val emailError = when {
            email.isBlank() -> "Ingresa tu correo."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "El correo no es válido."
            else -> null
        }
        val passwordError = if (state.password.isBlank()) "Ingresa tu contraseña." else null

        if (emailError != null || passwordError != null) {
            uiState = state.copy(emailError = emailError, passwordError = passwordError)
            return
        }

        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val user = db.usuarioDao().getUserByEmail(email)

            if (user != null && user.password == state.password) {
                uiState = uiState.copy(errorMessage = null)
                onSuccess() // Inicia sesión con éxito
            } else {
                uiState = uiState.copy(errorMessage = "Correo o contraseña incorrectos.")
            }
        }
    }
}
