package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FiltroCategorias(
    categorias: List<String>,
    seleccionada: String,
    onSeleccion: (String) -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        categorias.forEach { categoria ->
            FilterChip(
                selected = categoria == seleccionada,
                onClick = { onSeleccion(categoria) },
                label = { Text(categoria) },
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}
