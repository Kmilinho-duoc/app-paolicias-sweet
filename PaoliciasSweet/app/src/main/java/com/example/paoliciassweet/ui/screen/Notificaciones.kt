package com.example.paoliciassweet.ui.screen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

private const val CANAL = "reservas_por_validar"

// Recurso nativo del administrador: notificación local cuando hay comprobantes por validar
fun notificarPendientes(context: Context, cantidad: Int) {
    // Android 13 o superior exige el permiso POST_NOTIFICATIONS
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
        ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
    ) return

    val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        manager.createNotificationChannel(
            NotificationChannel(CANAL, "Reservas por validar", NotificationManager.IMPORTANCE_DEFAULT)
        )
    }

    val notificacion = NotificationCompat.Builder(context, CANAL)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle("Reservas por validar")
        .setContentText("Tienes $cantidad reserva(s) con comprobante pendiente de validación.")
        .setAutoCancel(true)
        .build()
    manager.notify(1, notificacion)
}
