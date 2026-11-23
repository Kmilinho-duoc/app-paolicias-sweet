package com.example.paoliciassweet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.paoliciassweet.data.local.entity.UsuarioEntity

@Dao
interface UsuarioDao {

    // registrar un nuevo usuario en la base de datos sqlite
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun registerUser(user: UsuarioEntity)

    // busca un usuario por correo
    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UsuarioEntity?

    // administración de usuarios (rol Administrador)
    @Query("SELECT * FROM users ORDER BY name")
    suspend fun getAll(): List<UsuarioEntity>

    @Query("UPDATE users SET rol = :rol WHERE id = :id")
    suspend fun updateRol(id: Int, rol: String)

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: Int)
}
