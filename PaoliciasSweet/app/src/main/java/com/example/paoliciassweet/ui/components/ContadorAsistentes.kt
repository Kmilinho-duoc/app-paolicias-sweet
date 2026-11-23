package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// Contador − / + reutilizable: sirve para asistentes (de 10 en 10) o unidades (de 1 en 1)
@Composable
fun ContadorAsistentes(
    titulo: String,
    detalle: String,
    valor: Int,
    puedeBajar: Boolean,
    puedeSubir: Boolean,
    onMenos: () -> Unit,
    onMas: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(detalle, style = MaterialTheme.typography.bodySmall)
            }
            OutlinedIconButton(onClick = onMenos, enabled = puedeBajar) {
                Text("−")
            }
            Text("$valor", style = MaterialTheme.typography.titleMedium)
            FilledIconButton(onClick = onMas, enabled = puedeSubir) {
                Text("+")
            }
        }
    }
}
