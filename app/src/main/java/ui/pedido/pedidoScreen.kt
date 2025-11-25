package ui.pedido


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun PedidoScreen(
    pedidoViewModel: PedidoViewModel = viewModel()
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            "Mis pedidos",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {

            items(pedidoViewModel.pedidos) { pedido ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text("Fecha: ${pedidoViewModel.formatFecha(pedido.FECHA)}")
                        Text("Cantidad de productos: ${pedido.CANTIDAD_PRODUCTOS}")
                        Text("Subtotal: $${pedido.SUBTOTAL}")
                        Text("Precio unitario: $${pedido.PRECIO_UNITARIO}")
                        Text("Cantidad: ${pedido.CANTIDAD}")
                        Text("Descuentos: $${pedido.Descuentos}")
                        Text("Método de pago: ${pedido.METODO_DE_PAGO}")
                        Text("Total final: $${pedido.TOTAL}", style = MaterialTheme.typography.titleMedium)

                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PedidoScreenPreview() {
    PedidoScreen()
}