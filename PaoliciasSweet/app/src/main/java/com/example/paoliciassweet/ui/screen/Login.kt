package com.example.paoliciassweet.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.paoliciassweet.ui.components.IconoCabecera
import com.example.paoliciassweet.ui.viewmodel.LoginViewModel

@Composable
fun Login(
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit, // entrega el correo del usuario que inició sesión
    viewModel: LoginViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current // <-- contexto para la base de datos SQLite y Toast

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                IconoCabecera(Icons.Default.Lock)
                Spacer(modifier = Modifier.height(24.dp))
                Text("Iniciar sesión", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(24.dp))

                CampoTexto(
                    value = state.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    label = "Correo electrónico",
                    error = state.emailError,
                    keyboardType = KeyboardType.Email
                )
                Spacer(modifier = Modifier.height(8.dp))
                CampoTexto(
                    value = state.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = "Contraseña",
                    error = state.passwordError,
                    keyboardType = KeyboardType.Password,
                    isPassword = true
                )
                // Muestra mensaje de error en rojo si falla el inicio de sesión
                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                BotonPrincipal(
                    texto = "Iniciar sesión",
                    onClick = {
                        viewModel.onLoginClick(context) {
                            Toast.makeText(context, "¡Bienvenido de nuevo!", Toast.LENGTH_SHORT).show()
                            onLoginSuccess(state.email.trim())
                        }
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("¿Aún no tienes una cuenta?", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
                BotonSecundario(texto = "Crear cuenta", onClick = onNavigateToRegister)
            }
        }
    }
}
