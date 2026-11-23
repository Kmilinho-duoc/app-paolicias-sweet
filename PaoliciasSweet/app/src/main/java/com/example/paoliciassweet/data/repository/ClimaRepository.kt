package com.example.paoliciassweet.data.repository

import com.example.paoliciassweet.data.remote.RetrofitClient
import com.example.paoliciassweet.data.remote.api.ClimaApi
import com.example.paoliciassweet.model.Validaciones
import kotlin.math.roundToInt

// API externa: devuelve un texto con el pronóstico de Vallenar para la fecha, o null si no hay datos
class ClimaRepository(private val api: ClimaApi = RetrofitClient.clima) {

    suspend fun pronostico(fecha: String): String? = runCatching {
        val dia = Validaciones.fechaIso(fecha)
        val diario = api.pronostico(
            -28.5708, -70.7581, // Vallenar
            "temperature_2m_max,temperature_2m_min,precipitation_probability_max",
            "auto", dia, dia
        ).daily
        val max = diario?.max?.firstOrNull()
        val min = diario?.min?.firstOrNull()
        val lluvia = diario?.lluvia?.firstOrNull() ?: 0
        if (max == null || min == null) null
        else "Pronóstico en Vallenar: ${max.roundToInt()}° / ${min.roundToInt()}° · lluvia $lluvia%"
    }.getOrNull()
}
