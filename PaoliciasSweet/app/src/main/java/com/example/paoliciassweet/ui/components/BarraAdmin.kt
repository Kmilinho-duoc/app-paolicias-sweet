package com.example.paoliciassweet.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

// Barra inferior del administrador: Reservas (0), Productos (1), Usuarios (2) y Salir (cierra la sesión)
@Composable
fun BarraAdmin(
    seleccionada: Int,
    onReservas: () -> Unit,
    onProductos: () -> Unit,
    onUsuarios: () -> Unit,
    onSalir: () -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = seleccionada == 0,
            onClick = onReservas,
            icon = { Icon(Icons.Default.DateRange, contentDescription = null) },
            label = { Text("Reservas") }
        )
        NavigationBarItem(
            selected = seleccionada == 1,
            onClick = onProductos,
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
            label = { Text("Productos") }
        )
        NavigationBarItem(
            selected = seleccionada == 2,
            onClick = onUsuarios,
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Usuarios") }
        )
        NavigationBarItem(
            selected = false,
            onClick = onSalir,
            icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null) },
            label = { Text("Salir") }
        )
    }
}
