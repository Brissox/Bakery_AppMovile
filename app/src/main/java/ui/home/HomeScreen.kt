package com.example.prueba.ui.home


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.prueba.R
import ui.home.components.AnimatedLogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Inicio") }) }
    ) { inner ->
        HomeContent(
            modifier = Modifier
                .padding(inner)
                .fillMaxSize()
                .padding(16.dp),
            onLoginClick = onLoginClick,
            onRegisterClick = onRegisterClick,
            onRecoverClick = onRecoverClick
        )
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedLogo(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
        )
        Text("¡Bienvenido!")
        Button(onClick = onLoginClick) { Text("Login") }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onRegisterClick) { Text("Registrarse") }
            TextButton(onClick = onRecoverClick) { Text("Recuperar contraseña") }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    HomeScreen(
        onLoginClick = {},
        onRegisterClick = {},
        onRecoverClick = {}
    )
}
