package com.example.paoliciassweet.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.paoliciassweet.ui.screen.Catalogo
import com.example.paoliciassweet.ui.screen.Comprobante
import com.example.paoliciassweet.ui.screen.Login
import com.example.paoliciassweet.ui.screen.Register
import com.example.paoliciassweet.ui.screen.Reserva
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut

object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CATALOGO = "catalogo/{email}"
    const val RESERVA = "reserva/{email}/{productoId}"
    const val COMPROBANTE = "comprobante/{reservaId}"

    // Rutas con argumentos incluidos
    fun catalogo(email: String) = "catalogo/${Uri.encode(email)}"
    fun reserva(email: String, productoId: Int) = "reserva/${Uri.encode(email)}/$productoId"
    fun comprobante(reservaId: Int) = "comprobante/$reservaId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
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
                onLoginSuccess = { email ->
                    //navega al catálogo
                    navController.navigate(Routes.catalogo(email)) {
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

        composable(Routes.CATALOGO) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            Catalogo(
                onCotizar = { productoId ->
                    navController.navigate(Routes.reserva(email, productoId))
                }
            )
        }

        composable(
            Routes.RESERVA,
            arguments = listOf(navArgument("productoId") { type = NavType.IntType })
        ) { entry ->
            val email = entry.arguments?.getString("email").orEmpty()
            val productoId = entry.arguments?.getInt("productoId") ?: 0
            Reserva(
                email = email,
                productoId = productoId,
                onBack = { navController.popBackStack() },
                onContinuar = { reservaId ->
                    navController.navigate(Routes.comprobante(reservaId))
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
                // vuelve al catálogo y cierra reserva + comprobante
                onEnviado = { navController.popBackStack(Routes.CATALOGO, false) }
            )
        }
    }
}
