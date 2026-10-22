package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp

@Composable
fun ContadorAsistentes(
    asistentes: Int,
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
                Text("Asistentes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("De 10 en 10 · máximo 100", style = MaterialTheme.typography.bodySmall)
            }
            OutlinedIconButton(onClick = onMenos, enabled = asistentes > 10) {
                Text("−")
            }
            Text("$asistentes", style = MaterialTheme.typography.titleMedium)
            FilledIconButton(onClick = onMas, enabled = asistentes < 100) {
                Text("+")
            }
        }
    }
}
