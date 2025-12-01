package ui.pedido

import Data.Remote.dto.PedidoResp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.*





@Composable
fun pedidoScreen(viewModel: PedidoViewModel) {
    val state by viewModel.uiState.collectAsState()

    when {
        state.loading -> CargandoBox()
        state.error != null -> ErrorBox(mensaje = state.error ?: "Error") {
            viewModel.cargarPedidos(uid = String())
        }
        else -> ListarPedidos(state.items)
    }
}

@Composable
private fun CargandoBox() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorBox(mensaje: String, onReintentar: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Ocurrió un problema:\n$mensaje", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(12.dp))
            Button(onClick = onReintentar) { Text("Reintentar") }
        }
    }
}

@Composable
private fun ListarPedidos(items: List<PedidoResp>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Lista de pedidos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                textAlign = TextAlign.Center
            )
        }

        items(items) { f -> TarjetaPedido(f) }
    }
}

@Composable
fun TarjetaPedido(pedido: PedidoResp) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            val fechaFormateada = pedido.fechaCreacion?.format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
            ) ?: "Sin fecha"

            Text(
                text = "Fecha: $fechaFormateada",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )

            Divider()

            Text("Cantidad de productos: ${pedido.cantidadProductos}")
            Text("Método de pago: ${pedido.metodoPago}")
            Text("Descuentos: $${pedido.descuentos ?: 0}")

            Text(
                text = "Total: $${pedido.total ?: 0}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Divider()

            Text(
                text = "Detalle del pedido",
                style = MaterialTheme.typography.titleSmall
            )

            pedido.items.forEach { detalle ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text("Nombre Producto: ${detalle.nombreProducto}")
                    Text("Cantidad: ${detalle.cantidad}")
                    Text("Precio unitario: $${detalle.precioUnitario}")
                    Text("Subtotal: $${detalle.subtotal}")
                }
            }
        }
    }
}
