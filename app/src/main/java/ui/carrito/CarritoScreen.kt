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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prueba.ui.carrito.CartViewModel


@Composable
fun CarritoScreen(cartViewModel: CartViewModel = viewModel()) {
    val cartItems = cartViewModel.cartItems // OBS: sin 'by'

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Carrito de compras", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(cartItems, key = { it.producto.id }) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Row(modifier = Modifier.padding(12.dp)) {
                        Image(
                            painter = painterResource(id = item.producto.imagenes),
                            contentDescription = item.producto.titulo,
                            modifier = Modifier
                                .size(80.dp)
                                .padding(end = 12.dp)
                        )

                        Column(modifier = Modifier.weight(1f)) {
                            Text("Producto: ${item.producto.titulo}")
                            Text("Cantidad: ${item.cantidad}")
                            Text("Precio unitario: $${item.producto.precio}")

                            Row(
                                modifier = Modifier.padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                IconButton(onClick = { cartViewModel.increaseQuantity(item.producto) }) {
                                    Icon(Icons.Default.Add, contentDescription = "Aumentar")
                                }
                                IconButton(onClick = { cartViewModel.decreaseQuantity(item.producto) }) {
                                    Icon(Icons.Default.Remove, contentDescription = "Disminuir")
                                }
                                IconButton(onClick = { cartViewModel.eliminarProducto(item.producto) }) {
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



