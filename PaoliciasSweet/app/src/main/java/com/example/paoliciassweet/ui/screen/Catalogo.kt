package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BarraInferior
import com.example.paoliciassweet.ui.components.CampoTexto
import com.example.paoliciassweet.ui.components.FiltroCategorias
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ProductoCard
import com.example.paoliciassweet.ui.viewmodel.CatalogoViewModel

@Composable
fun Catalogo(
    cantidadCarrito: Int,      // órdenes en el carrito (para el contador del botón)
    onCotizar: (Int) -> Unit,  // entrega el id del producto elegido
    onVerReserva: () -> Unit,  // abre la reserva con las órdenes del carrito
    onReservas: () -> Unit,
    onUsuario: () -> Unit,
    viewModel: CatalogoViewModel = viewModel()
) {
    val productos = viewModel.productos

    LaunchedEffect(Unit) { viewModel.cargarProductos() }

    Scaffold(
        bottomBar = {
            BarraInferior(
                seleccionada = 0,
                onCatalogo = {},
                onReservas = onReservas,
                onUsuario = onUsuario
            )
        },
        // Botón circular del carrito: abajo a la derecha y solo cuando hay órdenes
        floatingActionButton = {
            if (cantidadCarrito > 0) {
                FloatingActionButton(
                    onClick = onVerReserva,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ) {
                    BadgedBox(badge = { Badge { Text("$cantidadCarrito") } }) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Ver reserva")
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Header(
                titulo = "Catálogo",
                subtitulo = "Tortas, pastelería y banquetería para tu evento",
                etiqueta = "Paolicias.Sweet"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CampoTexto(
                value = viewModel.busqueda,
                onValueChange = { viewModel.onBusquedaChanged(it) },
                label = "Buscar tortas, menús, catering"
            )
            Spacer(modifier = Modifier.height(8.dp))
            FiltroCategorias(
                categorias = viewModel.categorias,
                seleccionada = viewModel.categoria,
                onSeleccion = { viewModel.onCategoriaChanged(it) }
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (productos.isEmpty()) {
                Text("No encontramos productos con ese filtro.")
            } else {
                // el espacio inferior evita que el botón del carrito tape el último producto
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(bottom = 88.dp)
                ) {
                    items(productos, key = { it.id }) { producto ->
                        ProductoCard(producto = producto, onCotizar = { onCotizar(producto.id) })
                    }
                }
            }
        }
    }
}
