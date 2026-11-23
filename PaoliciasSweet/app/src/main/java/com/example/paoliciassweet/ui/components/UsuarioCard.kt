package com.example.paoliciassweet.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.paoliciassweet.data.local.entity.UsuarioEntity

// Cuenta de usuario en la lista del administrador: cambiar de rol o eliminar (salvo cuentas protegidas)
@Composable
fun UsuarioCard(
    usuario: UsuarioEntity,
    protegido: Boolean,
    onCambiarRol: () -> Unit,
    onEliminar: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    usuario.name,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                EstadoChip(usuario.rol)
            }
            FilaDato("Correo", usuario.email)
            FilaDato("Teléfono", usuario.phone)

            if (protegido) {
                Text("Cuenta protegida: no se puede modificar.", style = MaterialTheme.typography.bodySmall)
            } else {
                BotonSecundario(
                    texto = if (usuario.rol == "Administrador") "Cambiar a cliente" else "Cambiar a administrador",
                    onClick = onCambiarRol
                )
                BotonSecundario(texto = "Eliminar cuenta", onClick = onEliminar)
            }
        }
    }
}
