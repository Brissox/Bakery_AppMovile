package com.example.prueba.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prueba.R
import com.example.prueba.ui.RecuperarPasswordScreen
import com.example.prueba.ui.LoginScreen
import com.example.prueba.ui.RegistrarseScreen


//@OptIn(ExperimentalMaterial3Api::class):le dices al compilador que aceptas usar APIs experimentales de Material 3 (por ejemplo, el TopAppBar que aún puede cambiar)
@OptIn(ExperimentalMaterial3Api::class)

//@Composable fun HomeScreen(): declara un composable, es decir, una función que pinta UI en Compose.
@Composable
fun HomeScreen() {

    //Crea y memoriza un NavController para esta UI.
    //remember… evita que se cree uno nuevo en cada recomposición: el controlador se mantiene estable mientras el composable vive.
    val nav = rememberNavController()

    //NavHost es el contenedor de tu grafo (mapa) de pantallas.
    //startDestination = "homeRoot": la ruta inicial al abrir HomeScreen será "homeRoot".
    //Hoy hay definidos 3 destinos: homeroot, login, principal
    NavHost(navController = nav, startDestination = "homeRoot") {

        composable("homeRoot") {
            Scaffold( //layout base con zonas comunes (aquí solo topBar).
                topBar = { TopAppBar(title = { Text("Mi App Kotlin") }) } // barra superior con el título.
            ) { inner -> //El inner que llega al lambda del Scaffold es el padding que deja el topBar; lo aplicas con .padding(inner) para que el contenido no quede bajo la barra.
                HomeContent( //composable separado con el contenido del Home.
                    modifier = Modifier
                        .padding(inner) // respeta el espacio del topBar
                        .fillMaxSize()
                        .padding(16.dp), // margen interno de 16dp
                    onLoginClick = { nav.navigate("login") },
                    onRegisterClick = { nav.navigate("registrarse") },
                    onRecoverClick = { nav.navigate("recuperarContrasena") }
                )
            }
        }

        //Renderiza tu LoginScreen.
        composable("login") {
            LoginScreen(
                onBack = { nav.popBackStack() }, //onBack: hace popBackStack(), o sea, vuelve a la pantalla anterior en el stack (en este caso, homeRoot).
                onLoginSuccess = {
                    nav.navigate("principal") {
                        // deja homeRoot en el back stack para futuro, si quieres limpiar:
                        // popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }

        //Renderiza PrincipalScreen
        composable("principal") {
            PrincipalScreen(
                onLogout = {
                    // cierra sesión y vuelve al homeRoot limpiando el back stack
                    nav.navigate("homeRoot") {
                        //popUpTo("homeRoot") { inclusive = true }
                    }
                }
            )
        }

        //Renderiza Registrarse
        composable ("registrarse") {
            RegistrarseScreen(
                onBack = { nav.popBackStack() },
                onRegistered = {
                    nav.navigate("login") {
                        popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }

        //Renderiza Recuperar contraseña
        composable("recuperarContrasena") {
            RecuperarPasswordScreen(
                onBack = { nav.popBackStack() },
                onSent = {
                    nav.navigate("login") {
                        popUpTo("homeRoot") { inclusive = false }
                    }
                }
            )
        }
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center // centra toda la columna
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f) // ⚡ ancho máximo 90% de la pantalla
                .wrapContentHeight(), // ajusta altura al contenido
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo App",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Fit
            )

            // Título
            Text(
                "¡Bienvenido!",
                style = MaterialTheme.typography.headlineSmall
            )

            // Botón Login
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Login")
            }

            // Botones Registrarse y Olvidé mi contraseña
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedButton(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .weight(1f) // mismo peso
                        .height(50.dp)
                ) {
                    Text("Registrarse")
                }

                OutlinedButton(
                    onClick = onRecoverClick,
                    modifier = Modifier
                        .weight(1f) // mismo peso
                        .height(50.dp)
                ) {
                    Text(
                        "Olvidé mi\ncontraseña",
                        textAlign= TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}
