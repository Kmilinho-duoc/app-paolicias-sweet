package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.model.CarritoModel
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.CampoTexto
import com.example.paoliciassweet.ui.components.CotizacionCard
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ItemCarritoRow
import com.example.paoliciassweet.ui.viewmodel.ReservaViewModel

@Composable
fun Reserva(
    email: String,
    carrito: CarritoModel,          // órdenes agregadas desde el catálogo
    onQuitar: (Int) -> Unit,        // quita una orden por id de producto
    onBack: () -> Unit,
    onContinuar: (Int) -> Unit,     // entrega el id de la reserva guardada
    viewModel: ReservaViewModel = viewModel()
) {
    val state = viewModel.uiState

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                titulo = "Reservar evento",
                subtitulo = "Revisa tus órdenes y elige la fecha de tu evento.",
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Órdenes del carrito
            Text("Tus órdenes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            if (carrito.items.isEmpty()) {
                Text("Tu reserva está vacía. Agrega productos desde el catálogo.", style = MaterialTheme.typography.bodySmall)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    carrito.items.forEach { item ->
                        ItemCarritoRow(item = item, onQuitar = { onQuitar(item.producto.id) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Datos del evento
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                CampoTexto(
                    value = state.fecha,
                    onValueChange = { viewModel.onFechaChanged(it) },
                    label = "Fecha del evento",
                    error = state.fechaError,
                    modifier = Modifier.weight(1f)
                )
                CampoTexto(
                    value = state.hora,
                    onValueChange = { viewModel.onHoraChanged(it) },
                    label = "Hora de inicio",
                    error = state.horaError,
                    modifier = Modifier.weight(1f)
                )
            }
            // API externa: pronóstico del tiempo para la fecha elegida
            if (state.clima != null) {
                Text(state.clima, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(4.dp))

            CampoTexto(
                value = state.observaciones,
                onValueChange = { viewModel.onObservacionesChanged(it) },
                label = "Observaciones (alergias, vegetarianos u otros)",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(12.dp))

            CotizacionCard(
                detalle = if (carrito.cantidadItems == 1) "1 orden" else "${carrito.cantidadItems} órdenes",
                total = carrito.total,
                abono = carrito.abono
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (state.errorEnvio != null) {
                Text(state.errorEnvio, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
            BotonPrincipal(
                texto = "Continuar al abono",
                enabled = carrito.items.isNotEmpty(),
                onClick = { viewModel.onContinuarClick(email, carrito) { id -> onContinuar(id) } }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
