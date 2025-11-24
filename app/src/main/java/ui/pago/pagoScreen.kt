package ui.pago

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import ui.recover.RecuperarViewModel

@Composable
fun PagoScreen(
    pagoViewModel: PagoViewModel = viewModel(),
    onSent: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Resumen de compra", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(pagoViewModel.cartItems) { item ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("Producto: ${item.productos.nombre}")
                        Text("Cantidad: ${item.cantidad}")
                        Text("Precio unitario: $${item.productos.precio}")
                        Text("Subtotal: $${item.productos.precio * item.cantidad}")
                    }
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Total a pagar: $${pagoViewModel.total}", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                pagoViewModel.realizarPago()
                onCompraExitosa()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pagar compra")
        }
    }
}
