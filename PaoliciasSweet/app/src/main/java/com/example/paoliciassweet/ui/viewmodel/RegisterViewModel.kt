package com.example.paoliciassweet.ui.viewmodel

import android.content.Context
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.database.AppDatabase
import com.example.paoliciassweet.data.local.entity.UsuarioEntity
import com.example.paoliciassweet.model.RegisterModel
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    var uiState by mutableStateOf(RegisterModel())
        private set

    fun onNameChanged(name: String) { uiState = uiState.copy(name = name, nameError = null) }
    fun onEmailChanged(email: String) { uiState = uiState.copy(email = email, emailError = null) }
    // solo dígitos y máximo 9 (teléfono chileno)
    fun onPhoneChanged(phone: String) { uiState = uiState.copy(phone = phone.filter { it.isDigit() }.take(9), phoneError = null) }
    fun onPasswordChanged(password: String) { uiState = uiState.copy(password = password, passwordError = null) }
    fun onConfirmPasswordChanged(confirmPassword: String) { uiState = uiState.copy(confirmPassword = confirmPassword, confirmPasswordError = null) }

    fun onRegisterClick(context: Context, onSuccess: () -> Unit) {
        val state = uiState
        val email = state.email.trim()

        // Validación por campo
        val nameError = if (state.name.trim().length < 3) "Ingresa tu nombre completo." else null
        val emailError = when {
            email.isBlank() -> "Ingresa tu correo."
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "El correo no es válido."
            else -> null
        }
        val phoneError = if (state.phone.length != 9) "Ingresa 9 dígitos." else null
        val passwordError = if (state.password.length < 6) "Mínimo 6 caracteres." else null
        val confirmError = if (state.password != state.confirmPassword) "Las contraseñas no coinciden." else null

        if (listOf(nameError, emailError, phoneError, passwordError, confirmError).any { it != null }) {
            uiState = state.copy(
                nameError = nameError,
                emailError = emailError,
                phoneError = phoneError,
                passwordError = passwordError,
                confirmPasswordError = confirmError
            )
            return
        }

        // méto-do asíncronico en SQLite con Room
        viewModelScope.launch {
            val db = AppDatabase.getDatabase(context)
            val existingUser = db.usuarioDao().getUserByEmail(email)

            if (existingUser != null) {
                uiState = uiState.copy(emailError = "El correo ya está registrado.")
            } else {
                val newUser = UsuarioEntity(
                    name = state.name.trim(),
                    email = email,
                    phone = state.phone,
                    password = state.password
                )
                db.usuarioDao().registerUser(newUser)
                onSuccess()
            }
        }
    }
}
