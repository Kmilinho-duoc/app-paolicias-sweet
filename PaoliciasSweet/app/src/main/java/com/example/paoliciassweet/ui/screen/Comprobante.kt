package com.example.paoliciassweet.ui.screen

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.AbonoCard
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.components.OpcionPago
import com.example.paoliciassweet.ui.viewmodel.ComprobanteViewModel

@Composable
fun Comprobante(
    reservaId: Int,
    onBack: () -> Unit,
    onEnviado: (String) -> Unit, // entrega el correo del usuario para volver a sus reservas
    viewModel: ComprobanteViewModel = viewModel()
) {
    val reserva = viewModel.reserva
    val context = LocalContext.current
    val esTransferencia = viewModel.metodoPago == "Transferencia"

    // Depósito en banco: foto con la cámara (permiso CAMERA)
    val tomarFoto = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        viewModel.onFotoTomada(bitmap)
    }
    val pedirPermiso = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { concedido ->
        if (concedido) tomarFoto.launch(null)
        else Toast.makeText(context, "Necesitamos el permiso de cámara para adjuntar el comprobante.", Toast.LENGTH_SHORT).show()
    }
    val abrirCamara = {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            tomarFoto.launch(null)
        } else {
            pedirPermiso.launch(Manifest.permission.CAMERA)
        }
    }

    // Transferencia: captura desde la galería (selector de fotos del sistema, no necesita permisos)
    val elegirImagen = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.onImagenElegida(context, uri)
    }
    val abrirGaleria = {
        elegirImagen.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    LaunchedEffect(reservaId) { viewModel.cargarReserva(reservaId) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Header(
                titulo = "Adjuntar comprobante",
                subtitulo = "Elige cómo pagarás la garantía del 50% para bloquear la fecha de tu evento.",
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (reserva != null) {
                AbonoCard(abono = reserva.abono, estado = reserva.estado)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Elección del método de pago de la garantía
            Text("Método de pago", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
            OpcionPago(
                titulo = "Transferencia electrónica",
                descripcion = "Adjunta la captura de tu transferencia desde la galería.",
                seleccionada = esTransferencia,
                onClick = { viewModel.onMetodoChanged("Transferencia") }
            )
            Spacer(modifier = Modifier.height(8.dp))
            OpcionPago(
                titulo = "Depósito en banco",
                descripcion = "Toma una foto del comprobante de tu depósito.",
                seleccionada = !esTransferencia,
                onClick = { viewModel.onMetodoChanged("Depósito") }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Recuadro del comprobante: abre la galería o la cámara según el método elegido
            val foto = viewModel.foto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF2B2930))
                    .clickable { if (esTransferencia) abrirGaleria() else abrirCamara() },
                contentAlignment = Alignment.Center
            ) {
                if (foto != null) {
                    Image(
                        bitmap = foto.asImageBitmap(),
                        contentDescription = "Comprobante de pago",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(if (esTransferencia) "🖼️" else "📷", fontSize = 28.sp)
                        Text(
                            if (esTransferencia) "Toca para elegir la captura" else "Toca para tomar la foto",
                            color = Color.White,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            if (viewModel.error != null) {
                Text(viewModel.error.orEmpty(), color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))
            }
            BotonPrincipal(
                texto = "Enviar comprobante",
                enabled = reserva != null && reserva.estado != "Enviado" && viewModel.foto != null,
                onClick = {
                    viewModel.onEnviarClick {
                        vibrar(context, 1000L)
                        Toast.makeText(context, "¡Comprobante enviado! Validaremos tu garantía.", Toast.LENGTH_SHORT).show()
                        onEnviado(reserva?.clienteEmail.orEmpty())
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Notifications, contentDescription = null, modifier = Modifier.size(16.dp))
                Text("Te avisaremos con una notificación cuando validen tu abono.", style = MaterialTheme.typography.bodySmall)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
