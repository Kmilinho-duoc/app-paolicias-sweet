package com.example.paoliciassweet.ui.navigation

import android.net.Uri
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.paoliciassweet.ui.screen.AdminProductoForm
import com.example.paoliciassweet.ui.screen.AdminProductos
import com.example.paoliciassweet.ui.screen.AdminUsuarios
import com.example.paoliciassweet.ui.screen.Agenda
import com.example.paoliciassweet.ui.screen.Catalogo
import com.example.paoliciassweet.ui.screen.Comprobante
import com.example.paoliciassweet.ui.screen.Detalle
import com.example.paoliciassweet.ui.screen.Login
import com.example.paoliciassweet.ui.screen.Register
import com.example.paoliciassweet.ui.screen.Reserva
import com.example.paoliciassweet.ui.screen.Reservas
import com.example.paoliciassweet.ui.screen.Usuario
import com.example.paoliciassweet.ui.viewmodel.CarritoViewModel

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val AGENDA = "agenda/{email}"       // rol Administrador: pestaña 1 (reservas)
    const val ADMIN_PRODUCTOS = "admin_productos/{email}" // rol Administrador: pestaña 2
    const val ADMIN_USUARIOS = "admin_usuarios/{email}"   // rol Administrador: pestaña 3
    const val ADMIN_PRODUCTO = "admin_producto/{id}"      // formulario de producto (id 0 = nuevo)
    const val CATALOGO = "catalogo/{email}"   // pestaña 1 de la barra inferior (rol Cliente)
    const val RESERVAS = "reservas/{email}"   // pestaña 2
    const val USUARIO = "usuario/{email}"     // pestaña 3
    const val DETALLE = "detalle/{productoId}"
    const val RESERVA = "reserva/{email}"
    const val COMPROBANTE = "comprobante/{reservaId}"

    // Rutas con argumentos incluidos
    fun agenda(email: String) = "agenda/${Uri.encode(email)}"
    fun adminProductos(email: String) = "admin_productos/${Uri.encode(email)}"
    fun adminUsuarios(email: String) = "admin_usuarios/${Uri.encode(email)}"
    fun adminProducto(id: Int) = "admin_producto/$id"
    fun catalogo(email: String) = "catalogo/${Uri.encode(email)}"
    fun reservas(email: String) = "reservas/${Uri.encode(email)}"
    fun usuario(email: String) = "usuario/${Uri.encode(email)}"
    fun detalle(productoId: Int) = "detalle/$productoId"
    fun reserva(email: String) = "reserva/${Uri.encode(email)}"
    fun comprobante(reservaId: Int) = "comprobante/$reservaId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // El carrito se comparte entre catálogo, detalle y reserva: se crea una sola vez aquí
    val carritoViewModel: CarritoViewModel = viewModel()

    // Cambiar de pestaña: vuelve al catálogo y deja apilada solo la pestaña elegida
    val irATab: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Routes.CATALOGO) { inclusive = false }
            launchSingleTop = true
        }
    }

    // Administrador: cambia de pestaña dejando apilada solo la agenda y la pestaña elegida
    val irAAdmin: (String) -> Unit = { ruta ->
        navController.navigate(ruta) {
            popUpTo(Routes.AGENDA) { inclusive = false }
            launchSingleTop = true
        }
    }
    val cerrarSesionAdmin: () -> Unit = {
        navController.navigate(Routes.LOGIN) {
            popUpTo(Routes.AGENDA) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,

        // Transiciones simples entre pantallas
        enterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) + fadeIn(tween(300))
        },
        exitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Left, tween(300)) + fadeOut(tween(300))
        },
        popEnterTransition = {
            slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) + fadeIn(tween(300))
        },
        popExitTransition = {
            slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Right, tween(300)) + fadeOut(tween(300))
        }
    ) {
        composable(Routes.LOGIN) {
            Login(
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onLoginSuccess = { email, rol ->
                    // cada rol entra a su propia pantalla
                    val destino = if (rol == "Administrador") Routes.agenda(email) else Routes.catalogo(email)
                    navController.navigate(destino) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.REGISTER) {
            Register(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        // limpia la pila para no acumular pantallas login activas
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }

        // Rol Administrador: 3 pestañas (reservas, productos y usuarios) con fundido corto
        composable(
            Routes.AGENDA,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Agenda(
                onProductos = { irAAdmin(Routes.adminProductos(email)) },
                onUsuarios = { irAAdmin(Routes.adminUsuarios(email)) },
                onCerrarSesion = cerrarSesionAdmin
            )
        }

        composable(
            Routes.ADMIN_PRODUCTOS,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            AdminProductos(
                onReservas = { irAAdmin(Routes.agenda(email)) },
                onUsuarios = { irAAdmin(Routes.adminUsuarios(email)) },
                onCerrarSesion = cerrarSesionAdmin,
                onNuevo = { navController.navigate(Routes.adminProducto(0)) },
                onEditar = { id -> navController.navigate(Routes.adminProducto(id)) }
            )
        }

        composable(
            Routes.ADMIN_USUARIOS,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            AdminUsuarios(
                emailActual = email,
                onReservas = { irAAdmin(Routes.agenda(email)) },
                onProductos = { irAAdmin(Routes.adminProductos(email)) },
                onCerrarSesion = cerrarSesionAdmin
            )
        }

        composable(
            Routes.ADMIN_PRODUCTO,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { entry ->
            val id = entry.arguments?.getInt("id") ?: 0
            AdminProductoForm(
                productoId = id,
                onBack = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        // Las 3 pestañas usan un fundido corto en vez del deslizamiento
        composable(
            Routes.CATALOGO,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Catalogo(
                cantidadCarrito = carritoViewModel.uiState.cantidadItems,
                onCotizar = { productoId -> navController.navigate(Routes.detalle(productoId)) },
                onVerReserva = { navController.navigate(Routes.reserva(email)) },
                onReservas = { irATab(Routes.reservas(email)) },
                onUsuario = { irATab(Routes.usuario(email)) }
            )
        }

        composable(
            Routes.RESERVAS,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Reservas(
                email = email,
                onAdjuntar = { reservaId -> navController.navigate(Routes.comprobante(reservaId)) },
                onCatalogo = { irATab(Routes.catalogo(email)) },
                onUsuario = { irATab(Routes.usuario(email)) }
            )
        }

        composable(
            Routes.USUARIO,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) },
            popEnterTransition = { fadeIn(tween(200)) },
            popExitTransition = { fadeOut(tween(200)) }
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Usuario(
                email = email,
                onCatalogo = { irATab(Routes.catalogo(email)) },
                onReservas = { irATab(Routes.reservas(email)) },
                onCerrarSesion = {
                    // vacía el carrito y vuelve al login cerrando toda la sesión
                    carritoViewModel.vaciar()
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.CATALOGO) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Routes.DETALLE,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { entry ->
            val productoId = entry.arguments?.getInt("productoId") ?: 0
            Detalle(
                productoId = productoId,
                onBack = { navController.popBackStack() },
                onAgregar = { producto, cantidad ->
                    carritoViewModel.agregar(producto, cantidad)
                    navController.popBackStack() // vuelve al catálogo para seguir agregando
                }
            )
        }

        composable(Routes.RESERVA) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Reserva(
                email = email,
                carrito = carritoViewModel.uiState,
                onQuitar = { productoId -> carritoViewModel.quitar(productoId) },
                onBack = { navController.popBackStack() },
                onContinuar = { reservaId ->
                    carritoViewModel.vaciar() // la reserva ya quedó guardada
                    navController.navigate(Routes.comprobante(reservaId)) {
                        popUpTo(Routes.RESERVA) { inclusive = true }
                    }
                }
            )
        }

        composable(
            Routes.COMPROBANTE,
            arguments = listOf(navArgument("reservaId") { type = NavType.IntType })
        ) { entry ->
            val reservaId = entry.arguments?.getInt("reservaId") ?: 0
            Comprobante(
                reservaId = reservaId,
                onBack = { navController.popBackStack() },
                // al enviar, muestra la pestaña Reservas con el nuevo estado
                onEnviado = { email -> irATab(Routes.reservas(email)) }
            )
        }
    }
}
