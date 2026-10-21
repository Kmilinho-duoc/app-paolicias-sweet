package com.example.paoliciassweet.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.paoliciassweet.data.local.dao.ReservaDao
import com.example.paoliciassweet.data.local.dao.UsuarioDao
import com.example.paoliciassweet.data.local.entity.ReservaEntity
import com.example.paoliciassweet.data.local.entity.UsuarioEntity

@Database(entities = [UsuarioEntity::class, ReservaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao
    abstract fun reservaDao(): ReservaDao
    companion object {          //propio del patrón singleton
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "Paolicias_database.db" // definimos nombre del archivo SQLite
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}
