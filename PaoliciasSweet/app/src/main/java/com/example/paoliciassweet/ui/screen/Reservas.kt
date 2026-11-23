package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BarraInferior
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.BotonSecundario
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ReservaCard
import com.example.paoliciassweet.ui.viewmodel.ReservasViewModel

@Composable
fun Reservas(
    email: String,
    onAdjuntar: (Int) -> Unit, // abre el comprobante de una reserva pendiente
    onCatalogo: () -> Unit,
    onUsuario: () -> Unit,
    viewModel: ReservasViewModel = viewModel()
) {
    LaunchedEffect(email) { viewModel.cargar(email) }

    Scaffold(
        bottomBar = {
            BarraInferior(
                seleccionada = 1,
                onCatalogo = onCatalogo,
                onReservas = {},
                onUsuario = onUsuario
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
                titulo = "Mis reservas",
                subtitulo = "Revisa el estado de tus eventos y de la garantía del 50%.",
                etiqueta = "Paolicias.Sweet"
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.mensaje != null) {
                Text(viewModel.mensaje.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (viewModel.lista.isEmpty()) {
                Text("Aún no tienes reservas. Agrega productos desde el catálogo.")
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(viewModel.lista, key = { it.id }) { reserva ->
                        ReservaCard(reserva = reserva) {
                            // mientras no se envíe el comprobante, se puede continuar o cancelar
                            if (reserva.estado == "Pendiente") {
                                BotonPrincipal(texto = "Adjuntar comprobante", onClick = { onAdjuntar(reserva.id) })
                                BotonSecundario(texto = "Cancelar reserva", onClick = { viewModel.cancelar(email, reserva.id) })
                            }
                        }
                    }
                }
            }
        }
    }
}
