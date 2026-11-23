package com.example.paoliciassweet.ui.screen

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BarraAdmin
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.BotonSecundario
import com.example.paoliciassweet.ui.components.FiltroCategorias
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ReservaCard
import com.example.paoliciassweet.ui.viewmodel.AgendaViewModel

// Pantalla del rol Administrador: agenda de todas las reservas, validación de pagos y cierre de sesión
@Composable
fun Agenda(
    onProductos: () -> Unit,
    onUsuarios: () -> Unit,
    onCerrarSesion: () -> Unit,
    viewModel: AgendaViewModel = viewModel()
) {
    val context = LocalContext.current

    // Permiso para mostrar notificaciones (Android 13+). Al aceptarlo se vuelve a cargar la agenda.
    val pedirNotificaciones = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
        viewModel.cargar()
    }
    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pedirNotificaciones.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        viewModel.cargar()
    }

    // Notificación local cuando hay comprobantes esperando validación
    LaunchedEffect(viewModel.lista) {
        if (viewModel.porValidar > 0) notificarPendientes(context, viewModel.porValidar)
    }

    Scaffold(
        bottomBar = {
            BarraAdmin(
                seleccionada = 0,
                onReservas = {},
                onProductos = onProductos,
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
                titulo = "Agenda de eventos",
                subtitulo = "Reservas de todos los clientes, ordenadas por fecha.",
                etiqueta = "Paolicias.Sweet · Administrador"
            )
            Spacer(modifier = Modifier.height(12.dp))
            FiltroCategorias(
                categorias = viewModel.filtros,
                seleccionada = viewModel.filtro,
                onSeleccion = { viewModel.onFiltroChanged(it) }
            )
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.mensaje != null) {
                Text(viewModel.mensaje.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (viewModel.visibles.isEmpty()) {
                Text("No hay reservas para mostrar.", modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(viewModel.visibles, key = { it.id }) { reserva ->
                        ReservaCard(reserva = reserva, mostrarCliente = true) {
                            // el administrador revisa el comprobante y valida o rechaza el pago
                            if (reserva.estado == "Enviado" || reserva.estado == "Pagado 50%") {
                                BotonSecundario(texto = "Ver comprobante", onClick = { viewModel.verComprobante(reserva.id) })
                            }
                            if (reserva.estado == "Enviado") {
                                BotonPrincipal(texto = "Validar pago", onClick = { viewModel.validar(reserva.id) })
                                BotonSecundario(texto = "Rechazar", onClick = { viewModel.rechazar(reserva.id) })
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            BotonSecundario(texto = "Actualizar", onClick = { viewModel.cargar() })
        }
    }

    // Ventana con la imagen del comprobante
    val comprobante = viewModel.comprobante
    if (comprobante != null) {
        AlertDialog(
            onDismissRequest = { viewModel.cerrarComprobante() },
            confirmButton = { TextButton(onClick = { viewModel.cerrarComprobante() }) { Text("Cerrar") } },
            text = {
                Image(
                    bitmap = comprobante.asImageBitmap(),
                    contentDescription = "Comprobante de pago",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                )
            }
        )
    }
}
