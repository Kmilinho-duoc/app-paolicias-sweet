package com.example.paoliciassweet.model

// Validaciones simples y reutilizables (sin depender de Android, así se pueden probar con JUnit)
object Validaciones {
    private val regexCorreo = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    private val regexFecha = Regex("^(0[1-9]|[12]\\d|3[01])/(0[1-9]|1[0-2])/\\d{4}$")
    private val regexHora = Regex("^([01]\\d|2[0-3]):[0-5]\\d$")

    fun correoValido(correo: String) = regexCorreo.matches(correo.trim())
    fun fechaValida(fecha: String) = regexFecha.matches(fecha.trim())
    fun horaValida(hora: String) = regexHora.matches(hora.trim())

    // "28/11/2026" -> "2026-11-28" (formato que pide la API del clima y que ordena bien)
    fun fechaIso(fecha: String): String {
        val partes = fecha.trim().split("/")
        return if (partes.size == 3) "${partes[2]}-${partes[1]}-${partes[0]}" else fecha
    }
}
