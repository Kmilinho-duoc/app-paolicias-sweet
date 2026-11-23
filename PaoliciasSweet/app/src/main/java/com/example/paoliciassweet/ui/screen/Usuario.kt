package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BarraInferior
import com.example.paoliciassweet.ui.components.BotonSecundario
import com.example.paoliciassweet.ui.components.FilaDato
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.IconoCabecera
import com.example.paoliciassweet.ui.viewmodel.UsuarioViewModel

@Composable
fun Usuario(
    email: String,
    onCatalogo: () -> Unit,
    onReservas: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: UsuarioViewModel = viewModel()
) {
    val context = LocalContext.current
    val usuario = viewModel.usuario

    LaunchedEffect(email) { viewModel.cargar(context, email) }

    Scaffold(
        bottomBar = {
            BarraInferior(
                seleccionada = 2,
                onCatalogo = onCatalogo,
                onReservas = onReservas,
                onUsuario = {}
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            IconoCabecera(Icons.Default.Person)
            Spacer(modifier = Modifier.height(16.dp))
            Header(
                titulo = "Mi cuenta",
                subtitulo = "Tus datos de usuario.",
                etiqueta = "Paolicias.Sweet"
            )
            Spacer(modifier = Modifier.height(16.dp))

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilaDato("Nombre", usuario?.name ?: "-")
                    FilaDato("Correo", usuario?.email ?: email)
                    FilaDato("Teléfono", usuario?.phone ?: "-")
                }
            }
            Spacer(modifier = Modifier.height(24.dp))

            BotonSecundario(texto = "Cerrar sesión", onClick = onCerrarSesion)
        }
    }
}
