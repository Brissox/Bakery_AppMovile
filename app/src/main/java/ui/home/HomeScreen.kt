package com.example.prueba.ui.home


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
import androidx.compose.ui.unit.sp
import com.example.prueba.R
import com.example.prueba.ui.home.components.AnimatedLogo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit,
    onRecoverClick: () -> Unit,
) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Pasteleria Mil Sabores") }) }
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
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement =Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedLogo(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp),
        )
        Spacer(modifier = Modifier.height(50.dp))

        Text("¡Bienvenido!",  fontSize = 30.sp)

        Spacer(modifier = Modifier.height(50.dp))

        Button(onClick = onLoginClick) { Text("Login",
            fontSize = 22.sp,
            modifier = Modifier.size(width = 250.dp, height = 40.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            textAlign = TextAlign.Center) }

        Spacer(modifier = Modifier.height(50.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TextButton(onClick = onRegisterClick) { Text("Registrarse", fontSize = 18.sp) }
            TextButton(onClick = onRecoverClick) { Text("Recuperar contraseña", fontSize = 18.sp) }
        }
        Spacer(modifier = Modifier.height(100.dp))

        Text("B2C Copyright - 2025",  fontSize = 15.sp)
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
