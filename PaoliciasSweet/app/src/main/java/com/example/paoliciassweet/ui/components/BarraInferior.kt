package com.example.paoliciassweet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// Barra inferior con las 3 secciones: Catálogo (0), Reservas (1) y Usuario (2)
@Composable
fun BarraInferior(
    seleccionada: Int,
    onCatalogo: () -> Unit,
    onReservas: () -> Unit,
    onUsuario: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = seleccionada == 0,
            onClick = onCatalogo,
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Catálogo") }
        )
        NavigationBarItem(
            selected = seleccionada == 1,
            onClick = onReservas,
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Reservas") }
        )
        NavigationBarItem(
            selected = seleccionada == 2,
            onClick = onUsuario,
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Usuario") }
        )
    }
}
