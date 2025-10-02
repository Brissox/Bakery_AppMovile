package ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.ui.tooling.preview.Preview
import cl.duoc.bakery_app.ui.theme.Bakery_appTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    onLogout: () -> Unit = {} //onLogout: callback que se ejecuta tras cerrar sesión. Tiene valor por defecto vacío {}, así no es obligatorio pasarlo.
) {
    val user = FirebaseAuth.getInstance().currentUser //obtiene el usuario actualmente autenticado.
    val saludo = "Hola ${user?.email ?: "usuario"}" //Si el usuario existe, muestra su email; si no, muestra el texto "usuario"


    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Principal") },
                actions = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) { Text("Logout") }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(saludo, style = MaterialTheme.typography.headlineSmall)
            Text("Bienvenido a tu pantalla principal.")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrincipalScreenPreview() {
    Bakery_appTheme {
        PrincipalScreen(
            onLogout = {} // no hace nada en el preview
        )
    }
}