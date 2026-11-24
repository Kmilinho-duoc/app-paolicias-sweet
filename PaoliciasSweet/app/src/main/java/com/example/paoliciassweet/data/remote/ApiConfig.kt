package com.example.paoliciassweet.data.remote

// Direcciones de los microservicios. 10.0.2.2 es "tu computador" visto desde el emulador.
// Al subir el backend a la nube, solo hay que cambiar estas direcciones.
object ApiConfig {
    const val PRODUCTOS_URL = "http://52.44.83.20:8081/"
    const val RESERVAS_URL = "http://52.44.83.20:8082/"
    const val USUARIOS_URL = "http://52.44.83.20:8083/"

    // API externa (pronóstico del tiempo, sin clave)
    const val CLIMA_URL = "https://api.open-meteo.com/"
}
