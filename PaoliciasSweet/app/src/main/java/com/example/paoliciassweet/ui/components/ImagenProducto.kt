package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.paoliciassweet.model.ProductoModel
import coil.compose.AsyncImage
import com.example.paoliciassweet.data.remote.ApiConfig

// Foto del producto: busca en res/drawable el archivo con el nombre guardado en producto.imagen.
// Si no hay foto, muestra su emoji sobre el color de fondo.
@Composable
fun ImagenProducto(
    producto: ProductoModel,
    modifier: Modifier = Modifier,
    tamanoEmoji: TextUnit = 32.sp
) {
    val contexto = LocalContext.current
    val idImagen = remember(producto.imagen) {
        producto.imagen?.let { contexto.resources.getIdentifier(it, "drawable", contexto.packageName) } ?: 0
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(producto.colorFondo)),
        contentAlignment = Alignment.Center
    ) {
        when {
            // 1) foto subida al servidor
            producto.tieneFoto -> AsyncImage(
                model = "${ApiConfig.PRODUCTOS_URL}api/productos/${producto.id}/imagen",
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // 2) foto incluida en la app (res/drawable)
            idImagen != 0 -> Image(
                painter = painterResource(idImagen),
                contentDescription = producto.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            // 3) sin foto: emoji (o el ícono neutro, si decidiste quitar los emojis)
            else -> Text(producto.emoji, fontSize = tamanoEmoji)
        }
    }
}
