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
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.ui.components.BarraAdmin
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ProductoAdminCard
import com.example.paoliciassweet.ui.viewmodel.AdminProductosViewModel

// Rol Administrador: lista de productos del microservicio
@Composable
fun AdminProductos(
    onReservas: () -> Unit,
    onUsuarios: () -> Unit,
    onCerrarSesion: () -> Unit,
    onNuevo: () -> Unit,
    onEditar: (Int) -> Unit,
    viewModel: AdminProductosViewModel = viewModel()
) {
    var porEliminar by remember { mutableStateOf<ProductoDto?>(null) }

    LaunchedEffect(Unit) { viewModel.cargar() }

    Scaffold(
        bottomBar = {
            BarraAdmin(
                seleccionada = 1,
                onReservas = onReservas,
                onProductos = {},
                onUsuarios = onUsuarios,
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
                titulo = "Productos",
                subtitulo = "Agrega, edita o elimina los productos del catálogo.",
                etiqueta = "Paolicias.Sweet · Administrador"
            )
            Spacer(modifier = Modifier.height(12.dp))
            BotonPrincipal(texto = "Nuevo producto", onClick = onNuevo)
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.mensaje != null) {
                Text(viewModel.mensaje.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (viewModel.lista.isEmpty()) {
                Text("No hay productos para mostrar.")
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.lista, key = { it.id }) { producto ->
                        ProductoAdminCard(
                            producto = producto,
                            onEditar = { onEditar(producto.id) },
                            onEliminar = { porEliminar = producto }
                        )
                    }
                }
            }
        }
    }

    // Confirmación antes de eliminar
    val producto = porEliminar
    if (producto != null) {
        AlertDialog(
            onDismissRequest = { porEliminar = null },
            title = { Text("Eliminar producto") },
            text = { Text("¿Seguro que quieres eliminar \"${producto.nombre}\"?") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.eliminar(producto.id)
                    porEliminar = null
                }) { Text("Eliminar") }
            },
            dismissButton = { TextButton(onClick = { porEliminar = null }) { Text("Cancelar") } }
        )
    }
}
