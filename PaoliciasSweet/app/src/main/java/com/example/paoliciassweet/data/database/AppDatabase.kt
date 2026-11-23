package com.example.paoliciassweet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.paoliciassweet.data.local.dao.UsuarioDao
import com.example.paoliciassweet.data.local.entity.UsuarioEntity

// SQLite local: solo guarda los usuarios (login y registro). Lo demás vive en los microservicios.
@Database(entities = [UsuarioEntity::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    companion object {          //propio del patrón singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Paolicias_database.db" // definimos nombre del archivo SQLite
                ).fallbackToDestructiveMigration(true) // si cambia la estructura, recrea la base (prototipo)
                    .addCallback(object : Callback() {
                        // Crea el usuario administrador de prueba si todavía no existe
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            db.execSQL(
                                "INSERT INTO users (name, email, phone, password, rol) " +
                                    "SELECT 'Administrador', 'admin@paolicias.cl', '900000000', 'Admin123', 'Administrador' " +
                                    "WHERE NOT EXISTS (SELECT 1 FROM users WHERE email = 'admin@paolicias.cl')"
                            )
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }

}
