package com.example.paoliciassweet.data.remote.api

import com.example.paoliciassweet.data.remote.dto.ClimaDto
import retrofit2.http.GET
import retrofit2.http.Query

// API externa: pronóstico del tiempo (Open-Meteo)
interface ClimaApi {
    @GET("v1/forecast")
    suspend fun pronostico(
        @Query("latitude") latitud: Double,
        @Query("longitude") longitud: Double,
        @Query("daily") diario: String,
        @Query("timezone") zonaHoraria: String,
        @Query("start_date") desde: String,
        @Query("end_date") hasta: String
    ): ClimaDto
}
