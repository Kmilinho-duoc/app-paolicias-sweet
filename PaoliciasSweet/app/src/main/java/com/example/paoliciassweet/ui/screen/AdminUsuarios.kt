package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.data.local.entity.UsuarioEntity
import com.example.paoliciassweet.ui.components.BarraAdmin
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.UsuarioCard
import com.example.paoliciassweet.ui.viewmodel.AdminUsuariosViewModel

// Rol Administrador: cuentas de usuario guardadas en SQLite
@Composable
fun AdminUsuarios(
    emailActual: String,
    onReservas: () -> Unit,
    onProductos: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: AdminUsuariosViewModel = viewModel()
) {
    val context = LocalContext.current
    var porEliminar by remember { mutableStateOf<UsuarioEntity?>(null) }

    LaunchedEffect(Unit) { viewModel.cargar(context) }

    Scaffold(
        bottomBar = {
            BarraAdmin(
                seleccionada = 2,
                onReservas = onReservas,
                onProductos = onProductos,
                onUsuarios = {},
                onSalir = onCerrarSesion
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Header(
                titulo = "Usuarios",
                subtitulo = "Cuentas registradas: cambia su rol o elimínalas.",
                etiqueta = "Paolicias.Sweet · Administrador"
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.usuarios.isEmpty()) {
                Text("No hay usuarios registrados.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.usuarios, key = { it.id }) { usuario ->
                        UsuarioCard(
                            usuario = usuario,
                            protegido = viewModel.esProtegido(usuario, emailActual),
                            onCambiarRol = { viewModel.cambiarRol(context, usuario) },
                            onEliminar = { porEliminar = usuario }
                        )
                    }
                }
            }
        }
    }

    // Confirmación antes de eliminar
    val usuario = porEliminar
    if (usuario != null) {
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar cuenta") },
            text = { Text("¿Seguro que quieres eliminar la cuenta de ${usuario.name}?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(context, usuario)
                    porEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } }
        )
    }
}
