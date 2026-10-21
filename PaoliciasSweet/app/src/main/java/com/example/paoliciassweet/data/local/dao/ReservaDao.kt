package com.example.paoliciassweet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.paoliciassweet.data.local.entity.ReservaEntity

@Dao
interface ReservaDao {

    // guarda la reserva y devuelve el id generado
    @Insert
    suspend fun insertarReserva(reserva: ReservaEntity): Long

    // recupera una reserva por id
    @Query("SELECT * FROM reservas WHERE id = :id LIMIT 1")
    suspend fun getReservaById(id: Int): ReservaEntity?

    // cambia el estado (Pendiente -> Enviado)
    @Query("UPDATE reservas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)
}
