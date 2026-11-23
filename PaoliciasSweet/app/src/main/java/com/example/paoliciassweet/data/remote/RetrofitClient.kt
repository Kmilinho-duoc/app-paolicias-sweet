package com.example.paoliciassweet.data.remote

import com.example.paoliciassweet.data.remote.api.ClimaApi
import com.example.paoliciassweet.data.remote.api.ProductosApi
import com.example.paoliciassweet.data.remote.api.ReservasApi
import com.example.paoliciassweet.data.remote.api.UsuariosApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Crea una sola vez cada servicio de Retrofit
object RetrofitClient {
    private val http = OkHttpClient.Builder()
        .connectTimeout(5, TimeUnit.SECONDS) // si el servidor no responde, falla rápido
        .readTimeout(15, TimeUnit.SECONDS)
        // muestra en Logcat cada petición (filtrar por "OkHttp")
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
        .build()

    private fun <T> crear(baseUrl: String, api: Class<T>): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(api)

    val productos: ProductosApi by lazy { crear(ApiConfig.PRODUCTOS_URL, ProductosApi::class.java) }
    val reservas: ReservasApi by lazy { crear(ApiConfig.RESERVAS_URL, ReservasApi::class.java) }
    val usuarios: UsuariosApi by lazy { crear(ApiConfig.USUARIOS_URL, UsuariosApi::class.java) }
    val clima: ClimaApi by lazy { crear(ApiConfig.CLIMA_URL, ClimaApi::class.java) }
}
