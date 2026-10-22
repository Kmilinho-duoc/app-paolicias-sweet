package com.example.paoliciassweet.ui.screen

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.paoliciassweet.ui.components.AbonoCard
import com.example.paoliciassweet.ui.components.BotonPrincipal
import com.example.paoliciassweet.ui.components.BotonSecundario
import com.example.paoliciassweet.ui.components.Header
import com.example.paoliciassweet.ui.viewmodel.ComprobanteViewModel
import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.core.content.ContextCompat

@Composable
fun Comprobante(
    reservaId: Int,
    onBack: () -> Unit,
    onEnviado: () -> Unit,
    viewModel: ComprobanteViewModel = viewModel()
) {
    val reserva = viewModel.reserva
    val context = LocalContext.current

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

    LaunchedEffect(reservaId) { viewModel.cargarReserva(context, reservaId) }

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
                subtitulo = "Sube la foto o captura de tu transferencia del 50% para bloquear la fecha de tu evento.",
                onBack = onBack
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (reserva != null) {
                AbonoCard(abono = reserva.abono, estado = reserva.estado)
            }
            Spacer(modifier = Modifier.height(16.dp))

            val foto = viewModel.foto
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFF2B2930))
                    .clickable { abrirCamara() },
                contentAlignment = Alignment.Center
            ) {
                if (foto != null) {
                    Image(
                        bitmap = foto.asImageBitmap(),
                        contentDescription = "Comprobante de transferencia",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("📷", fontSize = 28.sp)
                        Text("Encuadra tu comprobante", color = Color.White, style = MaterialTheme.typography.bodySmall)
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            BotonPrincipal(
                texto = "Enviar comprobante",
                enabled = reserva != null && reserva.estado != "Enviado" && viewModel.foto != null,
                onClick = {
                    viewModel.onEnviarClick(context) {
                        vibrar(context, 1100L)
                        Toast.makeText(context, "¡Comprobante enviado! Recibirás un correo sobre tu validación.", Toast.LENGTH_SHORT).show()
                        onEnviado()
                    }
                }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
