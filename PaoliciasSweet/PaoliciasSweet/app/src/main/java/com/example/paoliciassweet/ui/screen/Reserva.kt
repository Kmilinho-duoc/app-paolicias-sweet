package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.CampoTexto
import com.example.paoliciassweet.ui.components.ContadorAsistentes
import com.example.paoliciassweet.ui.components.CotizacionCard
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ProductoResumen
import com.example.paoliciassweet.ui.viewmodel.ReservaViewModel

@Composable
fun Reserva(
    email: String,
    productoId: Int,
    onBack: () -> Unit,
    onContinuar: (Int) -> Unit, // entrega el id de la reserva guardada
    viewModel: ReservaViewModel = viewModel()
) {
    val state = viewModel.uiState
    val context = LocalContext.current

    LaunchedEffect(productoId) { viewModel.cargarProducto(productoId) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                titulo = "Reservar evento",
                subtitulo = "Configura tu evento y revisa tu cotización en tiempo real.",
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(16.dp))

            state.producto?.let { ProductoResumen(producto = it, onCambiar = onBack) }
            Spacer(modifier = Modifier.height(12.dp))

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
            Spacer(modifier = Modifier.height(4.dp))

            ContadorAsistentes(
                asistentes = state.asistentes,
                onMenos = { viewModel.onMenosAsistentes() },
                onMas = { viewModel.onMasAsistentes() }
            )
            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(
                value = state.observaciones,
                onValueChange = { viewModel.onObservacionesChanged(it) },
                label = "Observaciones (alergias, vegetarianos u otros)",
                singleLine = false
            )
            Spacer(modifier = Modifier.height(12.dp))

            CotizacionCard(detalle = state.detalle, total = state.total, abono = state.abono)
            Spacer(modifier = Modifier.height(16.dp))

            BotonPrincipal(
                texto = "Continuar al abono",
                onClick = { viewModel.onContinuarClick(context, email) { id -> onContinuar(id) } }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
