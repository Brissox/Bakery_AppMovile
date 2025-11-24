package com.example.prueba.ui.carrito

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.R // Asegúrate de importar tu R
import com.example.prueba.ui.carrito.CartViewModel

@Composable
fun CarritoScreen(cartViewModel: CartViewModel = viewModel()) {
    val cartItems = cartViewModel.cartItems 
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de compras", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            // Usamos id_producto como key
            items(cartItems, key = { it.productos.id_producto }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        
                        // --- LÓGICA PARA MOSTRAR LA IMAGEN DESDE DRAWABLE ---
                        val imageResId = remember(item.productos.enlaceimg) {
                            val rawName = item.productos.enlaceimg ?: ""
                            
                            // Limpiamos el nombre igual que en la pantalla principal
                            var nombre = rawName.substringAfterLast('/') // Quita /assets/...
                            nombre = nombre.substringBeforeLast(".")     // Quita .jpg
                            nombre = nombre.lowercase()
                                           .replace(" ", "_")
                                           .replace("-", "_")

                            val id = context.resources.getIdentifier(
                                nombre,
                                "drawable",
                                context.packageName
                            )
                            if (id != 0) id else R.drawable.ic_launcher_foreground
                        }

                        Image(
                            painter = painterResource(id = imageResId),
                            contentDescription = item.productos.nombre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 12.dp)
                        )
                        // ---------------------------------------------------

                        Column(modifier = Modifier.weight(1f)) {
                            // Cambiamos .titulo por .nombre (tu modelo nuevo)
                            Text("Producto: ${item.productos.nombre}")
                            Text("Cantidad: ${item.cantidad}")
                            Text("Precio unitario: $${item.productos.precio}")

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = { cartViewModel.increaseQuantity(item.productos) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                }
                                IconButton(onClick = { cartViewModel.decreaseQuantity(item.productos) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                                }
                                IconButton(onClick = { cartViewModel.eliminarProducto(item.productos) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                                }
                            }
                        }
                    }
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Total: $${cartViewModel.calcularTotal()}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { cartViewModel.clearCart() }, modifier = Modifier.fillMaxWidth()) {
            Text("Vaciar carrito")
        }
    }
}