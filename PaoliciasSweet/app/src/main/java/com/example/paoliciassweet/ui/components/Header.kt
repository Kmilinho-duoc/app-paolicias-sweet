package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun Header(
    titulo: String,
    subtitulo: String? = null,
    etiqueta: String? = null,
    onBack: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        if (onBack != null) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
        }
        if (etiqueta != null) {
            Text(etiqueta, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
        }
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        if (subtitulo != null) {
            Text(subtitulo, style = MaterialTheme.typography.bodySmall)
        }
    }
}
