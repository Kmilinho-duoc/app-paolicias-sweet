package com.example.paoliciassweet.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.BotonSecundario
import com.example.paoliciassweet.ui.components.CampoTexto
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.IconoCabecera
import com.example.paoliciassweet.ui.viewmodel.RegisterViewModel

@Composable
fun Register(
    onNavigateToLogin: () -> Unit,
    viewModel: RegisterViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current // Contexto para acceder a la BD SQLite y lanzar el Toast

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconoCabecera(Icons.Default.Person)
            Spacer(modifier = Modifier.height(16.dp))
            Header(titulo = "Crear cuenta", subtitulo = "Completa tus datos para crear tu cuenta.")
            Spacer(modifier = Modifier.height(16.dp))

            CampoTexto(
                value = state.name,
                onValueChange = { viewModel.onNameChanged(it) },
                label = "Nombre completo",
                error = state.nameError
            )
            CampoTexto(
                value = state.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = "Correo",
                error = state.emailError,
                keyboardType = KeyboardType.Email
            )
            CampoTexto(
                value = state.phone,
                onValueChange = { viewModel.onPhoneChanged(it) },
                label = "Teléfono",
                error = state.phoneError,
                keyboardType = KeyboardType.Phone
            )
            CampoTexto(
                value = state.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = "Contraseña",
                error = state.passwordError,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            CampoTexto(
                value = state.confirmPassword,
                onValueChange = { viewModel.onConfirmPasswordChanged(it) },
                label = "Repetir contraseña",
                error = state.confirmPasswordError,
                keyboardType = KeyboardType.Password,
                isPassword = true
            )
            Spacer(modifier = Modifier.height(8.dp))
            BotonPrincipal(
                texto = "Registrarse",
                onClick = {
                    viewModel.onRegisterClick(context) {
                        Toast.makeText(
                            context,
                            "¡Registro exitoso! Por favor inicia sesión.",
                            Toast.LENGTH_SHORT
                        ).show()
                        onNavigateToLogin() // Retorna a la pantalla de Login
                    }
                }
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "¿Ya tienes una cuenta?",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))
            BotonSecundario(texto = "Iniciar sesión", onClick = onNavigateToLogin)
        }
    }
}
