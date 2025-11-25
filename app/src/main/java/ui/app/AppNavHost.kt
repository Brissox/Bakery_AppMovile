package ui.app

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba.ui.carrito.CarritoScreen
import com.example.prueba.ui.carrito.CartViewModel
import com.example.prueba.ui.home.HomeScreen
import com.example.prueba.ui.login.LoginScreen
import com.example.prueba.ui.principal.PrincipalScreen
import com.example.prueba.ui.register.RegistrarseScreen
import com.example.prueba.ui.recover.RecuperarPasswordScreen
import ui.pago.PagoScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost() {

    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = Route.HomeRoot.path
    ) {

        composable(Route.HomeRoot.path) {
            HomeScreen(
                onLoginClick = { navController.navigate(Route.Login.path) },
                onRegisterClick = { navController.navigate(Route.Register.path) },
                onRecoverClick = { navController.navigate(Route.RecoverPassword.path) }
            )
        }

        composable(Route.Login.path) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoginSuccess = {
                    navController.navigate(Route.Principal.path) {
                        launchSingleTop = true
                    }
                }
            )
        }

        composable(Route.Register.path) {
            RegistrarseScreen(
                onBack = { navController.popBackStack() },
                onRegistered = {
                    navController.navigate(Route.Login.path)
                }
            )
        }

        composable(Route.RecoverPassword.path) {
            RecuperarPasswordScreen(
                onBack = { navController.popBackStack() },
                onSent = { navController.navigate(Route.Login.path) }
            )
        }

        composable(Route.Principal.path) {
            PrincipalScreen(
                onLogout = {
                    navController.navigate(Route.HomeRoot.path) {
                        popUpTo(Route.HomeRoot.path) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onCheckout = {
                    navController.navigate(Route.Pago.path)
                }
            )
        }

        composable("carrito") {
            CarritoScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }

        composable(Route.Pago.path) {
            PagoScreen(
                navController = navController,
                cartViewModel = cartViewModel
            )
        }
    }
}
