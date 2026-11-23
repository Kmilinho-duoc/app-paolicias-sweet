package com.example.paoliciassweet.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.paoliciassweet.data.remote.dto.ProductoDto
import com.example.paoliciassweet.data.repository.ProductoRepository
import com.example.paoliciassweet.model.ProductoFormModel
import kotlinx.coroutines.launch
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Formulario para crear (id 0) o editar un producto
class ProductoFormViewModel(
    private val repositorio: ProductoRepository = ProductoRepository()
) : ViewModel() {
    val categorias = listOf("Tortas", "Pastelería", "Catering")

    var uiState by mutableStateOf(ProductoFormModel())
        private set

    var foto by mutableStateOf<Bitmap?>(null)
        private set

    // producto original: al editar se conservan sus datos que el formulario no muestra (tarifario, color)
    private var base = ProductoDto()
    private var cargadoId = -1

    fun cargar(id: Int) {
        if (id == cargadoId) return
        cargadoId = id
        if (id == 0) return
        viewModelScope.launch {
            repositorio.obtenerRemoto(id)
                .onSuccess {
                    base = it
                    uiState = ProductoFormModel(
                        nombre = it.nombre,
                        descripcion = it.descripcion,
                        categoria = it.categoria.ifBlank { "Tortas" },
                        precio = it.precio.toString(),
                        porPersona = it.porPersona,
                        emoji = it.emoji,
                        ingredientes = it.ingredientes.joinToString(", "),
                        rinde = it.rinde,
                        alergenos = it.alergenos,
                        anticipacion = it.anticipacion,
                        imagen = it.imagen.orEmpty()
                    )
                }
                .onFailure { uiState = uiState.copy(mensaje = "No se pudo cargar el producto.") }
        }
    }

    fun onNombreChanged(valor: String) { uiState = uiState.copy(nombre = valor, nombreError = null) }
    fun onDescripcionChanged(valor: String) { uiState = uiState.copy(descripcion = valor, descripcionError = null) }
    fun onCategoriaChanged(valor: String) { uiState = uiState.copy(categoria = valor) }
    fun onPrecioChanged(valor: String) { uiState = uiState.copy(precio = valor.filter { it.isDigit() }, precioError = null) }
    fun onPorPersonaChanged(valor: Boolean) { uiState = uiState.copy(porPersona = valor) }
    fun onEmojiChanged(valor: String) { uiState = uiState.copy(emoji = valor) }
    fun onIngredientesChanged(valor: String) { uiState = uiState.copy(ingredientes = valor) }
    fun onRindeChanged(valor: String) { uiState = uiState.copy(rinde = valor) }
    fun onAlergenosChanged(valor: String) { uiState = uiState.copy(alergenos = valor) }
    fun onAnticipacionChanged(valor: String) { uiState = uiState.copy(anticipacion = valor) }
    fun onImagenChanged(valor: String) { uiState = uiState.copy(imagen = valor) }

    fun onImagenElegida(context: Context, uri: Uri) {
        viewModelScope.launch {
            foto = withContext(Dispatchers.IO) {
                val opciones = BitmapFactory.Options().apply { inSampleSize = 2 }
                context.contentResolver.openInputStream(uri)?.use {
                    BitmapFactory.decodeStream(it, null, opciones)
                }
            }
        }
    }
    // valida y envía: crea (POST) si es nuevo o actualiza (PUT) si ya existía
    fun guardar(onSuccess: () -> Unit) {
        val s = uiState
        val precio = s.precio.toIntOrNull()

        val nombreError = if (s.nombre.trim().length < 3) "Ingresa el nombre del producto." else null
        val descripcionError = if (s.descripcion.isBlank()) "Ingresa una descripción." else null
        val precioError = if (precio == null || precio <= 0) "Ingresa un precio mayor a 0." else null

        if (nombreError != null || descripcionError != null || precioError != null || precio == null) {
            uiState = s.copy(nombreError = nombreError, descripcionError = descripcionError, precioError = precioError)
            return
        }

        val producto = base.copy(
            nombre = s.nombre.trim(),
            descripcion = s.descripcion.trim(),
            categoria = s.categoria,
            precio = precio,
            porPersona = s.porPersona,
            emoji = s.emoji.ifBlank { "🍰" },
            imagen = s.imagen.trim().ifBlank { null },
            ingredientes = s.ingredientes.split(",").map { it.trim() }.filter { it.isNotEmpty() },
            rinde = s.rinde.trim(),
            alergenos = s.alergenos.trim(),
            anticipacion = s.anticipacion.trim()
        )

        viewModelScope.launch {
            val resultado = if (base.id == 0) repositorio.crear(producto) else repositorio.actualizar(base.id, producto)
            resultado
                .onSuccess { guardado ->
                    val bitmap = foto
                    if (bitmap != null) {
                        repositorio.subirImagen(guardado.id, aJpeg(bitmap)).onFailure {
                            uiState = uiState.copy(mensaje = "Se guardó el producto, pero no se pudo subir la foto.")
                            return@launch
                        }
                    }
                    onSuccess()
                }
                .onFailure { uiState = uiState.copy(mensaje = "No se pudo guardar. Revisa tu conexión e inténtalo de nuevo.") }
        }
    }
    private fun aJpeg(bitmap: Bitmap): ByteArray {
        val salida = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, salida)
        return salida.toByteArray()
    }
}
