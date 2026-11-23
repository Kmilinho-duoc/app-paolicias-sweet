package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paoliciassweet.data.remote.dto.ReservaDto
import com.example.paoliciassweet.model.aPesos

// Resumen de una reserva. La usan los dos roles: cada pantalla agrega sus propios botones en "acciones"
@Composable
fun ReservaCard(
    reserva: ReservaDto,
    mostrarCliente: Boolean = false,
    acciones: @Composable ColumnScope.() -> Unit = {}
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reserva #${reserva.id}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                EstadoChip(reserva.estado)
            }
            if (mostrarCliente) FilaDato("Cliente", reserva.clienteEmail)
            FilaDato("Fecha y hora", "${reserva.fecha} · ${reserva.hora}")
            HorizontalDivider()
            reserva.items.forEach { FilaDato("${it.producto} (${it.detalle})", it.subtotal.aPesos()) }
            HorizontalDivider()
            FilaDato("Total", reserva.total.aPesos())
            FilaDato("Garantía del 50%", reserva.abono.aPesos())
            if (reserva.metodoPago.isNotBlank()) FilaDato("Método de pago", reserva.metodoPago)
            if (reserva.observaciones.isNotBlank()) FilaDato("Observaciones", reserva.observaciones)

            acciones()
        }
    }
}
