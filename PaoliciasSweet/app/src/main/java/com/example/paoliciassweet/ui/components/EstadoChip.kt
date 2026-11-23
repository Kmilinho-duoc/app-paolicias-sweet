package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Etiqueta del estado de la reserva: "Pendiente" (rosado) o "Enviado" (lila)
@Composable
fun EstadoChip(estado: String) {
    val pendiente = estado == "Pendiente"
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (pendiente) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            estado,
            style = MaterialTheme.typography.labelSmall,
            color = if (pendiente) MaterialTheme.colorScheme.onErrorContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
