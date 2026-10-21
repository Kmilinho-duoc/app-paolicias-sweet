package com.example.paoliciassweet.data.local.entity
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class ReservaEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val usuarioEmail: String,
    val producto: String,
    val fecha: String,
    val hora: String,
    val asistentes: Int,
    val observaciones: String,
    val total: Int,
    val abono: Int,
    val estado: String
)
