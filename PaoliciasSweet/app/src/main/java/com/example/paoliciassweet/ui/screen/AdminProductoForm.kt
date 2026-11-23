package com.example.paoliciassweet.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.CampoTexto
import com.example.paoliciassweet.ui.components.FiltroCategorias
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.viewmodel.ProductoFormViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import com.example.paoliciassweet.ui.components.BotonSecundario

// Formulario para crear (productoId = 0) o editar un producto
@Composable
fun AdminProductoForm(
    productoId: Int,
    onBack: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: ProductoFormViewModel = viewModel()
) {
    val state = viewModel.uiState

    LaunchedEffect(productoId) { viewModel.cargar(productoId) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .imePadding()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Header(
                titulo = if (productoId == 0) "Nuevo producto" else "Editar producto",
                subtitulo = "Completa los datos que verá el cliente en el catálogo.",
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(8.dp))

            CampoTexto(
                value = state.nombre,
                onValueChange = { viewModel.onNombreChanged(it) },
                label = "Nombre",
                error = state.nombreError
            )
            CampoTexto(
                value = state.descripcion,
                onValueChange = { viewModel.onDescripcionChanged(it) },
                label = "Descripción corta",
                error = state.descripcionError
            )

            Text("Categoría", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            FiltroCategorias(
                categorias = viewModel.categorias,
                seleccionada = state.categoria,
                onSeleccion = { viewModel.onCategoriaChanged(it) }
            )

            CampoTexto(
                value = state.precio,
                onValueChange = { viewModel.onPrecioChanged(it) },
                label = "Precio en pesos",
                error = state.precioError,
                keyboardType = KeyboardType.Number
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("El precio es por persona", style = MaterialTheme.typography.bodyMedium)
                Switch(checked = state.porPersona, onCheckedChange = { viewModel.onPorPersonaChanged(it) })
            }

            CampoTexto(value = state.emoji, onValueChange = { viewModel.onEmojiChanged(it) }, label = "Emoji")
            CampoTexto(
                value = state.ingredientes,
                onValueChange = { viewModel.onIngredientesChanged(it) },
                label = "Ingredientes (separados por coma)",
                singleLine = false
            )
            CampoTexto(value = state.rinde, onValueChange = { viewModel.onRindeChanged(it) }, label = "Rinde (ej: 12 porciones)")
            CampoTexto(value = state.alergenos, onValueChange = { viewModel.onAlergenosChanged(it) }, label = "Contiene (alérgenos)")
            CampoTexto(value = state.anticipacion, onValueChange = { viewModel.onAnticipacionChanged(it) }, label = "Pedir con anticipación de")
            val context = LocalContext.current
            val elegirFoto = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
                if (uri != null) viewModel.onImagenElegida(context, uri)
            }
            BotonSecundario(
                texto = if (viewModel.foto == null) "Elegir foto" else "Cambiar foto",
                onClick = { elegirFoto.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)) }
            )
            viewModel.foto?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Image(it.asImageBitmap(), null, Modifier.size(120.dp))
            }

            if (state.mensaje != null) {
                Text(state.mensaje, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }
            BotonPrincipal(texto = "Guardar", onClick = { viewModel.guardar(onGuardado) })
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
