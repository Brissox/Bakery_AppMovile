package com.example.prueba.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.example.prueba.R
import com.example.prueba.ui.theme.PruebaTheme

data class Producto(
    val nombre: String,
    val precio: String,
    val descripcion: String,
    val imagen: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrincipalScreen(
    onLogout: () -> Unit = {},
    isPreview: Boolean = false
) {
    val saludo = if (isPreview) {
        "Hola usuario (Preview)"
    } else {
        val user = FirebaseAuth.getInstance().currentUser
        "Hola ${user?.email ?: "usuario"}"
    }

    val productos = listOf(
        Producto("Torta de Chocolate", "$15.000", "Clásica y deliciosa", R.drawable.torta_chocolate),
        Producto("Cheesecake Frutilla", "$13.000", "Suave con cobertura de frutilla", R.drawable.cheesecake),
        Producto("Cupcakes Vainilla", "$9.000", "Pack de 6 unidades", R.drawable.cupcakes)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pastelería Dulce Tentación") },
                actions = {
                    TextButton(onClick = {
                        FirebaseAuth.getInstance().signOut()
                        onLogout()
                    }) {
                        Text("Cerrar sesión", color = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Text(
                text = saludo,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Nuestros productos más vendidos 🍰",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(productos) { producto ->
                    ProductoCard(producto)
                }
            }
        }
    }
}

@Composable
fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Imagen del producto (usa una imagen de tu carpeta res/drawable)
            Image(
                painter = painterResource(id = producto.imagen),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .size(90.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(producto.descripcion, style = MaterialTheme.typography.bodyMedium)
                Text(producto.precio, style = MaterialTheme.typography.titleMedium)
                Button(
                    onClick = { /* TODO: agregar al carrito */ },
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    Text("Agregar al carrito")
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PrincipalScreenPreview() {
    PruebaTheme {
        PrincipalScreen(isPreview = true)
    }
}
