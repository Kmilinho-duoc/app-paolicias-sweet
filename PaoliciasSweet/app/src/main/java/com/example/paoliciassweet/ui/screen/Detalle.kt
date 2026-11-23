package com.example.paoliciassweet.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.model.ProductoModel
import com.example.paoliciassweet.model.aPesos
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.ContadorAsistentes
import com.example.paoliciassweet.ui.components.FilaDato
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.ImagenProducto
import com.example.paoliciassweet.ui.viewmodel.DetalleViewModel

@Composable
fun Detalle(
    productoId: Int,
    onBack: () -> Unit,
    onAgregar: (ProductoModel, Int) -> Unit, // producto y cantidad elegida
    viewModel: DetalleViewModel = viewModel()
) {
    val state = viewModel.uiState
    val producto = state.producto
    val context = LocalContext.current

    LaunchedEffect(productoId) { viewModel.cargarProducto(productoId) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                titulo = producto?.nombre ?: "Producto",
                subtitulo = producto?.descripcion,
                onBack = onBack
            )

            if (producto != null) {
                Spacer(modifier = Modifier.height(16.dp))
                ImagenProducto(
                    producto = producto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    tamanoEmoji = 72.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = if (producto.porPersona) "Por persona ${producto.precio.aPesos()}"
                    else "Desde ${producto.precio.aPesos()}",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Información útil para el cliente
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Información", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        FilaDato("Categoría", producto.categoria)
                        if (producto.rinde.isNotBlank()) FilaDato("Rinde", producto.rinde)
                        if (producto.anticipacion.isNotBlank()) FilaDato("Pedir con", producto.anticipacion)
                        if (producto.alergenos.isNotBlank()) FilaDato("Contiene", producto.alergenos)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))

                if (producto.ingredientes.isNotEmpty()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text("Ingredientes", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                            producto.ingredientes.forEach {
                                Text("• $it", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                ContadorAsistentes(
                    titulo = if (producto.porPersona) "Asistentes" else "Cantidad",
                    detalle = if (producto.porPersona) "De 10 en 10 · máximo 100" else "Máximo ${producto.cantidadMaxima} unidades",
                    valor = state.cantidad,
                    puedeBajar = state.cantidad > producto.cantidadMinima,
                    puedeSubir = state.cantidad < producto.cantidadMaxima,
                    onMenos = { viewModel.onMenos() },
                    onMas = { viewModel.onMas() }
                )
                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Subtotal de esta orden", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                        Text(state.subtotal.aPesos(), style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                BotonPrincipal(
                    texto = "Agregar a la reserva",
                    onClick = {
                        Toast.makeText(context, "Agregado a tu reserva.", Toast.LENGTH_SHORT).show()
                        onAgregar(producto, state.cantidad)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
